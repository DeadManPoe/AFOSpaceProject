package server_store;

import common.Action;
import common.GamePublicData;
import common.PlayerToken;
import common.RemoteMethodCall;
import store_actions.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Represents a thread that handles a request by a client in the logic of the
 * client server pattern
 *
 * @author Andrea Sessa
 * @author Giorgio Pea
 * @version 1.0
 */
public class ReqRespHandler extends Thread {

    private Socket socket;
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;
    private ServerStore serverStore;
    private UUID uuid;
    private final Queue<RemoteMethodCall> buffer;

    public ReqRespHandler(Socket socket) {
        this.uuid = UUID.randomUUID();
        this.serverStore = ServerStore.getInstance();
        this.socket = socket;
        this.buffer = new ConcurrentLinkedQueue<>();
        try {
            this.objectInputStream = new ObjectInputStream(socket.getInputStream());
            this.objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            this.objectOutputStream.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public UUID getUuid() {
        return uuid;
    }

    private RemoteMethodCall receiveData() throws IOException, ClassNotFoundException {
        return (RemoteMethodCall) this.objectInputStream.readObject();
    }

    private void sendData(RemoteMethodCall remoteMethodCall) throws IOException {
        this.objectOutputStream.writeObject(remoteMethodCall);
        this.objectOutputStream.flush();
    }

    private void performReceivedMethodCall(RemoteMethodCall remoteMethodCall) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String methodName = remoteMethodCall.getMethodName();
        ArrayList<Object> parameters = remoteMethodCall
                .getMethodParameters();
        Class<?>[] parametersClasses = new Class[parameters.size()];
        for (int i = 0; i < parameters.size(); i++) {
            if (parameters.get(i).getClass().getName().contains("Action")
                    || parameters.get(i).getClass().getName()
                    .contains("ClientNotification")) {
                parametersClasses[i] = parameters.get(i).getClass()
                        .getSuperclass();
            } else {
                parametersClasses[i] = parameters.get(i).getClass();
            }

        }
        this.getClass().getDeclaredMethod(methodName, parametersClasses)
                .invoke(this, parameters.toArray());
    }

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
     * A service that sends to the client/player the list of all available games
     *
     * @throws IOException
     */
    private void getGames() throws IOException {
        List<server_store.Game> games = this.serverStore.getState().getGames();
        List<GamePublicData> gamesList = new ArrayList<GamePublicData>();
        for (server_store.Game game : games) {
            gamesList.add(game.gamePublicData);
        }
        ArrayList<Object> parameters = new ArrayList<Object>();
        parameters.add(gamesList);
        this.sendData(
                new RemoteMethodCall("setAvailableGames", parameters));
        this.serverStore.dispatchAction(new CommunicationRemoveReqRespHandlerAction(this.uuid));
    }

    /**
     * A service that creates a new game upon a map and associates that game to
     * the client/player. A notification is sent to the client/player as well.
     *
     * @param gameMapName the name of map to be associated with the new game
     * @param playerName  the client/player unique identifier
     * @throws IOException
     */
    public void joinNewGame(String gameMapName, String playerName)
            throws IOException {
        server_store.Game game = new server_store.Game(gameMapName);
        this.serverStore.dispatchAction(new GamesAddGameAction(game));
        this.serverStore.dispatchAction(new GameAddPlayerAction(this.uuid, game.gamePublicData.getId(),playerName));
        this.serverStore.dispatchAction(new CommunicationRemoveReqRespHandlerAction(this.uuid));

    }

    /**
     * A service that associates the client/player to the specified existing
     * game. A notification is sent to the client/player as well
     *
     * @param gameId     the id of game the client wants to join
     * @param playerName the client/player unique identifier
     * @throws IOException
     */
    public void joinGame(Integer gameId, String playerName) throws IOException {
        this.serverStore.dispatchAction(new GameAddPlayerAction(this.uuid, gameId,playerName));
        //this.serverStore.dispatchAction(new CommunicationAddPubSubHandlerAction(new PubSubHandler(objectOutputStream, gameId)));
        //this.serverStore.dispatchAction(new GameStartGameAction(gameId));
        this.serverStore.dispatchAction(new CommunicationRemoveReqRespHandlerAction(this.uuid));
    }
    public void subscribe(PlayerToken playerToken) throws IOException {
        this.serverStore.dispatchAction(new CommunicationAddPubSubHandlerAction(new PubSubHandler(objectOutputStream, playerToken)));
        //this.serverStore.dispatchAction(new GameStartGameAction(playerToken.getGameId()));
        this.serverStore.dispatchAction(new CommunicationRemoveReqRespHandlerAction(this.uuid));
        for (Game game : serverStore.getState().getGames()){
            if (game.gamePublicData.getId() == playerToken.getGameId()){
                if(game.players.size() == 2){
                    this.serverStore.dispatchAction(new GameStartGameAction(playerToken.getGameId()));
                    break;
                }
            }
        }

    }

    /**
     * A service that processes the specified action sent by the client/player
     * and notifies the client/player
     *
     * @param action      the action sent by the client/player to be performed on the
     *                    game
     * @param playerToken the client/player unique identifier
     * @throws IOException
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public void makeAction(StoreAction action, PlayerToken playerToken)
            throws IOException, InstantiationException, IllegalAccessException {
        this.serverStore.dispatchAction(new GameMakeActionAction(playerToken, this.uuid, action));
    }

    /**
     * A service that delivers a text message to all the subscribers of a
     * specific topic in the logic of the pub/sub pattern
     *
     * @param message the text message to be delivered to all the subscribers of a
     *                specific topic in the logic of the pub/sub pattern
     * @param token   the token of the player who wants to send the text message.
     *                From this token the topic(a game), whose subscribers have to
     *                be delivered the text message, is derived.
     * @throws IOException
     */
    public void publishGlobalMessage(String message,
                                     PlayerToken token) throws IOException {
        for (PubSubHandler handler : this.serverStore.getState().getPubSubHandlers()){
            if(handler.getPlayerToken().getGameId().equals(token.getGameId())){
                ArrayList<Object> parameters = new ArrayList<>();
                parameters.add(token.getUUID().toString()+" says: "+message);
                handler.queueNotification(new RemoteMethodCall("publishChatMsg",parameters));
            }
        }
    }

    /**
     * A services that allows a client to force(without waiting the timeout) the
     * start of the game only if there are at least two player
     *
     * @throws IOException
     */
    /*public void forceGameStart(PlayerToken token) {
        Map<PlayerToken, Game> games = this.serverStore.getState().GAMES_BY_PLAYERTOKEN;
        Game game = games.get(token);
        if (game.getPublicData().getPlayersCount() > 1) {
            game.startGame();
        }
        try {
            this.sendData(
                    new RemoteMethodCall("ackMessage"));
        } catch (IOException e) {
            ServerLogger.getLogger().log(Level.SEVERE,
                    "Error in sending ackMessage rmethodcall", e);
        }
    }*/

    public void addRemoteMethodCallToQueue(RemoteMethodCall remoteMethodCall){
        buffer.add(remoteMethodCall);
        synchronized (buffer) {
            buffer.notify();
        }
    }


    @Override
    public void run() {
        boolean mustRun = true;
        while (mustRun) {
            try {
                this.performReceivedMethodCall(this.receiveData());
                RemoteMethodCall remoteMethodCall = buffer.poll();
                if (remoteMethodCall != null) {
                    this.sendData(remoteMethodCall);
                    if (!remoteMethodCall.getMethodName().equals("subscribe") ){
                        closeDataFlow();
                    }
                    mustRun = false;
                } else {
                    // If there are no incoming remote method calls the thread
                    // waits
                    synchronized (buffer) {
                        buffer.wait();
                    }

                }

            } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException | ClassNotFoundException | IOException | InterruptedException e) {
                e.printStackTrace();
            }


        }
    }
}
