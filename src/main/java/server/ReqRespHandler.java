package server;

import common.GamePublicData;
import common.PlayerToken;
import common.RemoteMethodCall;
import it.polimi.ingsw.cg_19.Game;
import it.polimi.ingsw.cg_19.Player;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by giorgiopea on 19/04/17.
 */
public class ReqRespHandler extends Thread {

    private final Socket socket;
    private final GameManager gameManager;
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;
    private final Queue<RemoteMethodCall> buffer;

    public ReqRespHandler(Socket socket) {
        this.gameManager = GameManager.getInstance();
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

    private RemoteMethodCall receiveData() throws IOException, ClassNotFoundException {
        return (RemoteMethodCall) this.objectInputStream.readObject();
    }

    private void sendData(RemoteMethodCall remoteMethodCall) throws IOException {
        this.objectOutputStream.writeObject(remoteMethodCall);
        this.objectOutputStream.flush();
    }

    private void performReceivedMethodCall(RemoteMethodCall remoteMethodCall) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, InvocationTargetException {
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
        List<GamePublicData> gamesList = new ArrayList<GamePublicData>();
        for (Game game : this.gameManager.getGames()) {
            gamesList.add(game.getPublicData());
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
        Game game = new Game(gameMapName);
        this.gameManager.addGame(game);
        PlayerToken playerToken = game.addPlayer(playerName);
        this.gameManager.addPlayerToGame(playerToken, game.getId());
        ArrayList<Object> parameters = new ArrayList<Object>();
        parameters.add(playerToken);
        this.sendData(
                new RemoteMethodCall("sendToken", parameters));
        SubscriberHandler handler = server.getSocketDataExchange().keepAlive();
        game.addSubscriber(handler);

        parameters.clear();
        parameters.add("You've joined a new game");
        game.notifyListeners(new RemoteMethodCall("publishChatMsg", parameters));

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
        //this.serverStore.dispatchAction(new GameStartGameAction(playerToken.gameId));
        this.serverStore.dispatchAction(new CommunicationRemoveReqRespHandlerAction(this.uuid));
        for (Game game : serverStore.getState().getGames()){
            if (game.gamePublicData.getId() == playerToken.gameId){
                if(game.players.size() == 8){
                    this.serverStore.dispatchAction(new GameStartGameAction(playerToken.gameId));
                    break;
                }
                else if(game.players.size() == 2){
                    startableGame(game);
                    break;
                }
            }
        }

    }
    public void onDemandGameStart(PlayerToken playerToken){
        List<Game> games = ServerStore.getInstance().getState().getGames();
        for ( Game game : games){
            if (game.gamePublicData.getId() == playerToken.gameId
                    && game.currentPlayer.playerToken.equals(playerToken)){
                ServerStore.getInstance().dispatchAction(new GameStartGameAction(game.gamePublicData.getId()));
                ServerStore.getInstance().dispatchAction(new CommunicationRemoveReqRespHandlerAction(this.uuid));
                break;
            }
        }
    }

    private void startableGame(Game game) {
        for (PubSubHandler handler : ServerStore.getInstance().getState().getPubSubHandlers()){
            if (handler.getPlayerToken().equals(game.currentPlayer.playerToken)){
                handler.queueNotification(new RemoteMethodCall("signalStartableGame",new ArrayList<Object>()));
                break;
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
        String playerName = "";
        Game game = gameManager.getGame(token.getGameId());
        for (Player player : game.)
        for (PubSubHandler handler : this.serverStore.getState().getPubSubHandlers()){
            if(handler.getPlayerToken().gameId.equals(token.gameId)){
                ArrayList<Object> parameters = new ArrayList<>();
                parameters.add(playerName+" says: "+message);
                handler.queueNotification(new RemoteMethodCall("publishChatMsg",parameters));
            }
        }
    }

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
