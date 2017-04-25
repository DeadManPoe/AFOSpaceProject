package client;

import common.*;
import factories.FermiGameMapFactory;
import factories.GalileiGameMapFactory;
import factories.GalvaniGameMapFactory;
import it.polimi.ingsw.cg_19.GameMap;
import it.polimi.ingsw.cg_19.Player;
import it.polimi.ingsw.cg_19.PlayerType;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * Created by giorgiopea on 25/04/17.
 */
public class ClientServices {
    private Client client;
    private final CommunicationHandler communicationHandler;
    private final ServerMethodsNameProvider serverMethodsNameProvider;
    private final GuiInteractionManager guiInteractionManager;
    private static ClientServices instance = new ClientServices();

    public static ClientServices getInstance(){
        return instance;
    }

    private ClientServices(){
        this.communicationHandler = CommunicationHandler.getInstance();
        this.serverMethodsNameProvider = ServerMethodsNameProvider.getInstance();
        this.guiInteractionManager =

    }

    public void init(Client client){
        this.client = client;
    }


    /**
     * Makes the game associated to this client start. This method is invoked indirectly using reflection.
     *
     * @param mapName The name of the game map.
     */
    private void setMapAndStartGame(String mapName) {

    }

    /**
     * Requests to the server the creation of a new game with a given map.
     *
     * @param gameMapName The name of the map of the game to be created.
     * @param playerName  The name of the client in the game.
     */
    public void joinNewGame(String gameMapName, String playerName) {
        //this.clientStore.dispatchAction(new ClientSetPlayerAction(playerName, null));
        ArrayList<Object> parameters = new ArrayList<>();
        parameters.add(gameMapName);
        parameters.add(playerName);
        try {
            RemoteMethodCall methodCall = this.communicationHandler.newComSession(new RemoteMethodCall("joinNewGame", parameters));
            this.processRemoteInvocation(methodCall);
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        } catch (IOException e1) {
            //If connection is down
            //this.clientStore.dispatchAction(new ClientSetConnectionActiveAction(false));
        }
    }

    /**
     * Requests to the server the join of a game.
     *
     * @param gameId     The id of the game to join.
     * @param playerName The name of the client in the game.
     */
    public void joinGame(Integer gameId, String playerName) {
        //this.clientStore.dispatchAction(new ClientSetPlayerAction(playerName, null));
        ArrayList<Object> parameters = new ArrayList<>();
        parameters.add(gameId);
        parameters.add(playerName);
        try {
            RemoteMethodCall methodCall = this.communicationHandler.newComSession(new RemoteMethodCall("joinGame", parameters));
            this.processRemoteInvocation(methodCall);
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        } catch (IOException e1) {
            //If connection is down
            //this.clientStore.dispatchAction(new ClientSetConnectionActiveAction(false));
        }
    }

    /**
     * Publishes a message in the chat window.
     * This method is invoked indirectly using reflection.
     *
     * @param msg The message to be published.
     */
    private void publishChatMsg(String msg) {
       // this.clientStore.dispatchAction(new ClientSetCurrentChatMessage(msg));
    }


    /**
     * Moves the client to the sector at the given coordinates and executes the other logic related to this action.
     * This action is validated and registered by contacting the game server.
     *
     * @param coordinate The coordinates of the sector to move to
     */
    public void moveToSector(Coordinate coordinate) {
        Sector targetSector = this.client.getGameMap().getSectorByCoords(coordinate);
        //this.clientStore.dispatchAction(new ClientAskAttackAction(false));
        ArrayList<Object> parameters = new ArrayList<>();
        Action action = new MoveAction(targetSector);
        parameters.add(action);
        parameters.add(this.client.getToken());
        try {
            RemoteMethodCall methodCall = this.communicationHandler.newComSession(new RemoteMethodCall("makeAction", parameters));
            this.processRemoteInvocation(methodCall);
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        } catch (IOException e1) {
            //If connection is down
            //this.clientStore.dispatchAction(new ClientSetConnectionActiveAction(false));
        }
        boolean isActionServerValidated = this.client.getCurrentNotification().getActionResult();
        //List<Card> drawnCards = this.clientStore.getState().currentReqRespNotification.getDrawnCards();
        //this.clientStore.dispatchAction(new ClientMoveToSectorAction(targetSector, isActionServerValidated));
       /* if (isActionServerValidated) {
            if (drawnCards.size() == 1) {
                this.clientStore.dispatchAction(
                        new ClientSetDrawnSectorObjectCard(
                                (SectorCard) drawnCards.get(0), null, true));
            } else if (drawnCards.size() == 2) {
                this.clientStore.dispatchAction(
                        new ClientSetDrawnSectorObjectCard(
                                (SectorCard) drawnCards.get(0),
                                (ObjectCard) drawnCards.get(1), true));
            }
        }*/

    }

    /**
     * Signals that there's the possibility, by the client, to start the game without waiting for 8 players to join.
     * This method is invoked indirectly using reflection.
     */
    private void signalStartableGame() {
        //this.clientStore.dispatchAction(new ClientStartableGameAction());
    }

    /**
     * Makes the client use an object card. This action is validated and registered by contacting the game server.
     *
     * @param objectCard The object card to be used
     */
    public void useObjCard(ObjectCard objectCard) {
        //Player player = this.clientStore.getState().player;
        if (this.client.getPrivateDeck().getContent().contains(objectCard)) {
            if (objectCard instanceof LightsObjectCard) {
                //this.clientStore.dispatchAction(new ClientAskSectorToLightAction(true));
            } else if (objectCard instanceof AttackObjectCard) {
                //this.clientStore.dispatchAction(new ClientAskAttackAction(true));
            } else {
                ArrayList<Object> parameters = new ArrayList<>();
                Action action = new UseObjAction(objectCard);
                parameters.add(action);
                parameters.add(this.client.getToken());
                RemoteMethodCall methodCall = null;
                try {
                    methodCall = this.communicationHandler.newComSession(new RemoteMethodCall("makeAction", parameters));
                    this.processRemoteInvocation(methodCall);
                } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                } catch (IOException e1) {
                    //If connection is down

                }
                boolean isActionServerValidated = this.client.getCurrentNotification().getActionResult();
                //this.clientStore.dispatchAction(new ClientUseObjectCard(objectCard, isActionServerValidated));
                if (isActionServerValidated) {
                    if (objectCard instanceof TeleportObjectCard) {
                        //this.clientStore.dispatchAction(new ClientTeleportToStartingSectorAction());
                    } else if (objectCard instanceof SuppressorObjectCard) {
                        //this.clientStore.dispatchAction(new ClientSuppressAction(true));
                    } else if (objectCard instanceof AdrenalineObjectCard) {
                        //this.clientStore.dispatchAction(new ClientAdrenlineAction());
                    }
                }

            }
        }


    }

    /**
     * Allows the client to act on the game as consequence of a favorable turn switch.
     * This method is invoked indirectly using reflection.
     */
    private void startTurn() {
        //this.clientStore.dispatchAction(new ClientStartTurnAction());
    }

    /**
     * Makes the client indicate a sector to the other players of the game as a consequence of
     * a global noise sector card. This action is validated and registered by contacting the game server.
     *
     * @param coordinate The coordinates of the sector to be indicated
     * @param hasObject  If there's an object card associated with the global noise sector card(irrelevant)
     */
    public void globalNoise(Coordinate coordinate, boolean hasObject) {
        Sector targetSector = this.client.getGameMap().getSectorByCoords(coordinate);
        if (targetSector != null) {
            SectorCard globalNoiseCard = new GlobalNoiseSectorCard(hasObject,
                    targetSector);
            ArrayList<Object> parameters = new ArrayList<>();
            Action action = new UseSectorCardAction(globalNoiseCard);
            parameters.add(action);
            parameters.add(this.client.getToken());
            try {
                RemoteMethodCall methodCall = this.communicationHandler.newComSession(new RemoteMethodCall(this.serverMethodsNameProvider.makeAction(), parameters));
                this.processRemoteInvocation(methodCall);
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e1) {

            }
            //boolean isActionServerValidated = this.clientStore.getState().currentReqRespNotification.getActionResult();
            //this.clientStore.dispatchAction(new ClientSetDrawnSectorObjectCard(null, null, isActionServerValidated));
        } else {
            throw new IllegalArgumentException(
                    "The sector you have indicated does not exists, please try again");
        }
    }

    /**
     * Makes the client indicate a sector and discover if there are players in the sector itself or in the adjacent ones
     * as a consequence of a lights object card. This action is validated and registered by contacting the game server.
     *
     * @param coordinate The coordinates of the sector for the ligths object card effect
     */
    public void lights(Coordinate coordinate) {
        Sector targetSector = this.clientStore.getState().gameMap.getSectorByCoords(coordinate);
        if (targetSector != null) {
            ObjectCard lightsCard = new LightsObjectCard(targetSector);
            ArrayList<Object> parameters = new ArrayList<>();
            StoreAction action = new UseObjAction(lightsCard);
            parameters.add(action);
            parameters.add(this.clientStore.getState().player.playerToken);
            RemoteMethodCall remoteMethodCall = null;
            try {
                remoteMethodCall = this.communicationHandler.newComSession(new RemoteMethodCall("makeAction", parameters));
                this.processRemoteInvocation(remoteMethodCall);
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e1) {
                //If connection is down
                this.clientStore.dispatchAction(new ClientSetConnectionActiveAction(false));
            }
            boolean isActionServerValidated = this.clientStore.getState().currentReqRespNotification.getActionResult();
            this.clientStore.dispatchAction(new ClientUseObjectCard(lightsCard, isActionServerValidated));
            if (isActionServerValidated) {
                this.clientStore.dispatchAction(new ClientAskSectorToLightAction(false));
            }
        } else {
            throw new IllegalArgumentException(
                    "Undefined sector, please try again");
        }

    }

    /**
     * Makes the client end its turn. This action is validated and registered by contacting the game server.
     */
    public void endTurn() {
        ArrayList<Object> parameters = new ArrayList<>();
        StoreAction action = new EndTurnAction();
        parameters.add(action);
        parameters.add(this.clientStore.getState().player.playerToken);
        try {
            RemoteMethodCall remoteMethodCall = this.communicationHandler.newComSession(new RemoteMethodCall("makeAction", parameters));
            this.processRemoteInvocation(remoteMethodCall);
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        } catch (IOException e) {
            //If connection is down
            this.clientStore.dispatchAction(new ClientSetConnectionActiveAction(false));
        }
        boolean isActionServerValidated = this.clientStore.getState().currentReqRespNotification.getActionResult();
        this.clientStore.dispatchAction(new ClientEndTurnAction(isActionServerValidated));
    }

    private void forceEndTurn(){
        RRClientNotification clientNotification = new RRClientNotification();
        clientNotification.setMessage("You have taken too much to act, you will skip your turn");
        this.clientStore.dispatchAction(new ClientSetCurrentReqRespNotificationAction(clientNotification));
        this.clientStore.dispatchAction(new ClientEndTurnAction(true));
    }

    /**
     * Makes the client discard a given object card. This action is validated and registered by contacting the game server.
     *
     * @param objectCard The object card to be discarded.
     */
    public void discardCard(ObjectCard objectCard) {
        Player player = this.clientStore.getState().player;
        if (player.privateDeck.getContent().contains(objectCard)) {
            ArrayList<Object> parameters = new ArrayList<>();
            StoreAction action = new DiscardAction(objectCard);
            parameters.add(action);
            parameters.add(player.playerToken);
            try {
                RemoteMethodCall remoteMethodCall = this.communicationHandler.newComSession(new RemoteMethodCall("makeAction", parameters));
                this.processRemoteInvocation(remoteMethodCall);
            } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            } catch (IOException e1) {
                //If connection is down
                this.clientStore.dispatchAction(new ClientSetConnectionActiveAction(false));
            }
            boolean isActionServerValidated = this.clientStore.getState().currentReqRespNotification.getActionResult();
            this.clientStore.dispatchAction(new ClientDiscardObjectCardAction(objectCard, isActionServerValidated));
        }
    }




    public void processRemoteInvocation(RemoteMethodCall remoteClientInvocation)
            throws IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, SecurityException {
        String methodName = remoteClientInvocation.getMethodName();
        ArrayList<Object> parameters = remoteClientInvocation
                .getMethodParameters();
        Class<?>[] parametersClasses = new Class[parameters.size()];
        for (int i = 0; i < parameters.size(); i++) {
            parametersClasses[i] = parameters.get(i).getClass();
        }
        this.getClass().getDeclaredMethod(methodName, parametersClasses)
                .invoke(this, parameters.toArray());
    }

    /**
     * @see ClientRemoteServicesInterface#sendToken
     * @throws IOException
     *             signals a com. error
     * @throws RemoteException
     *             signals a com. error
     */
    public void sendToken(PlayerToken token) throws IOException,
            RemoteException {
        client.setToken(token);
    }

    /**
     * @see ClientRemoteServicesInterface#sendAvailableGames
     * @throws IOException
     *             signals a com. error
     * @throws RemoteException
     *             signals a com. error
     */
    public void sendAvailableGames(ArrayList<GamePublicData> availableGames)
            throws IOException, RemoteException {
        client.setAvailableGames(availableGames);
    }

    /**
     * @see ClientRemoteServicesInterface#publishChatMsg
     * @throws IOException
     *             signals a com. error
     * @throws RemoteException
     *             signals a com. error
     */
    public void publishChatMsg(String msg) throws RemoteException {
        client.displayMessage(msg);
    }


    private void syncNotification(RRClientNotification clientNotification){
        client.setNotification(clientNotification);
    }


    public void asyncNotification(PSClientNotification psNotification) {
        client.psNotify(psNotification);
    }
    /**
     * @see ClientRemoteServicesInterface#sendMap
     */
    public void sendMap(String mapName, PlayerToken playerToken) {
        if (mapName.equals("GALILEI")) {
            GalileiGameMapFactory factory = new GalileiGameMapFactory();
            GameMap map = factory.makeMap();
            client.setGameMap(map);
            client.setPrivateDeck(new PrivateDeck());
            if (client.getToken().getPlayerType().equals(PlayerType.ALIEN)) {
                client.setCurrentSector(map.getAlienSector());
            } else {
                client.setCurrentSector(map.getHumanSector());
            }

        } else if (mapName.equals("FERMI")) {
            FermiGameMapFactory factory = new FermiGameMapFactory();
            GameMap map = factory.makeMap();
            client.setGameMap(map);
            client.setPrivateDeck(new PrivateDeck());
            if (client.getToken().getPlayerType().equals(PlayerType.ALIEN)) {
                client.setCurrentSector(map.getAlienSector());
            } else {
                client.setCurrentSector(map.getHumanSector());
            }
        } else if (mapName.equals("GALVANI")) {
            GalvaniGameMapFactory factory = new GalvaniGameMapFactory();
            GameMap map = factory.makeMap();
            client.setGameMap(map);
            client.setPrivateDeck(new PrivateDeck());
            if (client.getToken().getPlayerType().equals(PlayerType.ALIEN)) {
                client.setCurrentSector(map.getAlienSector());
            } else {
                client.setCurrentSector(map.getHumanSector());
            }
        } else {
            throw new IllegalArgumentException("The type of map is undefined");
        }
        if (playerToken.equals(client.getToken())) {
            client.setIsMyTurn(true);
        }
        // START THE GAME
        synchronized (client) {
            client.setGameStarted(true);
            client.notifyAll();
        }
    }

    public void kick(PlayerToken playerToken) {
        if (playerToken.equals(client.getToken())) {
            client.shutdown();
        }
    }

    private void allowTurn() {
        this.client.setIsMyTurn(true);
    }
    /**
     * Makes the client send a chat message to the other players. This action is validated and registered by contacting the game server.
     *
     * @param message The message to be sent.
     */
    public void sendMessage(String message) {
        ArrayList<Object> parameters = new ArrayList<>();
        parameters.add(message);
        parameters.add(this.client.getToken());
        try {
            RemoteMethodCall remoteMethodCall = this.communicationHandler.newComSession(new RemoteMethodCall("publishGlobalMessage", parameters));
            this.processRemoteInvocation(remoteMethodCall);

        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        } catch (IOException e) {
            //If connection is down
            //this.clientStore.dispatchAction(new ClientSetConnectionActiveAction(false));
        }
    }
    /**
     * Makes the client attack a sector at given coordinates. This action is validated and registered by contacting the game server.
     *
     * @param coordinate The coordinates of the sector to be attacked.
     */
    public void attack(Coordinate coordinate) {
        //Player player = this.clientStore.getState().player;
        boolean humanAttack = this.client.getToken().getPlayerType().equals(PlayerType.HUMAN);
        Sector targetSector = this.client.getGameMap().getSectorByCoords(coordinate);
        ArrayList<Object> parameters = new ArrayList<>();
        AttackObjectCard card;
        if (humanAttack) {
            card = new AttackObjectCard(targetSector);
            Action action = new UseObjAction(card);
            parameters.add(action);
            parameters.add(this.client.getToken());

        } else {
            Action action = new MoveAttackAction(targetSector);
            parameters.add(action);
            parameters.add(this.client.getToken());
        }
        try {
            RemoteMethodCall remoteMethodCall = this.communicationHandler.newComSession(new RemoteMethodCall("makeAction", parameters));
            this.processRemoteInvocation(remoteMethodCall);
        } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        } catch (IOException e1) {
            //If connection is down
            //this.clientStore.dispatchAction(new ClientSetConnectionActiveAction(false));
        }
        boolean isActionServerValidated = this.client.getCurrentNotification().getActionResult();
        //this.clientStore.dispatchAction(new ClientMoveToSectorAction(targetSector, isActionServerValidated));

    }
    /**
     * Sets the games that are running of waiting to be run on the game server.
     * This method is executed indirectly using reflection.
     *
     * @param avGames The list of data relative to the games available on the server.
     */
    private void setAvailableGames(ArrayList<GamePublicData> avGames) {
        this.client.setAvailableGames(avGames);
    }

    /**
     * Sets the identification token for the client and subscribes the client to asynchronous
     * notification/method calls by the server.
     * This method is invoked indirectly using reflection.
     *
     * @param playerToken The client's identification token
     */
    private void setPlayerTokenAndSubscribe(PlayerToken playerToken) {
        this.client.setToken(playerToken);
        ArrayList<Object> parameters = new ArrayList<>();
        parameters.add(playerToken);
        try {
            RemoteMethodCall remoteMethodCall = this.communicationHandler.newComSession(
                    new RemoteMethodCall(this.serverMethodsNameProvider.subscribe(),parameters));
            this.processRemoteInvocation(remoteMethodCall);
            if (this.client.getCurrentNotification().getActionResult()){

            }
        } catch (IOException | ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

    }
    /**
     * Requests to the game server a list of available games
     */
    public void getGames() {
        try {
            RemoteMethodCall methodCall = this.communicationHandler.newComSession(new RemoteMethodCall("getGames", new ArrayList<>()));
            this.processRemoteInvocation(methodCall);
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        } catch (IOException e1) {

        }
    }
    /**
     * Requests to the server the start of a created game without waiting for 8 players to join the game.
     */
    public void onDemandGameStart() {
        ArrayList<Object> parameters = new ArrayList<>();
        parameters.add(this.client.getToken());
        try {
            RemoteMethodCall methodCall = this.communicationHandler.newComSession(
                    new RemoteMethodCall(this.serverMethodsNameProvider.onDemandGameStart(), parameters));
            this.processRemoteInvocation(methodCall);
            if (this.client.getCurrentNotification().getActionResult()){
                //Start the game
            }
        } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        } catch (IOException e) {

        }
    }
}
