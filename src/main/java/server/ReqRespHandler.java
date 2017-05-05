package server;

import common.GamePublicData;
import common.PlayerToken;
import common.RRClientNotification;
import common.RemoteMethodCall;
import server_store.ServerStore;
import server_store.StoreAction;
import store_actions.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 *
 * Manages a connection with the client in a request response fashion.
 */
public class ReqRespHandler extends Thread {

    private final Socket socket;
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;
    private final ServerStore serverStore;
    private final ClientMethodsNamesProvider clientMethodsNamesProvider;

    public ReqRespHandler(Socket socket) {
        this.clientMethodsNamesProvider = ClientMethodsNamesProvider.getInstance();
        this.serverStore = ServerStore.getInstance();
        this.socket = socket;
        try {
            this.objectInputStream = new ObjectInputStream(socket.getInputStream());
            this.objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            this.objectOutputStream.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * Reads as a {@link RemoteMethodCall} the current object sent by the client.
     *
     * @return The {@link RemoteMethodCall} the client has just sent.
     * @throws IOException            Networking problem.
     * @throws ClassNotFoundException Object casting problem.
     */
    private RemoteMethodCall receiveData() throws IOException, ClassNotFoundException {
        return (RemoteMethodCall) this.objectInputStream.readObject();
    }

    /**
     * Sends to the client a {@link RemoteMethodCall}.
     *
     * @param remoteMethodCall The {@link RemoteMethodCall} to be sent to the client.
     * @throws IOException Networking problem.
     */
    private void sendData(RemoteMethodCall remoteMethodCall) throws IOException {
        this.objectOutputStream.writeObject(remoteMethodCall);
        this.objectOutputStream.flush();
    }

    /**
     * Parses a {@link RemoteMethodCall} and invokes a method defined in this class (if it exists).
     *
     * @param remoteMethodCall The {@link RemoteMethodCall} to be parsed.
     * @throws NoSuchMethodException     Reflection problem.
     * @throws IllegalAccessException    Reflection problem.
     * @throws InvocationTargetException Reflection problem.
     */
    private void performReceivedMethodCall(RemoteMethodCall remoteMethodCall) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String methodName = remoteMethodCall.getMethodName();
        ArrayList<Object> parameters = remoteMethodCall
                .getMethodParameters();
        Class<?>[] parametersClasses = new Class[parameters.size()];
        for (int i = 0; i < parameters.size(); i++) {
            if (parameters.get(i).getClass().getName().contains("Action")) {
                parametersClasses[i] = parameters.get(i).getClass()
                        .getSuperclass();
            } else {
                parametersClasses[i] = parameters.get(i).getClass();
            }

        }
        this.getClass().getDeclaredMethod(methodName, parametersClasses)
                .invoke(this, parameters.toArray());
    }

    /**
     * Closes all the stream associated with the socket associated with this class.
     * This socket is then closed too.
     */
    private void closeDataFlow() {
        try {
            this.objectOutputStream.close();
            this.objectInputStream.close();
            this.socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * A service that sends to the client/player the list of all available games.
     * This method is invoked by reflection.
     *
     * @throws IOException Networking problem.
     */
    private void getGames() throws IOException {
        List<Game> games = this.serverStore.getState().getGames();
        List<GamePublicData> gamesList = new ArrayList<GamePublicData>();
        for (Game game : games) {
            gamesList.add(game.getGamePublicData());
        }
        ArrayList<Object> parameters = new ArrayList<Object>();
        parameters.add(gamesList);
        this.sendData(
                new RemoteMethodCall(this.clientMethodsNamesProvider.sendAvailableGames(), parameters));
        //this.serverStore.dispatchAction(new CommunicationRemoveReqRespHandlerAction(this.uuid));
        this.closeDataFlow();
    }

    /**
     * A service that creates a new game upon a map and associates that game to
     * the client/player. A notification is sent to the client/player as well.
     * This method is invoked by reflection.
     *
     * @param gameMapName The name of map to be associated with the new game.
     * @param playerName  The client/player unique identifier.
     * @throws IOException Networking problem.
     */
    private void joinNewGame(String gameMapName, String playerName)
            throws IOException {
        Game game = new Game(gameMapName);
        this.serverStore.dispatchAction(new GamesAddGameAction(game));
        this.serverStore.dispatchAction(new GameAddPlayerAction(game.getGamePublicData().getId(), playerName));
        //this.serverStore.dispatchAction(new CommunicationRemoveReqRespHandlerAction(this.uuid));
        ArrayList<Object> parameters = new ArrayList<>();
        parameters.add(game.getLastRRclientNotification());
        this.sendData(new RemoteMethodCall(this.clientMethodsNamesProvider.syncNotification(), parameters));
        this.closeDataFlow();
    }

    /**
     * A service that associates the client/player to the specified existing
     * game. A notification is sent to the client/player as well.
     * This method is invoked by reflection.
     *
     * @param gameId     The id of game the client wants to join.
     * @param playerName The client/player unique identifier.
     * @throws IOException Networking problem.
     */
    public void joinGame(Integer gameId, String playerName) throws IOException {
        this.serverStore.dispatchAction(new GameAddPlayerAction(gameId, playerName));
        //this.serverStore.dispatchAction(new CommunicationRemoveReqRespHandlerAction(this.uuid));
        Game game = this.serverStore.getState().getGames().get(this.serverStore.getState().getGames().size() - 1);
        ArrayList<Object> parameters = new ArrayList<>();
        parameters.add(game.getLastRRclientNotification());
        this.sendData(new RemoteMethodCall(this.clientMethodsNamesProvider.syncNotification(), parameters));
        this.closeDataFlow();
    }

    /**
     * A service that make the socket associated with this class persists. This method effectvely
     * subscribes a client in the logic of the publisher-subscriber pattern.
     * This method is invoked by reflection.
     *
     * @param playerToken The client identifier.
     * @throws IOException Networking problem.
     */
    private void subscribe(PlayerToken playerToken) throws IOException {
        this.serverStore.dispatchAction(new CommunicationAddPubSubHandlerAction(new PubSubHandler(this.socket, this.objectOutputStream, playerToken)));
        //this.serverStore.dispatchAction(new GameStartGameAction(playerToken.gameId));
        //this.serverStore.dispatchAction(new CommunicationRemoveReqRespHandlerAction(this.uuid));
        Game game = this.getGameById(playerToken.getGameId(), this.serverStore.getState().getGames());
        if (game.getPlayers().size() == 8) {
            this.serverStore.dispatchAction(new GameStartGameAction(game));
        } else if (game.getPlayers().size() == 2) {
            this.serverStore.dispatchAction(new GameStartableGameAction(game,true));
        }
    }

    /**
     * A service that makes that game associated with the given {@link PlayerToken} start, without
     * waiting for 8 players to join it. This method is invoked by reflection.
     *
     * @param playerToken The client identifier.
     * @throws IOException Networking problem.
     */
    private void onDemandGameStart(PlayerToken playerToken) throws IOException {
        Game game = this.getGameById(playerToken.getGameId(), this.serverStore.getState().getGames());
        ArrayList<Object> parameters = new ArrayList<>();
        if (game.getCurrentPlayer().getPlayerToken().equals(playerToken)){
            ServerStore.getInstance().dispatchAction(new GameStartGameAction(game));
            parameters.add(game.getLastRRclientNotification());
        }
        else {
            parameters.add(new RRClientNotification(false, null,null,null));
        }
        this.sendData(new RemoteMethodCall(this.clientMethodsNamesProvider.syncNotification(), parameters));
        this.closeDataFlow();
        //ServerStore.getInstance().dispatchAction(new CommunicationRemoveReqRespHandlerAction(this.uuid));

    }

    /**
     * A service that processes the specified action sent by the client/player
     * and notifies the client/player. This method is invoked by reflection.
     *
     * @param action      The action sent by the client/player to be performed on the
     *                    game.
     * @param playerToken The client/player unique identifier.
     * @throws IOException Networking problem.
     */
    public void makeAction(StoreAction action, PlayerToken playerToken) throws IOException {
        Game game = this.getGameById(playerToken.getGameId(), this.serverStore.getState().getGames());
        this.serverStore.dispatchAction(new GameMakeActionAction(game, playerToken, action));
        ArrayList<Object> parameters = new ArrayList<>();
        parameters.add(game.getLastRRclientNotification());
        this.sendData(new RemoteMethodCall(this.clientMethodsNamesProvider.syncNotification(), parameters));
        this.closeDataFlow();
    }

    /**
     * A service that delivers a text message to all the subscribers of a
     * specific topic in the logic of the pub/sub pattern. This method is invoked by reflection.
     *
     * @param message     the text message to be delivered to all the subscribers of a
     *                    specific topic in the logic of the pub/sub pattern.
     * @param playerToken the token of the player who wants to send the text message.
     *                    From this token the topic(a game), whose subscribers have to
     *                    be delivered the text message, is derived.
     * @throws IOException Networking problem
     */
    public void publishChatMsg(String message,
                               PlayerToken playerToken) throws IOException {
        Game game = this.getGameById(playerToken.getGameId(),this.serverStore.getState().getGames());
        this.serverStore.dispatchAction(new GamePutChatMsg(game, message, playerToken));
        ArrayList<Object> parameters = new ArrayList<>();
        parameters.add(game.getLastRRclientNotification());
        this.sendData(new RemoteMethodCall(this.clientMethodsNamesProvider.syncNotification(), parameters));
        this.closeDataFlow();
    }

    /**
     * Provides the game that has the given id.
     * @param gameId The id of the game to be provided.
     * @return The game that matches the given id.
     * @throws NoSuchElementException If no game matches the given id.
     */
    private Game getGameById(int gameId, List<Game> games) throws NoSuchElementException{
        for (Game game : games){
            if (game.getGamePublicData().getId() == gameId){
                return game;
            }
        }
        throw new NoSuchElementException("No game matches the given id");
    }


    @Override
    public void run() {
        try {
            this.performReceivedMethodCall(this.receiveData());
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException | IOException | ClassNotFoundException e) {
            e.printStackTrace();
            try {
                this.objectOutputStream.close();
                this.objectInputStream.close();
                this.socket.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }

        }
    }
}
