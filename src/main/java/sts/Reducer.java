package sts;

import server_store.ServerState;
import store_actions.StoreAction;

import java.util.List;
import java.util.Map;

/**
 * Created by giorgiopea on 08/03/17.
 *
 */
public abstract class Reducer {

    public abstract ServerState reduce(StoreAction action, ServerState state);
}
