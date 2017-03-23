package store_reducers;

import server_store.ServerState;
import store_actions.GamesEndGameAction;
import store_actions.GamesAddGameAction;
import server_store.StoreAction;
import server_store.Reducer;

/**
 * Created by giorgiopea on 14/03/17.
 *
 * Handles the logic related to the slice of the app's state
 * represented by the games
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

    /**
     * Removes a game from the list of games in the app's state
     * @param action The action that has triggered this task, see {@link store_actions.GamesEndGameAction}
     * @param state The current app's state
     * @return A new app's state
     */
    private ServerState removeGame(StoreAction action, ServerState state) {
        GamesEndGameAction castedAction = (GamesEndGameAction) action;
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
    /**
     * Adds a game to the list of games in the app's state
     * @param action The action that has triggered this task
     * @param state The current app's state, see {@link store_actions.GamesAddGameAction}
     * @return A new app's state
     */
    private ServerState addGame(StoreAction action, ServerState state){
        GamesAddGameAction castedAction = (GamesAddGameAction) action;
        state.getGames().add(castedAction.getPayload());
        return state;
    }
}
