package client_reducers;

import client_store.ClientState;
import client_store_actions.*;
import factories.GameMapFactory;
import it.polimi.ingsw.cg_19.PlayerType;
import server_store.Player;
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
        switch (action.getType()){
            case "@CLIENT_SET_GAME_MAP":
                return this.setGameMap(action,castedState);
            case "@CLIENT_START_GAME":
                return this.startGame(action,castedState);
            case "@CLIENT_ALLOW_TURN":
                return this.allowTurn(action,castedState);
            case "@CLIENT_MOVE_TO_SECTOR":
                return this.moveToSector(action,castedState);
            case "@CLIENT_TELEPORT":
                return this.teleport(action,castedState);
            case "@CLIENT_REMOVE_OBJ_CARD":
                return this.removeObjCard(action,castedState);
            case "@CLIENT_SET_PLAYER_TOKEN":
                return this.setClientToken(action,castedState);
            case "@CLIENT_INIT_PLAYER":
                return this.initPlayer(action,castedState);
            case "@CLIENT_END_TURN":
                return this.endTurn(action,castedState);
            case "@CLIENT_SET_AV_GAMES":
                return this.setAvGames(action,castedState);
            case "@CLIENT_PUBLISH_MSG":
                return this.publishMsg(action,castedState);

        }
        return state;
    }

    private State publishMsg(StoreAction action, ClientState state) {
        ClientSetCurrentMessage castedAction = (ClientSetCurrentMessage) action;
        state.lastChatMessage = castedAction.payload;
        return state;
    }

    private State setAvGames(StoreAction action, ClientState state) {
        ClientSetAvGamesAction castedAction = (ClientSetAvGamesAction) action;
        state.availableGames = castedAction.payload;
        return state;
    }

    private State endTurn(StoreAction action, ClientState state) {
        state.isMyTurn = false;
        state.player.hasMoved = false;
        return state;
    }

    private State initPlayer(StoreAction action, ClientState state) {
        ClientInitPlayerAction castedAction = (ClientInitPlayerAction) action;
        state.player = new Player(null,castedAction.payload,null);
        return state;
    }

    private State setClientToken(StoreAction action, ClientState state) {
        ClientSetPlayerTokenAction castedAction = (ClientSetPlayerTokenAction) action;
        state.player.playerToken = castedAction.payload;
        state.player.playerType = castedAction.payload.getPlayerType();
        return state;
    }

    private State removeObjCard(StoreAction action, ClientState state) {
        ClientRemoveObjCardAction castedAction = (ClientRemoveObjCardAction) action;
        state.player.privateDeck.removeCard(castedAction.payload);
        return state;
    }

    private State teleport(StoreAction action, ClientState state) {
        state.player.currentSector = state.gameMap.getHumanSector();
        return state;
    }

    private State moveToSector(StoreAction action, ClientState state) {
        ClientMoveAction castedAction = (ClientMoveAction) action;
        state.player.currentSector = castedAction.payload;
        state.player.hasMoved = true;
        return state;
    }

    private State startGame(StoreAction action, ClientState state) {
        state.isGameStarted = true;
        state.gamePollingTimer.cancel();
        return state;
    }

    private State setGameMap(StoreAction action, ClientState state) {
        ClientSetGameMapAction castedAction = (ClientSetGameMapAction) action;
        GameMapFactory mapFactory = GameMapFactory.provideCorrectFactory(castedAction.payload);
        state.gameMap = mapFactory.makeMap();
        if (state.player.playerType.equals(PlayerType.ALIEN)) {
            state.player.currentSector = state.gameMap.getAlienSector();
        } else {
            state.player.currentSector = state.gameMap.getAlienSector();
        }
        return state;
    }
    private State allowTurn(StoreAction action, ClientState state){
        state.isMyTurn = true;
        return state;
    }
}
