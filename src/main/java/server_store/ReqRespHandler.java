package server_store;

import common.*;
import it.polimi.ingsw.cg_19.Game;
import it.polimi.ingsw.cg_19.Player;
import server.ServerLogger;
import store_actions.CommunicationAddPubSubHandlerAction;
import store_actions.GameAddPlayer;
import store_actions.GameAddPlayerAction;
import store_actions.GameMakeActionAction;
import store_actions.GamesAddGameAction;
import sts.ActionFactory;
import sts.Store;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.logging.Level;

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
        this.buffer = new ArrayBlockingQueue<RemoteMethodCall>(1);
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
        // sendPubNotification and sendToken
        if (!remoteMethodCall.getMethodName().equals("sendPubNotification")
                && !remoteMethodCall.getMethodName().equals("sendToken")) {
            this.closeDataFlow();
        }
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
                new RemoteMethodCall("sendAvailableGames", parameters));
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
        //When a game is created create also the pubsub handler
        this.serverStore.dispatchAction(new GamesAddGameAction(game));
        this.serverStore.dispatchAction(new GameAddPlayerAction(this.uuid, game.gamePublicData.getId()));
        this.serverStore.dispatchAction(new CommunicationAddPubSubHandlerAction(new PubSubHandler(socket)));
        ArrayList<Object> parameters = new ArrayList<Object>();
        parameters.add(playerToken);
        this.sendData(
                new RemoteMethodCall("sendToken", parameters));
        //parameters.clear();
        //this.serverStore.dispatchAction(this.actionFactory.getAction("@GAMES_ADD_PLAYER_TO_GAME",gamePlayer));
        //game.notifyListeners(new RemoteMethodCall("publishChatMsg", parameters));

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
        Map<Integer, server_store.Game> games = this.serverStore.getState().GAMES_BY_ID;
        server_store.Game game = games.get(gameId);
        this.serverStore.dispatchAction(new GameAddPlayerAction(playerName, gameId, this, this.socket));
        PlayerToken playerToken = game.playerNameToToken.get(playerName);
        ArrayList<Object> parameters = new ArrayList<Object>();
        parameters.add(playerToken);
        this.sendData(
                new RemoteMethodCall("sendToken", parameters));
        this.serverStore.dispatchAction(new StartGameAction(gameId));
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
    public void makeAction(Action action, Integer gameId, PlayerToken playerToken)
            throws IOException, InstantiationException, IllegalAccessException {
        this.serverStore.dispatchAction(new GameMakeActionAction(gameId, action, playerToken));
        Map<Integer, server_store.Game> games = this.serverStore.getState().GAMES_BY_ID;
        server_store.Game game = games.get(gameId);
        ArrayList<Object> parameters = new ArrayList<Object>();
        parameters.add(game.lastResponseToClient);
        this.sendData(
                new RemoteMethodCall("sendNotification", parameters));
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
        Map<PlayerToken, Game> games = this.serverStore.getState().GAMES_BY_PLAYERTOKEN;
        Game game = games.get(token);
        Player player = game.fromTokenToPlayer(token);
        ArrayList<Object> parameters = new ArrayList<Object>();
        parameters.add("[" + player.getName() + "]: " + message);
        this.sendData(
                new RemoteMethodCall("ackMessage"));
        //game.notifyListeners(new RemoteMethodCall("publishChatMsg", parameters));
    }

    /**
     * A services that allows a client to force(without waiting the timeout) the
     * start of the game only if there are at least two player
     *
     * @param token The token of the player who wants to start the game
     * @throws IOException
     */
    public void forceGameStart(PlayerToken token) {
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
    }

    public void addRemoteMethodCallToQueue(RemoteMethodCall remoteMethodCall){
        buffer.add(remoteMethodCall);
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
                    if (!remoteMethodCall.getMethodName().equals("sendToken")) {
                        closeDataFlow();
                        mustRun = false;
                    }
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
