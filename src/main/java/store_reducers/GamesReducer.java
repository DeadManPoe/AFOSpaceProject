package store_reducers;

import com.sun.corba.se.spi.activation.Server;
import server_store.Game;
import server_store.ServerState;
import server_store.ServerStore;
import store_actions.GamesAddGameAction;
import store_actions.GamesRemoveGameAction;
import store_actions.StoreAction;
import sts.Reducer;

/**
 * Created by giorgiopea on 14/03/17.
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
        GamesRemoveGameAction castedAction = (GamesRemoveGameAction) action;
        for(int i=0; i<state.getGames().size(); i++){
            if(state.getGames().get(i).gamePublicData.getId() == castedAction.getPayload()){
                state.getGames().remove(i);
                break;
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
