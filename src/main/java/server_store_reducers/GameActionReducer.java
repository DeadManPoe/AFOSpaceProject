package server_store_reducers;

import effects.GameActionMapper;
import server.Game;
import server_store.*;
import server_store_actions.GameActionAction;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
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
            castedAction.getGame().setLastActionResult((boolean) executeMethod.invoke(null,castedAction.getGame(), castedAction.getAction()));
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return castedState;
    }
}
