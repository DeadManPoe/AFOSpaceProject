package server_store;

import server_store.StoreAction;

/**
 * Created by giorgiopea on 08/03/17.
 *
 */
public abstract class Effect {

    public abstract void apply(StoreAction action, ServerState state);

}
