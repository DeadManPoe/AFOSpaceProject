package store_reducers;

import effects.GameActionMapper;
import server.Game;
import server_store.*;
import store_actions.GameActionAction;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by giorgiopea on 21/03/17.
 *
 * Handles the logic related to the slice of the app's state represented by a single game,
 * in particular the logic related to in-game actions
 */
public class GameActionReducer implements Reducer {

    @Override
    public ServerState reduce(StoreAction action, State state) {
        /*
         * Executes the logic associated with the in-game action contained in the given StoreAction
         */
        ServerState castedState = (ServerState) state;
        GameActionAction castedAction = (GameActionAction) action;
        try {
            //From the class of the effect associated with the in-game action, by using reflection, the executeEffect
            //method is invoked with the necessary arguments
            Method executeMethod = GameActionMapper.getInstance().getEffect(castedAction.type).getMethod("executeEffect", Game.class, StoreAction.class);
            castedAction.game.lastActionResult = (boolean) executeMethod.invoke(null,castedAction.game, castedAction.action);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return castedState;
    }
}
