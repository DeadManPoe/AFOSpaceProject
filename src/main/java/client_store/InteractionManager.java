package client_store;

import client.ClientRemoteServicesInterface;
import client_store_actions.*;
import common.*;
import it.polimi.ingsw.cg_19.PlayerType;
import server_store.StoreAction;

import javax.swing.*;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
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

    private InteractionManager(){
        this.clientStore = ClientStore.getInstance();
        this.communicationHandler = CommunicationHandler.getInstance();
    }
    public void sendNotification(ClientNotification clientNotification)
            throws RemoteException, IOException {
        RRClientNotification rrClientNotification = (RRClientNotification) clientNotification;
        this.clientStore.dispatchAction(new ClientSetCurrentReqRespNotificationAction(rrClientNotification));
    }

    public void sendPubNotification(ClientNotification psNotification) {
        PSClientNotification notification = (PSClientNotification) psNotification;
        this.clientStore.dispatchAction(new ClientSetCurrentPubSubNotificationAction(notification));
    }



    public void setPlayerToken(PlayerToken token){
        this.clientStore.dispatchAction(new ClientSetPlayerTokenAction(token));
        ArrayList<Object> parameters = new ArrayList<>();
        parameters.add(this.clientStore.getState().player.playerToken);
        try {
            this.communicationHandler.newComSession(new RemoteMethodCall("subscribe",parameters));
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void setMapAndStartGame(String mapName){
        this.clientStore.dispatchAction(new ClientSetGameMapAction(mapName));
        this.clientStore.dispatchAction(new ClientStartGameAction());
    }
    public void joinNewGame(String gameMapName, String playerName){
        this.clientStore.dispatchAction(new ClientInitPlayerAction(playerName));
        ArrayList<Object> parameters = new ArrayList<>();
        parameters.add(gameMapName);
        parameters.add(playerName);
        try {
            RemoteMethodCall methodCall = this.communicationHandler.newComSession(new RemoteMethodCall("joinNewGame",parameters));
            this.processRemoteInvocation(methodCall);
        } catch (IOException | ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }
    public void joinGame(Integer gameId, String playerName){
        this.clientStore.dispatchAction(new ClientInitPlayerAction(playerName));
        ArrayList<Object> parameters = new ArrayList<>();
        parameters.add(gameId);
        parameters.add(playerName);
        try {
            RemoteMethodCall methodCall = this.communicationHandler.newComSession(new RemoteMethodCall("joinGame",parameters));
            this.processRemoteInvocation(methodCall);
        } catch (IOException | ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }
    public void publishChatMsg(String msg){
        ClientStore.getInstance().dispatchAction(new ClientSetCurrentMessage(msg));
    }
    public void denyTurn(){
        ClientStore.getInstance().dispatchAction(new ClientDenyTurnAction());
    }

    public void move(Coordinate coordinate) {
        Sector targetSector = this.clientStore.getState().gameMap.getSectorByCoords(coordinate);
        if (targetSector != null) {
            ArrayList<Object> parameters = new ArrayList<Object>();
            StoreAction action = new MoveAction(targetSector);
            parameters.add(action);
            parameters.add(this.clientStore.getState().player.playerToken);
            try {
                RemoteMethodCall methodCall = this.communicationHandler.newComSession(new RemoteMethodCall("makeAction",parameters));
                this.processRemoteInvocation(methodCall);
                if (this.clientStore.getState().currentReqRespNotification.getActionResult()){
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

    public void useObjCard(int objectCardIndex) throws IOException, ClassNotFoundException {
        int cardsAmount = this.clientStore.getState().player.privateDeck.getSize();
        if (objectCardIndex <= cardsAmount && objectCardIndex > 0) {
            ObjectCard objectCard = this.clientStore.getState().player.privateDeck.getCard(
                    objectCardIndex - 1);
            if (objectCard instanceof LightsObjectCard) {
                this.clientStore.dispatchAction(new ClientAskLightsAction(true));
            } else if (objectCard instanceof AttackObjectCard) {
                this.clientStore.dispatchAction(new ClientAskAttackAction(true));
            } else {
                ArrayList<Object> parameters = new ArrayList<>();
                StoreAction action = new UseObjAction(objectCard);
                parameters.add(action);
                parameters.add(this.clientStore.getState().player.playerToken);
                RemoteMethodCall methodCall = this.communicationHandler.newComSession(new RemoteMethodCall("makeAction",parameters));
                try {
                    this.processRemoteInvocation(methodCall);
                } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                    e.printStackTrace();
                }
                if (this.clientStore.getState().currentReqRespNotification.getActionResult()) {
                    this.clientStore.dispatchAction(new ClientRemoveObjCardAction(objectCard));
                    if (objectCard instanceof TeleportObjectCard) {
                        this.clientStore.dispatchAction(new ClientTeleportAction());
                    }
                    else if (objectCard instanceof SuppressorObjectCard){
                        this.clientStore.dispatchAction(new ClientSuppressAction(true));
                    }
                }
            }
        } else {
            throw new IllegalArgumentException(
                    "Undifined card, please try again");
        }
    }
    public void allowTurn(){
        this.clientStore.dispatchAction(new ClientAllowTurnAction());
    }
    public void globalNoise(Coordinate coordinate, boolean hasObject){
        Sector targetSector = this.clientStore.getState().gameMap.getSectorByCoords(coordinate);
        if (targetSector != null) {
            SectorCard globalNoiseCard = new GlobalNoiseSectorCard(hasObject,
                    targetSector);
            ArrayList<Object> parameters = new ArrayList<Object>();
            StoreAction action = new UseSectorCardAction(globalNoiseCard);
            parameters.add(action);
            parameters.add(this.clientStore.getState().player.playerToken);
            RemoteMethodCall methodCall = null;
            try {
                methodCall = this.communicationHandler.newComSession(new RemoteMethodCall("makeAction",parameters));
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
            try {
                this.processRemoteInvocation(methodCall);
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
            }
        } else {
            throw new IllegalArgumentException(
                    "The sector you have indicated does not exists, please try again");
        }
    }
    public void lights(Coordinate coordinate) {
        Sector targetSector = this.clientStore.getState().gameMap.getSectorByCoords(coordinate);
        if (targetSector != null) {
            ObjectCard lightsCard = new LightsObjectCard(targetSector);
            ArrayList<Object> parameters = new ArrayList<Object>();
            StoreAction action = new UseObjAction(lightsCard);
            parameters.add(action);
            parameters.add(this.clientStore.getState().player.playerToken);
            RemoteMethodCall remoteMethodCall = null;
            try {
                remoteMethodCall = this.communicationHandler.newComSession(new RemoteMethodCall("makeAction",parameters));
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
            try {
                this.processRemoteInvocation(remoteMethodCall);
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
            }
            this.clientStore.dispatchAction(new ClientAskLightsAction(false));
            this.clientStore.dispatchAction(new ClientRemoveObjCardAction(lightsCard));
        } else {
            throw new IllegalArgumentException(
                    "Undefined sector, please try again");
        }

    }
    //Not much sure
    private void setAvailableGames(ArrayList<GamePublicData> avGames){
        this.clientStore.dispatchAction(new ClientSetAvGamesAction(avGames));
    }
    public void endTurn() {
        ArrayList<Object> parameters = new ArrayList<Object>();
        StoreAction action = new EndTurnAction();
        parameters.add(action);
        parameters.add(this.clientStore.getState().player.playerToken);
        try {
            this.communicationHandler.newComSession(new RemoteMethodCall("makeAction",parameters));
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        if (this.clientStore.getState().currentReqRespNotification.getActionResult()) {
            this.clientStore.dispatchAction(new ClientEndTurnAction());
        }
    }
    public void discardCard(int objectCardIndex) {
        int cardsAmount = this.clientStore.getState().player.privateDeck.getSize();
        if (objectCardIndex <= cardsAmount && objectCardIndex > 0) {
            ObjectCard objectCardToDiscard = this.clientStore.getState().player.privateDeck.getCard(
                    objectCardIndex - 1);
            ArrayList<Object> parameters = new ArrayList<Object>();
            StoreAction action = new DiscardAction(objectCardToDiscard);
            parameters.add(action);
            parameters.add(this.clientStore.getState().player.playerToken);
            try {
                RemoteMethodCall remoteMethodCall = this.communicationHandler.newComSession(new RemoteMethodCall("makeAction",parameters));
                this.processRemoteInvocation(remoteMethodCall);
            } catch (IOException | ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }

            if (this.clientStore.getState().currentReqRespNotification.getActionResult()) {
                this.clientStore.dispatchAction(new ClientRemoveObjCardAction(objectCardToDiscard));
            }

        } else {
            throw new IllegalArgumentException(
                    "Undifined card, please try again");
        }

    }
    public void sendMessage(String message){
        ArrayList<Object> parameters = new ArrayList<Object>();
        parameters.add(message);
        parameters.add(this.clientStore.getState().player.playerToken);
        try {
            RemoteMethodCall remoteMethodCall = this.communicationHandler.newComSession(new RemoteMethodCall("publishGlobalMessage", parameters));
            this.processRemoteInvocation(remoteMethodCall);

        } catch (IOException | ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }
    public void attack(Coordinate coordinate){
        boolean humanAttack = ClientStore.getInstance().getState().player.playerType.equals(PlayerType.HUMAN);
        Sector targetSector = this.clientStore.getState().gameMap.getSectorByCoords(coordinate);
        if (targetSector != null) {
            ArrayList<Object> parameters = new ArrayList<Object>();
            AttackObjectCard card = null;
            if (humanAttack) {
                card = new AttackObjectCard(targetSector);
                StoreAction action = new UseObjAction(card);
                parameters.add(action);
                parameters.add(this.clientStore.getState().player.playerToken);
                try {
                    RemoteMethodCall remoteMethodCall = this.communicationHandler.newComSession(new RemoteMethodCall("makeAction",parameters));
                    this.processRemoteInvocation(remoteMethodCall);
                } catch (IOException | ClassNotFoundException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            } else {
                StoreAction action = new MoveAttackAction(targetSector);
                parameters.add(action);
                parameters.add(this.clientStore.getState().player.playerToken);
                RemoteMethodCall remoteMethodCall = null;
                try {
                    remoteMethodCall = this.communicationHandler.newComSession(new RemoteMethodCall("makeAction",parameters));
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
                try {
                    this.processRemoteInvocation(remoteMethodCall);
                } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                    e.printStackTrace();
                }
            }
            if (this.clientStore.getState().currentReqRespNotification.getActionResult()) {
                this.clientStore.dispatchAction(new ClientMoveAction(targetSector));
                this.clientStore.dispatchAction(new ClientAskAttackAction(false));
            }

        } else {
            throw new IllegalArgumentException(
                    "The sector you have indicated does not exists, please try again");
        }

    }
    public void getGames() throws IOException, ClassNotFoundException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        RemoteMethodCall methodCall = this.communicationHandler.newComSession(new RemoteMethodCall("getGames",new ArrayList<Object>()));
        this.processRemoteInvocation(methodCall);

    }
    public void processRemoteInvocation(RemoteMethodCall remoteClientInvocation)
            throws IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, SecurityException {
        if (remoteClientInvocation != null){
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
    public void executeMethod(String methodName, List<Object> parameters) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Class<?>[] parametersClasses = new Class[parameters.size()];
        for (int i=0; i<parametersClasses.length; i++){
            parametersClasses[i] = parameters.get(i).getClass();
        }
        this.getClass().getDeclaredMethod(methodName,parametersClasses).invoke(this,parameters.toArray());
    }

}
