package store_reducers;

import server.Game;
import server.PubSubHandler;
import server_store.ServerState;
import server_store.State;
import store_actions.GamesEndGameAction;
import store_actions.GamesAddGameAction;
import server_store.StoreAction;
import server_store.Reducer;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * Created by giorgiopea on 14/03/17.
 *
 * Handles the logic related to the slice of the app's state
 * represented by the games
 */
public class GamesReducer implements Reducer {
    /**
     * @see server_store.Reducer
     */
    @Override
    public ServerState reduce(StoreAction action, State state) {
        ServerState castedState = (ServerState) state;
        String actionType = action.type;

        if(actionType.equals("@GAMES_ADD_GAME")){
            this.addGame(action,castedState);
        }
        else if(actionType.equals("@GAMES_END_GAME")){
            this.removeGame(action,castedState);
        }
        return castedState;
    }

    /**
     * Removes a game from the list of games in the app's state
     * @param action The action that has triggered this task, see {@link store_actions.GamesEndGameAction}
     * @param state The current app's state
     * @return The app's new state
     */
    private ServerState removeGame(StoreAction action, ServerState state) {
        GamesEndGameAction castedAction = (GamesEndGameAction) action;
        state.getGames().remove(castedAction.getGame());
        for (PubSubHandler handler : state.getPubSubHandlers()){
            if (handler.getPlayerToken().getGameId() == castedAction.getGame().getGamePublicData().getId()){
                handler.setRunningFlag(false);
                state.getPubSubHandlers().remove(handler);
            }
        }
        return state;
    }
    /**
     * Adds a game to the list of games in the app's state
     * @param action The action that has triggered this task
     * @param state The current app's state, see {@link store_actions.GamesAddGameAction}
     * @return The app's new state
     */
    private ServerState addGame(StoreAction action, ServerState state){
        GamesAddGameAction castedAction = (GamesAddGameAction) action;
        state.getGames().add(castedAction.getPayload());
        return state;
    }
}
