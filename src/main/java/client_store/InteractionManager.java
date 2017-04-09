package client_store;

import client_store_actions.*;
import common.*;
import it.polimi.ingsw.cg_19.PlayerState;
import it.polimi.ingsw.cg_19.PlayerType;
import server_store.Player;
import server_store.StoreAction;
import store_actions.CommunicationRemovePubSubHandlerAction;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by giorgiopea on 25/03/17.
 *
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

    private void sendNotification(ClientNotification clientNotification)
            throws IOException {
        RRClientNotification rrClientNotification = (RRClientNotification) clientNotification;
        this.clientStore.dispatchAction(new ClientSetCurrentReqRespNotificationAction(rrClientNotification));
    }

    private void sendPubNotification(ClientNotification psNotification) {
        PSClientNotification notification = (PSClientNotification) psNotification;
        Player player = this.clientStore.getState().player;
        if (notification.getEscapedPlayer() != null){
            if (notification.getEscapedPlayer().equals(player.playerToken)){
                this.clientStore.dispatchAction(new ClientSetPlayerState(PlayerState.ESCAPED));
            }
        }
        if (notification.getDeadPlayers().contains(player.playerToken)){
            this.clientStore.dispatchAction(new ClientSetPlayerState(PlayerState.DEAD));
        }
        else if (notification.getAttackedPlayers().contains(player.playerToken)){
            this.clientStore.dispatchAction(new ClientUseObjectCard(new DefenseObjectCard(),true));
        }
        if (notification.getHumanWins() || notification.getAlienWins()){
            this.clientStore.dispatchAction(new ClientSetWinnersAction(notification.getAlienWins(),notification.getHumanWins()));
            this.clientStore.dispatchAction(new ClientRemovePubSubHandlerAction());
        }

    }


    private void setPlayerToken(PlayerToken playerToken) {
        String playerName = this.clientStore.getState().player.name;
        this.clientStore.dispatchAction(new ClientSetPlayerAction(playerName, playerToken));
        ArrayList<Object> parameters = new ArrayList<>();
        parameters.add(playerToken);
        try {
            this.communicationHandler.newComSession(new RemoteMethodCall("subscribe", parameters));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            this.clientStore.dispatchAction(new ClientSetConnectionActiveAction(false));
        }
    }

    public void onDemandGameStart() {
        ArrayList<Object> parameters = new ArrayList<>();
        parameters.add(ClientStore.getInstance().getState().player.playerToken);
        try {
            RemoteMethodCall methodCall = this.communicationHandler.newComSession(new RemoteMethodCall("onDemandGameStart", parameters));
            this.processRemoteInvocation(methodCall);

        } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
        catch (IOException e){
            this.clientStore.dispatchAction(new ClientSetConnectionActiveAction(false));
        }
    }

    private void setMapAndStartGame(String mapName) {
        this.clientStore.dispatchAction(new ClientStartGameAction(mapName));
    }

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
            this.clientStore.dispatchAction(new ClientSetConnectionActiveAction(false));
        }
    }

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
            this.clientStore.dispatchAction(new ClientSetConnectionActiveAction(false));
        }
    }

    public void publishChatMsg(String msg) {
        this.clientStore.dispatchAction(new ClientSetCurrentChatMessage(msg));
    }


    /**
     * Moves the client to the sector at the given coordinates and executes the other logic related to this action.
     * This action is validated by contacting the game server
     * @param coordinate The coordinates of the sector to move to
     */
    public void moveToSector(Coordinate coordinate) {
        Sector targetSector = this.clientStore.getState().gameMap.getSectorByCoords(coordinate);
        RRClientNotification currentReqResponseNotification = this.clientStore.getState().currentReqRespNotification;
        boolean isActionServerValidated = currentReqResponseNotification.getActionResult();
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
            this.clientStore.dispatchAction(new ClientSetConnectionActiveAction(false));
        }
        this.clientStore.dispatchAction(new ClientMoveToSectorAction(targetSector, isActionServerValidated));
        if (isActionServerValidated){
            if (drawnCards.size() == 1){
                this.clientStore.dispatchAction(
                        new ClientSetDrawnSectorObjectCard(
                                (SectorCard) drawnCards.get(0),null,true));
            }
            else if (drawnCards.size() == 2){
                this.clientStore.dispatchAction(
                        new ClientSetDrawnSectorObjectCard(
                                (SectorCard) drawnCards.get(0),
                                (ObjectCard) drawnCards.get(1),true));
            }
        }

    }

    public void signalStartableGame() {
        this.clientStore.dispatchAction(new ClientStartableGameAction());
    }

    /**
     * Makes the client use an object card. This action is validated by contacting the game server
     * @param objectCard The object card to be used
     */
    public void useObjCard(ObjectCard objectCard){
        Player player = this.clientStore.getState().player;
        if (player.privateDeck.getContent().contains(objectCard)){
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
                }
                catch ( IOException e1){
                    this.clientStore.dispatchAction(new ClientSetConnectionActiveAction(false));
                }
                boolean isActionServerValidated = this.clientStore.getState().currentReqRespNotification.getActionResult();
                this.clientStore.dispatchAction(new ClientUseObjectCard(objectCard,isActionServerValidated));
                if (isActionServerValidated){
                    if (objectCard instanceof TeleportObjectCard) {
                        this.clientStore.dispatchAction(new ClientTeleportToStartingSectorAction());
                    } else if (objectCard instanceof SuppressorObjectCard) {
                        this.clientStore.dispatchAction(new ClientSuppressAction(true));
                    }
                    else if (objectCard instanceof AdrenalineObjectCard){
                        this.clientStore.dispatchAction(new ClientAdrenlineAction());
                    }
                }

            }
        }



    }

    public void allowTurn() {
        this.clientStore.dispatchAction(new ClientStartTurnAction());
    }

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
                this.clientStore.dispatchAction(new ClientSetConnectionActiveAction(false));
            }
            boolean isActionServerValidated = this.clientStore.getState().currentReqRespNotification.getActionResult();
            this.clientStore.dispatchAction(new ClientSetDrawnSectorObjectCard(null,null, isActionServerValidated));
        } else {
            throw new IllegalArgumentException(
                    "The sector you have indicated does not exists, please try again");
        }
    }

    public void lights(Coordinate coordinate) {
        Sector targetSector = this.clientStore.getState().gameMap.getSectorByCoords(coordinate);
        boolean isActionServerValidated = this.clientStore.getState().currentReqRespNotification.getActionResult();
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
            }
            catch (IOException e1) {
                this.clientStore.dispatchAction(new ClientSetConnectionActiveAction(false));
            }
            this.clientStore.dispatchAction(new ClientUseObjectCard(lightsCard,isActionServerValidated));
            if (isActionServerValidated){
                this.clientStore.dispatchAction(new ClientAskSectorToLightAction(false));
            }
        } else {
            throw new IllegalArgumentException(
                    "Undefined sector, please try again");
        }

    }

    public void endTurn() {
        ArrayList<Object> parameters = new ArrayList<>();
        StoreAction action = new EndTurnAction();
        boolean isActionServerValidated = this.clientStore.getState().currentReqRespNotification.getActionResult();
        parameters.add(action);
        parameters.add(this.clientStore.getState().player.playerToken);
        try {
            this.communicationHandler.newComSession(new RemoteMethodCall("makeAction", parameters));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e){
            this.clientStore.dispatchAction(new ClientSetConnectionActiveAction(false));
        }
        this.clientStore.dispatchAction(new ClientEndTurnAction(isActionServerValidated));
    }

    public void discardCard(ObjectCard objectCard) {
        Player player = this.clientStore.getState().player;
        boolean isActionServerValidated = this.clientStore.getState().currentReqRespNotification.getActionResult();
        if (player.privateDeck.getContent().contains(objectCard)){
            ArrayList<Object> parameters = new ArrayList<>();
            StoreAction action = new DiscardAction(objectCard);
            parameters.add(action);
            parameters.add(player.playerToken);
            try {
                RemoteMethodCall remoteMethodCall = this.communicationHandler.newComSession(new RemoteMethodCall("makeAction", parameters));
                this.processRemoteInvocation(remoteMethodCall);
            } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
            catch (IOException e1){
                this.clientStore.dispatchAction(new ClientSetConnectionActiveAction(false));
            }
            this.clientStore.dispatchAction(new ClientDiscardObjectCardAction(objectCard,isActionServerValidated));
        }
    }

    public void sendMessage(String message) {
        ArrayList<Object> parameters = new ArrayList<>();
        parameters.add(message);
        parameters.add(this.clientStore.getState().player.playerToken);
        try {
            RemoteMethodCall remoteMethodCall = this.communicationHandler.newComSession(new RemoteMethodCall("publishGlobalMessage", parameters));
            this.processRemoteInvocation(remoteMethodCall);

        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        catch (IOException e){
            this.clientStore.dispatchAction(new ClientSetConnectionActiveAction(false));
        }
    }

    public void attack(Coordinate coordinate) {
        Player player = this.clientStore.getState().player;
        boolean humanAttack = player.playerToken.playerType.equals(PlayerType.HUMAN);
        boolean isActionServerValidated = this.clientStore.getState().currentReqRespNotification.getActionResult();
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
            this.clientStore.dispatchAction(new ClientSetConnectionActiveAction(false));
        }
        this.clientStore.dispatchAction(new ClientMoveToSectorAction(targetSector,isActionServerValidated));

    }

    private void setAvailableGames(ArrayList<GamePublicData> avGames) {
        this.clientStore.dispatchAction(new ClientSetAvailableGamesAction(avGames));
    }

    public void getGames() {
        try {
            RemoteMethodCall methodCall = this.communicationHandler.newComSession(new RemoteMethodCall("getGames", new ArrayList<>()));
            this.processRemoteInvocation(methodCall);
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        } catch (IOException e1) {
            this.clientStore.dispatchAction(new ClientSetConnectionActiveAction(false));
        }
    }

    public void processRemoteInvocation(RemoteMethodCall remoteClientInvocation)
            throws IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, SecurityException {
        if (remoteClientInvocation != null) {
            String methodName = remoteClientInvocation.getMethodName();
            ArrayList<Object> parameters = remoteClientInvocation
                    .getMethodParameters();
            Class<?>[] parametersClasses = new Class[parameters.size()];
            for (int i = 0; i < parameters.size(); i++) {
                if (parameters.get(i).getClass().getName().contains("action")
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

    }

}
