package client_store;

import client_store_actions.*;
import common.*;
import server_store.StoreAction;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.rmi.NotBoundException;
import java.util.ArrayList;

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

    }
    public void joinGame(int gameId, String playerName){

    }

    public void move(char horCoord, int vertCoord) {
        Coordinate coordinate = new Coordinate(horCoord, vertCoord);
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
                    "The sector you indicated does not exists, please try again");
        }
    }

    public void useObjCard(int objectCardIndex) throws IOException, ClassNotFoundException {
        int cardsAmount = this.clientStore.getState().player.privateDeck.getSize();
        if (objectCardIndex <= cardsAmount && objectCardIndex > 0) {
            ObjectCard objectCard = this.clientStore.getState().player.privateDeck.getCard(
                    objectCardIndex - 1);
            if (objectCard instanceof LightsObjectCard) {
                //
            } else if (objectCard instanceof AttackObjectCard) {
                //
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
                }
            }
        } else {
            throw new IllegalArgumentException(
                    "Undifined card, please try again");
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
