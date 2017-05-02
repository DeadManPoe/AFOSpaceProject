package server;

import common.*;
import it.polimi.ingsw.cg_19.Game;
import it.polimi.ingsw.cg_19.Player;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by giorgiopea on 19/04/17.
 *
 * Manages a connection with the client in a request response fashion.
 */
public class ReqRespHandler extends Thread {

    private final Socket socket;
    private final GameManager gameManager;
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;
    private final CommunicationHandler communicationHandler;
    private final ClientMethodsNamesProvider clientMethodsNamesProvider;

    public ReqRespHandler(Socket socket) {
        this.communicationHandler = CommunicationHandler.getInstance();
        this.gameManager = GameManager.getInstance();
        this.clientMethodsNamesProvider = ClientMethodsNamesProvider.getInstance();
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
    private void performReceivedMethodCall(RemoteMethodCall remoteMethodCall) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
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
        ArrayList<GamePublicData> gamesList = new ArrayList<>();
        for (Game game : this.gameManager.getGames()) {
            gamesList.add(game.getPublicData());
        }
        ArrayList<Object> parameters = new ArrayList<>();
        parameters.add(gamesList);
        this.sendData(
                new RemoteMethodCall(this.clientMethodsNamesProvider.sendAvailableGames(), parameters));
        this.closeDataFlow();
    }

    /**
     * A service that creates a new game upon a map and associates that game to
     * the client/player. A notification is sent to the client/player as well.
     * This method is invoked by reflection.
     *
     * @param gameMapName the name of map to be associated with the new game.
     * @param playerName  the client/player unique identifier.
     * @throws IOException Networking problem.
     */
    private void joinNewGame(String gameMapName, String playerName)
            throws IOException {
        Game game = new Game(gameMapName);
        this.gameManager.addGame(game);
        PlayerToken playerToken = game.addPlayer(playerName);
        ArrayList<Object> parameters = new ArrayList<>();
        parameters.add(new RRClientNotification(true, null, null, playerToken));
        this.sendData(new RemoteMethodCall(this.clientMethodsNamesProvider.syncNotification(), parameters));
        this.closeDataFlow();
    }

    /**
     * A service that associates the client/player to the specified existing
     * game. A notification is sent to the client/player as well.
     * This method is invoked by reflection.
     *
     * @param gameId     the id of game the client wants to join.
     * @param playerName the client/player unique identifier.
     * @throws IOException Networking problem.
     */
    private void joinGame(Integer gameId, String playerName) throws IOException {
        Game game = this.gameManager.getGame(gameId);
        PlayerToken playerToken = game.addPlayer(playerName);
        ArrayList<Object> parameters = new ArrayList<>();
        parameters.add(new RRClientNotification(true, null, null, playerToken));
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
        Game game = this.gameManager.getGame(playerToken.getGameId());
        PubSubHandler pubSubHandler = new PubSubHandler(this.objectOutputStream, playerToken);
        game.addPubSubHandler(pubSubHandler);
        this.communicationHandler.addPubSubHandler(pubSubHandler);
        if (game.getPlayers().size() == 2) {
            for (PubSubHandler _pubSubHandler : game.getPubSubHandlers()) {
                if (_pubSubHandler.getPlayerToken().equals(game.getCurrentPlayer().getPlayerToken())) {
                    _pubSubHandler.queueNotification(new RemoteMethodCall(
                            this.clientMethodsNamesProvider.signalStartableGame(), new ArrayList<>()));
                    break;
                }
            }
        } else if (game.getPlayers().size() == 8) {
            game.startGame();
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
        Game game = this.gameManager.getGame(playerToken.getGameId());
        ArrayList<Object> parameters = new ArrayList<>();
        parameters.add(new RRClientNotification(true, null, null, null));
        this.sendData(new RemoteMethodCall(this.clientMethodsNamesProvider.syncNotification(), parameters));
        this.closeDataFlow();
        game.startGame();
    }

    /**
     * A service that processes the specified action sent by the client/player
     * and notifies the client/player. This method is invoked by reflection.
     *
     * @param action      the action sent by the client/player to be performed on the
     *                    game.
     * @param playerToken the client/player unique identifier.
     * @throws IOException Networking problem.
     */
    private void makeAction(Action action, PlayerToken playerToken) throws IOException {
        Game game = this.gameManager.getGame(playerToken.getGameId());
        ArrayList<Object> parameters = new ArrayList<>();
        parameters.add(game.makeAction(action, playerToken));
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
    private void publishChatMsg(String message,
                                PlayerToken playerToken) throws IOException {
        Game game = this.gameManager.getGame(playerToken.getGameId());
        Player player = game.getPlayer(playerToken);
        for (PubSubHandler handler : game.getPubSubHandlers()) {
            ArrayList<Object> parameters = new ArrayList<>();
            parameters.add(player.getName() + " says: " + message);
            handler.queueNotification(new RemoteMethodCall(this.clientMethodsNamesProvider.chatMessage(), parameters));
        }
        ArrayList<Object> parameters = new ArrayList<>();
        parameters.add(new RRClientNotification(true, null, null, null));
        this.sendData(new RemoteMethodCall(this.clientMethodsNamesProvider.syncNotification(), parameters));
        this.closeDataFlow();
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
