package client_reducers;

import client_store.ClientState;
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
            case "@CLIENT_STARTABLE_GAME":
                return this.startableGame(action,castedState);

        }
        return state;
    }

    private State startableGame(StoreAction action, ClientState castedState) {
        castedState.setStartableGame(true);
        return castedState;
    }

    private State setWinners(StoreAction action, ClientState state) {
        ClientSetWinnersAction castedAction = (ClientSetWinnersAction) action;
        state.setAliensHaveWon(castedAction.isAliensHaveWon());
        state.setHumansHaveWon(castedAction.isHumansHaveWon());
        return state;
    }

    private State setPlayerState(StoreAction action, ClientState state) {
        ClientSetPlayerState castedAction = (ClientSetPlayerState) action;
        state.getPlayer().setPlayerState(castedAction.getPlayerState());
        if (state.getPlayer().getPlayerState().equals(PlayerState.DEAD) ||  state.getPlayer().getPlayerState().equals(PlayerState.ESCAPED)){
            state.setGameStarted(false);
        }
        return state;
    }

    private State discardObjectCard(StoreAction action, ClientState state) {
        ClientDiscardObjectCardAction castedAction = (ClientDiscardObjectCardAction) action;
        state.getPlayer().getPrivateDeck().removeCard(castedAction.getDiscardedObjectCard());
        return state;
    }

    private State adrenaline(ClientState state) {
        state.getPlayer().setAdrenalined(true);
        return state;
    }


    private State setDrawnSectorObjectCard(StoreAction action, ClientState state) {
        ClientSetDrawnSectorObjectCard castedAction = (ClientSetDrawnSectorObjectCard) action;
        if (castedAction.getDrawnObjectCard() != null){
            state.getPlayer().getPrivateDeck().addCard(castedAction.getDrawnObjectCard());
        }
        return state;
    }

    private State setConnectionActive(StoreAction action, ClientState state) {
        ClientSetConnectionActiveAction castedAction = (ClientSetConnectionActiveAction) action;
        state.setConnectionActive(castedAction.isConnectionActive());
        return state;
    }

    private State suppress(StoreAction action, ClientState state) {
        ClientSuppressAction castedAction = (ClientSuppressAction) action;
        state.getPlayer().setSedated(castedAction.isSedated());
        return state;
    }

    private State askSectorToLight(StoreAction action, ClientState state) {
        ClientAskSectorToLightAction castedAction = (ClientAskSectorToLightAction) action;
        state.setAskAttack(castedAction.isToBeAsked());
        return state;
    }
    private State askAttack(StoreAction action, ClientState state){
        ClientAskAttackAction castedAction = (ClientAskAttackAction) action;
        state.setAskAttack(castedAction.isToBeAsked());
        return state;
    }

    private State setPS(StoreAction action, ClientState state) {
        ClientSetCurrentPubSubNotificationAction castedAction = (ClientSetCurrentPubSubNotificationAction) action;
        state.setCurrentPubSubNotification(castedAction.getPsNotification());
        return state;
    }

    private State setRR(StoreAction action, ClientState state) {
        ClientSetCurrentReqRespNotificationAction castedAction = (ClientSetCurrentReqRespNotificationAction) action;
        state.setCurrentReqRespNotification(castedAction.getRrClientNotification());
        return state;
    }



    private State publishMsg(StoreAction action, ClientState state) {
        ClientSetCurrentChatMessage castedAction = (ClientSetCurrentChatMessage) action;
        state.setLastChatMessage(castedAction.getMessage());
        return state;
    }

    private State setAvailableGames(StoreAction action, ClientState state) {
        ClientSetAvailableGamesAction castedAction = (ClientSetAvailableGamesAction) action;
        state.setAvailableGames(castedAction.getAvailableGames());
        return state;
    }

    private State endTurn(StoreAction action, ClientState state) {
        state.setMyTurn(false);
        state.getPlayer().setHasMoved(false);
        state.getPlayer().setAdrenalined(false);
        state.getPlayer().setSedated(false);
        state.setAskAttack(false);
        state.setAskLights(false);
        return state;
    }

    private State setPlayer(StoreAction action, ClientState state) {
        ClientSetPlayerAction castedAction = (ClientSetPlayerAction) action;
        Player player = new Player(castedAction.getPlayerName());
        player.setPlayerToken(castedAction.getPlayerToken());
        state.setPlayer(player);
        return state;
    }

    private State useObjectCard(StoreAction action, ClientState state) {
        ClientUseObjectCard castedAction = (ClientUseObjectCard) action;
        state.getPlayer().getPrivateDeck().removeCard(castedAction.getObjectCard());
        return state;
    }

    private State teleportToStartingSector(StoreAction action, ClientState state) {
        state.getPlayer().setCurrentSector(state.getGameMap().getHumanSector());
        return state;
    }

    private State moveToSector(StoreAction action, ClientState state) {
        ClientMoveToSectorAction castedAction = (ClientMoveToSectorAction) action;
        state.getPlayer().setHasMoved(!castedAction.getTargetSector().equals(state.getPlayer().getCurrentSector()));
        state.getPlayer().setCurrentSector(castedAction.getTargetSector());
        return state;
    }

    private State startGame(StoreAction action, ClientState state) {
        ClientStartGameAction castedAction = (ClientStartGameAction) action;
        Player player = state.getPlayer();
        GameMapFactory mapFactory = GameMapFactory.provideCorrectFactory(castedAction.getGameMapName());
        state.setGameMap( mapFactory.makeMap());
        if (player.getPlayerToken().getPlayerType().equals(PlayerType.ALIEN)) {
            player.setCurrentSector(state.getGameMap().getAlienSector());
        } else {
            player.setCurrentSector(state.getGameMap().getHumanSector());
        }
        state.setGameStarted(true);
        return state;
    }

    private State startTurn(ClientState state){
        state.setMyTurn(true);
        return state;
    }
}
