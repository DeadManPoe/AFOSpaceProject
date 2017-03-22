package store_reducers;

import effects.GameActionMapper;
import server_store.Game;
import server_store.Reducer;
import server_store.ServerState;
import server_store.StoreAction;
import store_actions.GameActionAction;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by giorgiopea on 21/03/17.
 */
public class GameActionReducer extends Reducer {

    @Override
    public ServerState reduce(StoreAction action, ServerState state) {
        GameActionAction castedAction = (GameActionAction) action;
        try {
            Method executeMethod = GameActionMapper.getInstance().getEffect(action.getType()).getMethod("executeEffect", Game.class, StoreAction.class);
            castedAction.game.lastActionResult = (boolean) executeMethod.invoke(null,castedAction.game, castedAction.action);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return state;
    }
}
