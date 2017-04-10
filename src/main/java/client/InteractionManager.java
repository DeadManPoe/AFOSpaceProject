package client;

import client_store.ClientStore;
import client_store_actions.*;
import common.*;
import it.polimi.ingsw.cg_19.PlayerState;
import it.polimi.ingsw.cg_19.PlayerType;
import server_store.Player;
import server_store.StoreAction;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by giorgiopea on 25/03/17.
 * A manager that acts as MODEL/CONTROLLER along with the {@link ClientStore}.
 * The gui components of this application signal various user interactions to this manager,
 * and this manager implements the business logic of the application by dispatching actions
 * to the {@link ClientStore}.
 */
public class InteractionManager {

    private static final InteractionManager instance = new InteractionManager();
    private final ClientStore clientStore;
    private final CommunicationHandler communicationHandler;

    public static InteractionManager getInstance() {
        return instance;
    }

    private InteractionManager() {
        this.clientStore = ClientStore.getInstance();
        this.communicationHandler = CommunicationHandler.getInstance();
    }

    /**
     * Handles a synchronous notification that has been produced by the server
     * in response to a client request.
     * This method is invoked indirectly using reflection.
     *
     * @param clientNotification The produced notification
     */
    private void syncNotification(ClientNotification clientNotification) {
        RRClientNotification rrClientNotification = (RRClientNotification) clientNotification;
        this.clientStore.dispatchAction(new ClientSetCurrentReqRespNotificationAction(rrClientNotification));
    }

    /**
     * Handles an asynchronous notification that has been produced by the server.
     * This method is invoked indirectly using reflection.
     *
     * @param psNotification The produced notification
     * @see PubSubHandler
     */
    private void asyncNotification(ClientNotification psNotification) {
        PSClientNotification notification = (PSClientNotification) psNotification;
        Player player = this.clientStore.getState().player;
        if (notification.getEscapedPlayer() != null) {
            if (notification.getEscapedPlayer().equals(player.playerToken)) {
                this.clientStore.dispatchAction(new ClientSetPlayerState(PlayerState.ESCAPED));
            }
        }
        if (notification.getDeadPlayers().contains(player.playerToken)) {
            this.clientStore.dispatchAction(new ClientSetPlayerState(PlayerState.DEAD));
        } else if (notification.getAttackedPlayers().contains(player.playerToken)) {
            this.clientStore.dispatchAction(new ClientUseObjectCard(new DefenseObjectCard(), true));
        }
        if (notification.getHumanWins() || notification.getAlienWins()) {
            this.clientStore.dispatchAction(new ClientSetWinnersAction(notification.getAlienWins(), notification.getHumanWins()));
            this.clientStore.dispatchAction(new ClientRemovePubSubHandlerAction());
        }

    }

    /**
     * Sets the identification token for the client and subscribes the client to asynchronous
     * notification/method calls by the server.
     * This method is invoked indirectly using reflection.
     *
     * @param playerToken The client's identification token
     */
    private void setPlayerTokenAndSubscribe(PlayerToken playerToken) {
        String playerName = this.clientStore.getState().player.name;
        this.clientStore.dispatchAction(new ClientSetPlayerAction(playerName, playerToken));
        ArrayList<Object> parameters = new ArrayList<>();
        parameters.add(playerToken);
        try {
            this.communicationHandler.newComSession(new RemoteMethodCall("subscribe", parameters));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            //If connection is down
            this.clientStore.dispatchAction(new ClientSetConnectionActiveAction(false));
        }
    }

    /**
     * Requests to the server the start of a created game without waiting for 8 players to join the game.
     */
    public void onDemandGameStart() {
        ArrayList<Object> parameters = new ArrayList<>();
        parameters.add(ClientStore.getInstance().getState().player.playerToken);
        try {
            RemoteMethodCall methodCall = this.communicationHandler.newComSession(new RemoteMethodCall("onDemandGameStart", parameters));
            this.processRemoteInvocation(methodCall);

        } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        } catch (IOException e) {
            //If connection is down
            this.clientStore.dispatchAction(new ClientSetConnectionActiveAction(false));
        }
    }

    /**
     * Makes the game associated to this client start. This method is invoked indirectly using reflection.
     *
     * @param mapName The name of the game map.
     */
    private void setMapAndStartGame(String mapName) {
        this.clientStore.dispatchAction(new ClientStartGameAction(mapName));
    }

    /**
     * Requests to the server the creation of a new game with a given map.
     *
     * @param gameMapName The name of the map of the game to be created.
     * @param playerName  The name of the client in the game.
     */
    public void joinNewGame(String gameMapName, String playerName) {
        this.clientStore.dispatchAction(new ClientSetPlayerAction(playerName, null));
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
            this.clientStore.dispatchAction(new ClientSetConnectionActiveAction(false));
        }
    }

    /**
     * Requests to the server the join of a game.
     *
     * @param gameId     The id of the game to join.
     * @param playerName The name of the client in the game.
     */
    public void joinGame(Integer gameId, String playerName) {
        this.clientStore.dispatchAction(new ClientSetPlayerAction(playerName, null));
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
            this.clientStore.dispatchAction(new ClientSetConnectionActiveAction(false));
        }
    }

    /**
     * Publishes a message in the chat window.
     * This method is invoked indirectly using reflection.
     *
     * @param msg The message to be published.
     */
    private void publishChatMsg(String msg) {
        this.clientStore.dispatchAction(new ClientSetCurrentChatMessage(msg));
    }


    /**
     * Moves the client to the sector at the given coordinates and executes the other logic related to this action.
     * This action is validated and registered by contacting the game server.
     *
     * @param coordinate The coordinates of the sector to move to
     */
    public void moveToSector(Coordinate coordinate) {
        Sector targetSector = this.clientStore.getState().gameMap.getSectorByCoords(coordinate);
        RRClientNotification currentReqResponseNotification = this.clientStore.getState().currentReqRespNotification;
        List<Card> drawnCards = currentReqResponseNotification.getDrawnCards();
        this.clientStore.dispatchAction(new ClientAskAttackAction(false));
        ArrayList<Object> parameters = new ArrayList<>();
        StoreAction action = new MoveAction(targetSector);
        parameters.add(action);
        parameters.add(this.clientStore.getState().player.playerToken);
        try {
            RemoteMethodCall methodCall = this.communicationHandler.newComSession(new RemoteMethodCall("makeAction", parameters));
            this.processRemoteInvocation(methodCall);
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        } catch (IOException e1) {
            //If connection is down
            this.clientStore.dispatchAction(new ClientSetConnectionActiveAction(false));
        }
        boolean isActionServerValidated = currentReqResponseNotification.getActionResult();
        this.clientStore.dispatchAction(new ClientMoveToSectorAction(targetSector, isActionServerValidated));
        if (isActionServerValidated) {
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
        }

    }

    /**
     * Signals that there's the possibility, by the client, to start the game without waiting for 8 players to join.
     * This method is invoked indirectly using reflection.
     */
    private void signalStartableGame() {
        this.clientStore.dispatchAction(new ClientStartableGameAction());
    }

    /**
     * Makes the client use an object card. This action is validated and registered by contacting the game server.
     *
     * @param objectCard The object card to be used
     */
    public void useObjCard(ObjectCard objectCard) {
        Player player = this.clientStore.getState().player;
        if (player.privateDeck.getContent().contains(objectCard)) {
            if (objectCard instanceof LightsObjectCard) {
                this.clientStore.dispatchAction(new ClientAskSectorToLightAction(true));
            } else if (objectCard instanceof AttackObjectCard) {
                this.clientStore.dispatchAction(new ClientAskAttackAction(true));
            } else {
                ArrayList<Object> parameters = new ArrayList<>();
                StoreAction action = new UseObjAction(objectCard);
                parameters.add(action);
                parameters.add(player.playerToken);
                RemoteMethodCall methodCall = null;
                try {
                    methodCall = this.communicationHandler.newComSession(new RemoteMethodCall("makeAction", parameters));
                    this.processRemoteInvocation(methodCall);
                } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                } catch (IOException e1) {
                    //If connection is down
                    this.clientStore.dispatchAction(new ClientSetConnectionActiveAction(false));
                }
                boolean isActionServerValidated = this.clientStore.getState().currentReqRespNotification.getActionResult();
                this.clientStore.dispatchAction(new ClientUseObjectCard(objectCard, isActionServerValidated));
                if (isActionServerValidated) {
                    if (objectCard instanceof TeleportObjectCard) {
                        this.clientStore.dispatchAction(new ClientTeleportToStartingSectorAction());
                    } else if (objectCard instanceof SuppressorObjectCard) {
                        this.clientStore.dispatchAction(new ClientSuppressAction(true));
                    } else if (objectCard instanceof AdrenalineObjectCard) {
                        this.clientStore.dispatchAction(new ClientAdrenlineAction());
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
        this.clientStore.dispatchAction(new ClientStartTurnAction());
    }

    /**
     * Makes the client indicate a sector to the other players of the game as a consequence of
     * a global noise sector card. This action is validated and registered by contacting the game server.
     *
     * @param coordinate The coordinates of the sector to be indicated
     * @param hasObject  If there's an object card associated with the global noise sector card(irrelevant)
     */
    public void globalNoise(Coordinate coordinate, boolean hasObject) {
        Sector targetSector = this.clientStore.getState().gameMap.getSectorByCoords(coordinate);
        if (targetSector != null) {
            SectorCard globalNoiseCard = new GlobalNoiseSectorCard(hasObject,
                    targetSector);
            ArrayList<Object> parameters = new ArrayList<>();
            StoreAction action = new UseSectorCardAction(globalNoiseCard);
            parameters.add(action);
            parameters.add(this.clientStore.getState().player.playerToken);
            try {
                RemoteMethodCall methodCall = this.communicationHandler.newComSession(new RemoteMethodCall("makeAction", parameters));
                this.processRemoteInvocation(methodCall);
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e1) {
                //If connection is down
                this.clientStore.dispatchAction(new ClientSetConnectionActiveAction(false));
            }
            boolean isActionServerValidated = this.clientStore.getState().currentReqRespNotification.getActionResult();
            this.clientStore.dispatchAction(new ClientSetDrawnSectorObjectCard(null, null, isActionServerValidated));
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
            this.communicationHandler.newComSession(new RemoteMethodCall("makeAction", parameters));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            //If connection is down
            this.clientStore.dispatchAction(new ClientSetConnectionActiveAction(false));
        }
        boolean isActionServerValidated = this.clientStore.getState().currentReqRespNotification.getActionResult();
        this.clientStore.dispatchAction(new ClientEndTurnAction(isActionServerValidated));
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

    /**
     * Makes the client send a chat message to the other players. This action is validated and registered by contacting the game server.
     *
     * @param message The message to be sent.
     */
    public void sendMessage(String message) {
        ArrayList<Object> parameters = new ArrayList<>();
        parameters.add(message);
        parameters.add(this.clientStore.getState().player.playerToken);
        try {
            RemoteMethodCall remoteMethodCall = this.communicationHandler.newComSession(new RemoteMethodCall("publishGlobalMessage", parameters));
            this.processRemoteInvocation(remoteMethodCall);

        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        } catch (IOException e) {
            //If connection is down
            this.clientStore.dispatchAction(new ClientSetConnectionActiveAction(false));
        }
    }

    /**
     * Makes the client attack a sector at given coordinates. This action is validated and registered by contacting the game server.
     *
     * @param coordinate The coordinates of the sector to be attacked.
     */
    public void attack(Coordinate coordinate) {
        Player player = this.clientStore.getState().player;
        boolean humanAttack = player.playerToken.playerType.equals(PlayerType.HUMAN);
        Sector targetSector = this.clientStore.getState().gameMap.getSectorByCoords(coordinate);
        ArrayList<Object> parameters = new ArrayList<>();
        AttackObjectCard card;
        if (humanAttack) {
            card = new AttackObjectCard(targetSector);
            StoreAction action = new UseObjAction(card);
            parameters.add(action);
            parameters.add(player.playerToken);

        } else {
            StoreAction action = new MoveAttackAction(targetSector);
            parameters.add(action);
            parameters.add(player.playerToken);
        }
        try {
            RemoteMethodCall remoteMethodCall = this.communicationHandler.newComSession(new RemoteMethodCall("makeAction", parameters));
            this.processRemoteInvocation(remoteMethodCall);
        } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        } catch (IOException e1) {
            //If connection is down
            this.clientStore.dispatchAction(new ClientSetConnectionActiveAction(false));
        }
        boolean isActionServerValidated = this.clientStore.getState().currentReqRespNotification.getActionResult();
        this.clientStore.dispatchAction(new ClientMoveToSectorAction(targetSector, isActionServerValidated));

    }

    /**
     * Sets the games that are running of waiting to be run on the game server.
     * This method is executed indirectly using reflection.
     *
     * @param avGames The list of data relative to the games available on the server.
     */
    private void setAvailableGames(ArrayList<GamePublicData> avGames) {
        this.clientStore.dispatchAction(new ClientSetAvailableGamesAction(avGames));
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
            //If connection is down
            this.clientStore.dispatchAction(new ClientSetConnectionActiveAction(false));
        }
    }

    /**
     * Invokes via reflection a method with a name and arguments described in the given
     * {@link RemoteMethodCall} object.
     *
     * @param remoteClientInvocation An object that describes the method to be invoked on the client along with its
     *                               arguments.
     * @throws IllegalAccessException    reflection exception
     * @throws IllegalArgumentException  reflection exception
     * @throws InvocationTargetException reflection exception
     * @throws NoSuchMethodException     reflection exception
     * @throws SecurityException         reflection exception
     */
    public void processRemoteInvocation(RemoteMethodCall remoteClientInvocation)
            throws IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, SecurityException {
        if (remoteClientInvocation != null) {
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

    }

}
