package client_reducers;

import client_store.ClientState;
import client_store_actions.*;
import factories.GameMapFactory;
import it.polimi.ingsw.cg_19.PlayerType;
import server_store.Player;
import server_store.Reducer;
import server_store.State;
import server_store.StoreAction;

import java.util.Timer;

/**
 * Created by giorgiopea on 25/03/17.
 *
 */
public class ClientReducer implements Reducer {
    @Override
    public State reduce(StoreAction action, State state) {
        ClientState castedState = (ClientState) state;
        switch (action.getType()){
            case "@CLIENT_START_GAME":
                return this.startGame(action,castedState);
            case "@CLIENT_ALLOW_TURN":
                return this.allowTurn(action,castedState);
            case "@CLIENT_MOVE_TO_SECTOR":
                return this.moveToSector(action,castedState);
            case "@CLIENT_TELEPORT_TO_STARTING_SECTOR":
                return this.teleportToStartingSector(action,castedState);
            case "@CLIENT_USE_OBJECT_CARD":
                return this.useObjectCard(action,castedState);
            case "@CLIENT_SET_PLAYER":
                return this.setPlayer(action,castedState);
            case "@CLIENT_ADRENALINE":
                return this.adrenaline(action,castedState);
            case "@CLIENT_END_TURN":
                return this.endTurn(action,castedState);
            case "@CLIENT_SET_AVAILABLE_GAMES":
                return this.setAvailableGames(action,castedState);
            case "@CLIENT_PUBLISH_MSG":
                return this.publishMsg(action,castedState);
            case "@CLIENT_SET_CURRENT_REQRESP_NOTIFICATION":
                return this.setRR(action,castedState);
            case "@CLIENT_SET_CURRENT_PUBSUB_NOTIFICATION":
                return this.setPS(action,castedState);
            case "@CLIENT_DENY_TURN":
                return this.denyTurn(action,castedState);
            case "@CLIENT_ASK_SECTOR_TO_LIGHT":
                return this.askSectorToLight(action,castedState);
            case "@CLIENT_ASK_SECTOR_TO_ATTACK":
                return this.askAttack(action,castedState);
            case "@CLIENT_SUPPRESS":
                return this.suppress(action, castedState);
            case "@CLIENT_SET_CONNECTION_ACTIVE":
                return this.setConnectionActive(action, castedState);
            case "@CLIENT_SET_DRAWN_SECTOR_OBJECT_CARD":
                return this.setDrawnSectorObjectCard(action, castedState);

        }
        return state;
    }

    private State adrenaline(StoreAction action, ClientState state) {
        state.player.isAdrenalined = true;
        return state;
    }


    private State setDrawnSectorObjectCard(StoreAction action, ClientState state) {
        ClientSetDrawnSectorObjectCard castedAction = (ClientSetDrawnSectorObjectCard) action;
        if (castedAction.drawnObjectCard != null){
            state.player.privateDeck.addCard(castedAction.drawnObjectCard);
        }
        return state;
    }

    private State setConnectionActive(StoreAction action, ClientState state) {
        ClientSetConnectionActiveAction castedAction = (ClientSetConnectionActiveAction) action;
        state.connectionActive = castedAction.isConnectionActive;
        return state;
    }

    private State suppress(StoreAction action, ClientState state) {
        ClientSuppressAction castedAction = (ClientSuppressAction) action;
        state.player.isSedated = castedAction.payload;
        return state;
    }

    private State askSectorToLight(StoreAction action, ClientState state) {
        ClientAskSectorToLightAction castedAction = (ClientAskSectorToLightAction) action;
        state.askLights = castedAction.toBeAsked;
        return state;
    }
    private State askAttack(StoreAction action, ClientState state){
        ClientAskAttackAction castedAction = (ClientAskAttackAction) action;
        state.askAttack = castedAction.toBeAsked;
        return state;
    }

    private State denyTurn(StoreAction action, ClientState state) {
        state.isMyTurn = false;
        return state;
    }

    private State setPS(StoreAction action, ClientState state) {
        ClientSetCurrentPubSubNotificationAction castedAction = (ClientSetCurrentPubSubNotificationAction) action;
        state.currentPubSubNotification = castedAction.payload;
        return state;
    }

    private State setRR(StoreAction action, ClientState state) {
        ClientSetCurrentReqRespNotificationAction castedAction = (ClientSetCurrentReqRespNotificationAction) action;
        state.currentReqRespNotification = castedAction.payload;
        return state;
    }



    private State publishMsg(StoreAction action, ClientState state) {
        ClientSetCurrentMessage castedAction = (ClientSetCurrentMessage) action;
        state.lastChatMessage = castedAction.payload;
        return state;
    }

    private State setAvailableGames(StoreAction action, ClientState state) {
        ClientSetAvailableGamesAction castedAction = (ClientSetAvailableGamesAction) action;
        state.availableGames = castedAction.availableGames;
        state.gamePollingTimer = new Timer();
        return state;
    }

    private State endTurn(StoreAction action, ClientState state) {
        state.isMyTurn = false;
        state.player.hasMoved = false;
        return state;
    }

    private State setPlayer(StoreAction action, ClientState state) {
        ClientSetPlayerAction castedAction = (ClientSetPlayerAction) action;
        state.player = new Player(castedAction.playerName,castedAction.playerToken);
        return state;
    }

    private State useObjectCard(StoreAction action, ClientState state) {
        ClientUseObjectCard castedAction = (ClientUseObjectCard) action;
        if ( castedAction.objectCard != null){
            state.player.privateDeck.removeCard(castedAction.objectCard);
        }
        return state;
    }

    private State teleportToStartingSector(StoreAction action, ClientState state) {
        state.player.currentSector = state.gameMap.getHumanSector();
        return state;
    }

    private State moveToSector(StoreAction action, ClientState state) {
        ClientMoveAction castedAction = (ClientMoveAction) action;
        state.player.hasMoved = !castedAction.targetSector.equals(state.player.currentSector);
        state.player.currentSector = castedAction.targetSector;
        return state;
    }

    private State startGame(StoreAction action, ClientState state) {
        ClientStartGameAction castedAction = (ClientStartGameAction) action;
        Player player = state.player;
        GameMapFactory mapFactory = GameMapFactory.provideCorrectFactory(castedAction.gameMapName);
        state.gameMap = mapFactory.makeMap();
        if (player.playerToken.playerType.equals(PlayerType.ALIEN)) {
            player.currentSector = state.gameMap.getAlienSector();
        } else {
            player.currentSector = state.gameMap.getHumanSector();
        }
        state.gamePollingTimer.cancel();
        return state;
    }

    private State allowTurn(StoreAction action, ClientState state){
        state.isMyTurn = true;
        return state;
    }
}
