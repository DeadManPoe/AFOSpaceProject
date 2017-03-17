package store_reducers;

import server_store.ServerState;
import store_actions.GamesAddGameAction;
import store_actions.GameEndGame;
import store_actions.StoreAction;
import sts.Reducer;

/**
 * Created by giorgiopea on 14/03/17.
 *
 */
public class GamesReducer extends Reducer {
    @Override
    public ServerState reduce(StoreAction action, ServerState state) {

        String actionType = action.getType();

        if(actionType.equals("@GAMES_ADD_GAME")){
            this.addGame(action,state);
        }
        else if(actionType.equals("@GAMES_REMOVE_GAME")){
            this.removeGame(action,state);
        }
        return state;
    }

    private ServerState removeGame(StoreAction action, ServerState state) {
        GameEndGame castedAction = (GameEndGame) action;
        for(int i=0; i<state.getGames().size(); i++){
            if(state.getGames().get(i).gamePublicData.getId() == castedAction.getPayload()){
                state.getGames().remove(i);
                break;
            }
        }
        for (int i=0; i<state.getPubSubHandlers().size(); i++){
            if(state.getPubSubHandlers().get(i).getGameId().equals(castedAction.getPayload())){
                state.getPubSubHandlers().remove(i);
            }
        }
        return state;
    }

    private ServerState addGame(StoreAction action, ServerState state){
        GamesAddGameAction castedAction = (GamesAddGameAction) action;
        state.getGames().add(castedAction.getPayload());
        return state;
    }
}
