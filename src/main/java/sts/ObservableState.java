package sts;

import java.util.List;
import java.util.Map;
import java.util.Observable;

/**
 * Created by giorgiopea on 08/03/17.
 *
 */
public class ObservableState extends Observable {
    /**
     * The actual state of the application as a Map
     */
    private Map<String, Object> state;


    public ObservableState(Map<String, Object> state) {
        this.state = state;
    }

    public Map<String, Object> getState() {
        return state;
    }

    /**
     * Sets the state and the list of keys of the state whose associated value will be changed, notifies
     * any registered observer
     * @param state The new state
     * @param keysWithChangedValue The list of keys of the state whose associated value will be changed
     */
    public void setState(Map<String, Object> state, List<String> keysWithChangedValue) {
        this.state = state;
        setChanged();
        notifyObservers(keysWithChangedValue);
    }

    @Override
    public String toString() {
        return "ObservableState{" +
                "state=" + state.toString() +
                '}';
    }
}
