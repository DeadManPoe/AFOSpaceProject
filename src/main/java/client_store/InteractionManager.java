package client_store;

import client_gui.GuiManager;
import client_store_actions.*;
import common.*;
import it.polimi.ingsw.cg_19.GameMap;
import it.polimi.ingsw.cg_19.PlayerType;
import server_store.Player;
import server_store.StoreAction;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by giorgiopea on 25/03/17.
 */
public class InteractionManager implements Observer {

    private static final InteractionManager instance = new InteractionManager();
    //Ref to the client store
    private final ClientStore clientStore;
    //Ref to the communication manager
    private final CommunicationManager communicationManager;
    private final GuiManager guiManager;

    public static InteractionManager getInstance() {
        return instance;
    }

    private InteractionManager() {
        this.clientStore = ClientStore.getInstance();
        this.communicationManager = CommunicationManager.getInstance();
        this.guiManager = GuiManager.getInstance();
    }

    /**
     * Manages the answer to a request to the server. This method just updates the app's state
     * with the received answer, and it does so by dispatching a proper action.
     *
     * @param clientNotification The answer to a request to the server
     */
    private void sendNotification(ClientNotification clientNotification) {
        RRClientNotification castedClientNotification = (RRClientNotification) clientNotification;
        this.clientStore.dispatchAction(new ClientSetCurrentReqRespNotificationAction(castedClientNotification));
    }
    /**
     * Manages asynchronous server notification in the context of a publisher subscriber policy.
     * This method just updates the app's state with the received notification,
     * and it does so by dispatching a proper action.
     *
     * @param clientNotification The asynchronous server notification to be managed
     */
    private void sendPubNotification(ClientNotification clientNotification) {
        PSClientNotification castedClientNotification = (PSClientNotification) clientNotification;
        this.clientStore.dispatchAction(new ClientSetCurrentPubSubNotificationAction(castedClientNotification));
    }

    /**
     * Sets the client's player token.
     * @param playerToken The player token to be assigned to the client
     */
    private void setPlayerToken(PlayerToken playerToken) {
        this.clientStore.dispatchAction(new ClientSetPlayerTokenAction(playerToken));
        this.subscribe();
    }

    /**
     * Subscribes the client to asynchronous notifications related to the game he has joined
     */
    private void subscribe(){
        ArrayList<Object> parameters = new ArrayList<>();
        parameters.add(this.clientStore.getState().player.playerToken);
        try {
            RemoteMethodCall remoteMethodCall = this.communicationManager.newComSession(new RemoteMethodCall("subscribe", parameters));
            this.processRemoteInvocation(remoteMethodCall);
        } catch (IOException | ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    /**
     * Requests the start of the game the client has created skipping the natural waiting
     * of 8 players to start the game
     */
    public void onDemandGameStart() {
        ArrayList<Object> parameters = new ArrayList<>();
        parameters.add(ClientStore.getInstance().getState().player.playerToken);
        try {
            RemoteMethodCall methodCall = this.communicationManager.newComSession(new RemoteMethodCall("onDemandGameStart", parameters));
            this.processRemoteInvocation(methodCall);

        } catch (IOException | ClassNotFoundException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets the game map and starts the game associated with the client
     * @param mapName The name of the map to be used for the game
     */
    public void setMapAndStartGame(String mapName) {
        this.clientStore.dispatchAction(new ClientSetGameMapAction(mapName));
        this.clientStore.dispatchAction(new ClientStartGameAction());
        Player player = this.clientStore.getState().player;
        GameMap gameMap = this.clientStore.getState().gameMap;
        boolean isMyTurn = this.clientStore.getState().isMyTurn;
        this.guiManager.startGame(player.name,player.playerType,gameMap.getName(),isMyTurn, player.currentSector.getCoordinate());
    }

    /**
     * Requests the creation and join of a game
     * @param gameMapName The name of the map to be used for the game
     * @param playerName The name to be used in game by the client
     */
    public void joinNewGame(String gameMapName, String playerName) {
        this.clientStore.dispatchAction(new ClientInitPlayerAction(playerName));
        ArrayList<Object> parameters = new ArrayList<>();
        parameters.add(gameMapName);
        parameters.add(playerName);
        try {
            RemoteMethodCall methodCall = this.communicationManager.newComSession(new RemoteMethodCall("joinNewGame", parameters));
            this.processRemoteInvocation(methodCall);
        } catch (IOException | ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    /**
     * Requests the join of a game
     * @param gameId The identifier of the game to join
     * @param playerName The name to be used in game by the client
     */
    public void joinGame(Integer gameId, String playerName) {
        this.clientStore.dispatchAction(new ClientInitPlayerAction(playerName));
        ArrayList<Object> parameters = new ArrayList<>();
        parameters.add(gameId);
        parameters.add(playerName);
        try {
            RemoteMethodCall methodCall = this.communicationManager.newComSession(new RemoteMethodCall("joinGame", parameters));
            this.processRemoteInvocation(methodCall);
        } catch (IOException | ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets a new chat message for the game the client has joined
     * @param msg The chat message to be set
     */
    public void publishChatMsg(String msg) {
        ClientStore.getInstance().dispatchAction(new ClientSetCurrentMessage(msg));
    }
    /**
     * Requests and manages a move action for the game the client has joined
     * @param coordinate The coordinates of the sector to move to
     */
    public void move(Coordinate coordinate) {
        Sector targetSector = this.clientStore.getState().gameMap.getSectorByCoords(coordinate);
        if (targetSector != null) {
            //If I make a non legal attack and instead of redoing the attack I opt for a move action,
            // I need to set the app's state 'askAttack' prop to false
            this.clientStore.dispatchAction(new ClientAskAttackAction(false));
            ArrayList<Object> parameters = new ArrayList<Object>();
            StoreAction action = new MoveAction(targetSector);
            parameters.add(action);
            parameters.add(this.clientStore.getState().player.playerToken);
            try {
                RemoteMethodCall methodCall = this.communicationManager.newComSession(new RemoteMethodCall("makeAction", parameters));
                this.processRemoteInvocation(methodCall);
                if (this.clientStore.getState().currentReqRespNotification.getActionResult()) {
                    //If the move action has been accepted by the server, i need to make it happen on the client
                    this.clientStore.dispatchAction(new ClientMoveAction(targetSector));
                }
            } catch (IOException | ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }

        } else {
            throw new IllegalArgumentException(
                    "The sector you have indicated does not exists, please try again");
        }
    }

    /**
     * Signals that the game the game has created and joined can be started without
     * waiting for the presence of 8 players
     */
    private void signalStartableGame() {
        this.clientStore.dispatchAction(new ClientStartableGameAction());
    }

    /**
     * Requests and manages the use of an object card in the game the client has joined
     * @param objectCardIndex The index of the card to use in the client's private deck of object cards
     */
    public void useObjCard(int objectCardIndex){
        int cardsAmount = this.clientStore.getState().player.privateDeck.getSize();
        if (objectCardIndex <= cardsAmount && objectCardIndex >= 0) {
            ObjectCard objectCard = this.clientStore.getState().player.privateDeck.getCard(
                    objectCardIndex);
            if (objectCard instanceof LightsObjectCard) {
                this.clientStore.dispatchAction(new ClientAskLightsAction(true));
            } else if (objectCard instanceof AttackObjectCard) {
                this.clientStore.dispatchAction(new ClientAskAttackAction(true));
            } else {
                ArrayList<Object> parameters = new ArrayList<>();
                StoreAction action = new UseObjAction(objectCard);
                parameters.add(action);
                parameters.add(this.clientStore.getState().player.playerToken);
                try {
                    RemoteMethodCall methodCall = this.communicationManager.newComSession(new RemoteMethodCall("makeAction", parameters));
                    this.processRemoteInvocation(methodCall);
                    if (this.clientStore.getState().currentReqRespNotification.getActionResult()) {
                        this.clientStore.dispatchAction(new ClientRemoveObjCardAction(objectCard));
                        if (objectCard instanceof TeleportObjectCard) {
                            this.clientStore.dispatchAction(new ClientTeleportAction());
                        } else if (objectCard instanceof SuppressorObjectCard) {
                            this.clientStore.dispatchAction(new ClientSuppressAction(true));
                        }
                    }
                } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | ClassNotFoundException | IOException e) {
                    e.printStackTrace();
                }

            }
        } else {
            throw new IllegalArgumentException(
                    "Undifined card, please try again");
        }
    }

    /**
     * Allows the client's turn in the game he has joined
     */
    private void allowTurn() {
        this.clientStore.dispatchAction(new ClientAllowTurnAction());
    }

    /**
     * Requests and manages the execution of a global noise action in the game the client has joined
     * @param coordinate The coordinates of the sector associated with the action
     * @param hasObject If the action implies drawing an object card
     */
    public void globalNoise(Coordinate coordinate, boolean hasObject) {
        Sector targetSector = this.clientStore.getState().gameMap.getSectorByCoords(coordinate);
        if (targetSector != null) {
            SectorCard globalNoiseCard = new GlobalNoiseSectorCard(hasObject,
                    targetSector);
            ArrayList<Object> parameters = new ArrayList<Object>();
            StoreAction action = new UseSectorCardAction(globalNoiseCard);
            parameters.add(action);
            parameters.add(this.clientStore.getState().player.playerToken);
            try {
                RemoteMethodCall methodCall = this.communicationManager.newComSession(new RemoteMethodCall("makeAction", parameters));
                this.processRemoteInvocation(methodCall);
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | ClassNotFoundException | IOException e) {
                e.printStackTrace();
            }
        } else {
            throw new IllegalArgumentException(
                    "The sector you have indicated does not exists, please try again");
        }
    }

    /**
     * Requests and manages the execution of a lights object card action in the game the client has joined
     * @param coordinate The coordinates of the sector associated with the action
     */
    public void lights(Coordinate coordinate) {
        Sector targetSector = this.clientStore.getState().gameMap.getSectorByCoords(coordinate);
        if (targetSector != null) {
            ObjectCard lightsCard = new LightsObjectCard(targetSector);
            ArrayList<Object> parameters = new ArrayList<Object>();
            StoreAction action = new UseObjAction(lightsCard);
            parameters.add(action);
            parameters.add(this.clientStore.getState().player.playerToken);
            try {
                RemoteMethodCall remoteMethodCall = this.communicationManager.newComSession(new RemoteMethodCall("makeAction", parameters));
                this.processRemoteInvocation(remoteMethodCall);
                this.clientStore.dispatchAction(new ClientAskLightsAction(false));
                this.clientStore.dispatchAction(new ClientRemoveObjCardAction(lightsCard));
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | ClassNotFoundException | IOException e) {
                e.printStackTrace();
            }

        } else {
            throw new IllegalArgumentException(
                    "Undefined sector, please try again");
        }

    }
    /**
     * Requests and manages ending the client's turn in the game he has joined
     */
    public void endTurn() {
        ArrayList<Object> parameters = new ArrayList<Object>();
        StoreAction action = new EndTurnAction();
        parameters.add(action);
        parameters.add(this.clientStore.getState().player.playerToken);
        try {
            this.communicationManager.newComSession(new RemoteMethodCall("makeAction", parameters));
            if (this.clientStore.getState().currentReqRespNotification.getActionResult()) {
                this.clientStore.dispatchAction(new ClientEndTurnAction());
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Requests and manages the execution of a discard an object card action in the game the client has joined
     * @param objectCardIndex The index of the object card to discarded from the client's private deck
     */
    public void discardCard(int objectCardIndex) {
        int cardsAmount = this.clientStore.getState().player.privateDeck.getSize();
        if (objectCardIndex <= cardsAmount && objectCardIndex >= 0) {
            ObjectCard objectCardToDiscard = this.clientStore.getState().player.privateDeck.getCard(
                    objectCardIndex - 1);
            ArrayList<Object> parameters = new ArrayList<Object>();
            StoreAction action = new DiscardAction(objectCardToDiscard);
            parameters.add(action);
            parameters.add(this.clientStore.getState().player.playerToken);
            try {
                RemoteMethodCall remoteMethodCall = this.communicationManager.newComSession(new RemoteMethodCall("makeAction", parameters));
                this.processRemoteInvocation(remoteMethodCall);
                if (this.clientStore.getState().currentReqRespNotification.getActionResult()) {
                    this.clientStore.dispatchAction(new ClientRemoveObjCardAction(objectCardToDiscard));
                }
            } catch (IOException | ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }



        } else {
            throw new IllegalArgumentException(
                    "Undifined card, please try again");
        }

    }

    /**
     * Requests and manages the publishing of a message in the chat of the game the client has joined
     * @param message The message to be published
     */
    public void sendMessage(String message) {
        ArrayList<Object> parameters = new ArrayList<Object>();
        parameters.add(message);
        parameters.add(this.clientStore.getState().player.playerToken);
        try {
            RemoteMethodCall remoteMethodCall = this.communicationManager.newComSession(new RemoteMethodCall("publishGlobalMessage", parameters));
            this.processRemoteInvocation(remoteMethodCall);

        } catch (IOException | ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    /**
     * Requests and manages an attack action in the game the client has joined
     * @param coordinate The coordinates of the sector to be attacked
     */
    public void attack(Coordinate coordinate) {
        boolean humanAttack = ClientStore.getInstance().getState().player.playerType.equals(PlayerType.HUMAN);
        Sector targetSector = this.clientStore.getState().gameMap.getSectorByCoords(coordinate);
        if (targetSector != null) {
            ClientStore.getInstance().dispatchAction(new ClientAskAttackAction(true));
            ArrayList<Object> parameters = new ArrayList<Object>();
            if (humanAttack) {
                AttackObjectCard card = new AttackObjectCard(targetSector);
                StoreAction action = new UseObjAction(card);
                parameters.add(action);
                parameters.add(this.clientStore.getState().player.playerToken);
                try {
                    RemoteMethodCall remoteMethodCall = this.communicationManager.newComSession(new RemoteMethodCall("makeAction", parameters));
                    this.processRemoteInvocation(remoteMethodCall);
                } catch (IOException | ClassNotFoundException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            } else {
                StoreAction action = new MoveAttackAction(targetSector);
                parameters.add(action);
                parameters.add(this.clientStore.getState().player.playerToken);
                try {
                    RemoteMethodCall remoteMethodCall = this.communicationManager.newComSession(new RemoteMethodCall("makeAction", parameters));
                    this.processRemoteInvocation(remoteMethodCall);
                    if (this.clientStore.getState().currentReqRespNotification.getActionResult()) {
                        this.clientStore.dispatchAction(new ClientMoveAction(targetSector));
                        this.clientStore.dispatchAction(new ClientAskAttackAction(false));
                    }
                } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | ClassNotFoundException | IOException e) {
                    e.printStackTrace();
                }
            }


        } else {
            throw new IllegalArgumentException(
                    "The sector you have indicated does not exists, please try again");
        }

    }

    /**
     * Requests the list of games running on the server
     */
    public void getGames(){
        try {
            RemoteMethodCall methodCall = this.communicationManager.newComSession(new RemoteMethodCall("getGames", new ArrayList<Object>()));
            this.processRemoteInvocation(methodCall);
            RRClientNotification currentNotification = this.clientStore.getState().currentReqRespNotification;
            if (currentNotification.getActionResult()){
                this.clientStore.dispatchAction(new ClientSetAvGamesAction(currentNotification.getAvailableGames()));
            }
        } catch (IOException | ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }


    }
    private void processRemoteInvocation(RemoteMethodCall remoteClientInvocation)
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

    @Override
    public void update(Observable o, Object arg) {

    }
}
