package client_reducers;

import client_store.ClientState;
import client_store_actions.ClientSetGameMapAction;
import factories.GameMapFactory;
import it.polimi.ingsw.cg_19.PlayerType;
import server_store.Reducer;
import server_store.State;
import server_store.StoreAction;

/**
 * Created by giorgiopea on 25/03/17.
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
        }
        return state;
    }

    private State startGame(StoreAction action, ClientState state) {
        state.isGameStarted = true;
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
