package server_store;

/**
 * Created by giorgiopea on 08/03/17.
 *
 */
public interface Reducer {
    State reduce(StoreAction action, State state);
}
