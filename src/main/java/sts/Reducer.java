package sts;

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

    public abstract Map<String,Object> reduce(Action action, Map<String, Object> state);
}
