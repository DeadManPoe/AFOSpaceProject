package sts;

import server_store.ServerState;

import java.util.List;
import java.util.Map;

/**
 * Created by giorgiopea on 08/03/17.
 *
 */
public abstract class Reducer {

    private List<String> writableStateKeys;

    public Reducer(List<String> writableStateKeys) {
        this.writableStateKeys = writableStateKeys;
    }

    public List<String> getWritableStateKeys() {
        return writableStateKeys;
    }

    public abstract ServerState reduce(Action action, ServerState state);
}
