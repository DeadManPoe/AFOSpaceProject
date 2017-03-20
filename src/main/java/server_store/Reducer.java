package server_store;

import server_store.ServerState;
import server_store.StoreAction;

/**
 * Created by giorgiopea on 08/03/17.
 *
 */
public abstract class Reducer {

    public abstract ServerState reduce(StoreAction action, ServerState state);
}
