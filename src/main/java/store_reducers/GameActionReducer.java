package store_reducers;

import effects.GameActionMapper;
import server_store.Reducer;
import server_store.ServerState;
import server_store.StoreAction;

/**
 * Created by giorgiopea on 21/03/17.
 */
public class GameActionReducer extends Reducer {

    @Override
    public ServerState reduce(StoreAction action, ServerState state) {
        GameActionMapper.getInstance().getEffect(action).executeEffect();
    }
}
