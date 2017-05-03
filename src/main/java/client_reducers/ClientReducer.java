package client_reducers;

import client.ClientState;
import client_store_actions.*;
import common.PlayerState;
import common.StatefulTimer;
import factories.GameMapFactory;
import common.PlayerType;
import common.Player;
import server_store.Reducer;
import server_store.State;
import server_store.StoreAction;

/**
 * Created by giorgiopea on 25/03/17.
 *
 */
public class ClientReducer implements Reducer {
    @Override
    public State reduce(StoreAction action, State state) {
        ClientState castedState = (ClientState) state;
        switch (action.type){
            case "@CLIENT_START_GAME":
                return this.startGame(action,castedState);
            case "@CLIENT_START_TURN":
                return this.startTurn(castedState);
            case "@CLIENT_MOVE_TO_SECTOR":
                return this.moveToSector(action,castedState);
            case "@CLIENT_TELEPORT_TO_STARTING_SECTOR":
                return this.teleportToStartingSector(action,castedState);
            case "@CLIENT_USE_OBJECT_CARD":
                return this.useObjectCard(action,castedState);
            case "@CLIENT_SET_PLAYER":
                return this.setPlayer(action,castedState);
            case "@CLIENT_ADRENALINE":
                return this.adrenaline(castedState);
            case "@CLIENT_DISCARD_OBJECT_CARD":
                return this.discardObjectCard(action, castedState);
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
            case "@CLIENT_SET_PLAYER_STATE":
                return this.setPlayerState(action, castedState);
            case "@CLIENT_SET_WINNERS":
                return this.setWinners(action, castedState);

        }
        return state;
    }

    private State setWinners(StoreAction action, ClientState state) {
        ClientSetWinnersAction castedAction = (ClientSetWinnersAction) action;
        state.aliensHaveWon = castedAction.aliensHaveWon;
        state.humansHaveWon = castedAction.humansHaveWon;
        return state;
    }

    private State setPlayerState(StoreAction action, ClientState state) {
        ClientSetPlayerState castedAction = (ClientSetPlayerState) action;
        state.player.setPlayerState(castedAction.getPlayerState());
        if (state.player.getPlayerState().equals(PlayerState.DEAD) ||  state.player.getPlayerState().equals(PlayerState.ESCAPED)){
            state.isGameStarted = false;
        }
        return state;
    }

    private State discardObjectCard(StoreAction action, ClientState state) {
        ClientDiscardObjectCardAction castedAction = (ClientDiscardObjectCardAction) action;
        state.player.getPrivateDeck().removeCard(castedAction.getDiscardedObjectCard());
        return state;
    }

    private State adrenaline(ClientState state) {
        state.player.setAdrenalined(true);
        return state;
    }


    private State setDrawnSectorObjectCard(StoreAction action, ClientState state) {
        ClientSetDrawnSectorObjectCard castedAction = (ClientSetDrawnSectorObjectCard) action;
        if (castedAction.getDrawnObjectCard() != null){
            state.player.getPrivateDeck().addCard(castedAction.getDrawnObjectCard());
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
        state.player.setSedated(castedAction.isSedated());
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

    private State setPS(StoreAction action, ClientState state) {
        ClientSetCurrentPubSubNotificationAction castedAction = (ClientSetCurrentPubSubNotificationAction) action;
        state.currentPubSubNotification = castedAction.psNotification;
        return state;
    }

    private State setRR(StoreAction action, ClientState state) {
        ClientSetCurrentReqRespNotificationAction castedAction = (ClientSetCurrentReqRespNotificationAction) action;
        state.currentReqRespNotification = castedAction.rrClientNotification;
        return state;
    }



    private State publishMsg(StoreAction action, ClientState state) {
        ClientSetCurrentChatMessage castedAction = (ClientSetCurrentChatMessage) action;
        state.lastChatMessage = castedAction.message;
        return state;
    }

    private State setAvailableGames(StoreAction action, ClientState state) {
        ClientSetAvailableGamesAction castedAction = (ClientSetAvailableGamesAction) action;
        state.availableGames = castedAction.availableGames;
        if (state.gamePollingTimer == null){
            state.gamePollingTimer = new StatefulTimer();
        }
        return state;
    }

    private State endTurn(StoreAction action, ClientState state) {
        state.isMyTurn = false;
        state.player.setHasMoved(false);
        state.player.setAdrenalined(false);
        state.player.setSedated(false);
        state.askAttack = false;
        state.askLights = false;
        return state;
    }

    private State setPlayer(StoreAction action, ClientState state) {
        ClientSetPlayerAction castedAction = (ClientSetPlayerAction) action;
        Player player = new Player(castedAction.getPlayerName());
        player.setPlayerToken(castedAction.getPlayerToken());
        state.player = player;
        return state;
    }

    private State useObjectCard(StoreAction action, ClientState state) {
        ClientUseObjectCard castedAction = (ClientUseObjectCard) action;
        state.player.getPrivateDeck().removeCard(castedAction.getObjectCard());
        return state;
    }

    private State teleportToStartingSector(StoreAction action, ClientState state) {
        state.player.setCurrentSector(state.gameMap.getHumanSector());
        return state;
    }

    private State moveToSector(StoreAction action, ClientState state) {
        ClientMoveToSectorAction castedAction = (ClientMoveToSectorAction) action;
        state.player.setHasMoved(!castedAction.getTargetSector().equals(state.player.getCurrentSector()));
        state.player.setCurrentSector(castedAction.getTargetSector());
        return state;
    }

    private State startGame(StoreAction action, ClientState state) {
        ClientStartGameAction castedAction = (ClientStartGameAction) action;
        Player player = state.player;
        GameMapFactory mapFactory = GameMapFactory.provideCorrectFactory(castedAction.gameMapName);
        state.gameMap = mapFactory.makeMap();
        if (player.getPlayerToken().getPlayerType().equals(PlayerType.ALIEN)) {
            player.setCurrentSector(state.gameMap.getAlienSector());
        } else {
            player.setCurrentSector(state.gameMap.getHumanSector());
        }
        state.isGameStarted = true;
        return state;
    }

    private State startTurn(ClientState state){
        state.isMyTurn = true;
        return state;
    }
}
