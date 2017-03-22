package store_reducers;

import server_store.ServerState;
import store_actions.GameEndGameAction;
import store_actions.GamesAddGameAction;
import server_store.StoreAction;
import server_store.Reducer;

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
        else if(actionType.equals("@GAMES_END_GAME")){
            this.removeGame(action,state);
        }
        return state;
    }

    private ServerState removeGame(StoreAction action, ServerState state) {
        GameEndGameAction castedAction = (GameEndGameAction) action;
        for(int i=0; i<state.getGames().size(); i++){
            if(state.getGames().get(i).gamePublicData.getId() == castedAction.getPayload()){
                state.getGames().remove(i);
                break;
            }
        }
        for (int i=0; i<state.getPubSubHandlers().size(); i++){
            if(state.getPubSubHandlers().get(i).getPlayerToken().getGameId().equals(castedAction.getPayload())){
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
