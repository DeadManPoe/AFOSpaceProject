package sts;

import server_store.ObservableServerState;
import server_store.ServerState;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Observer;
import java.util.logging.Logger;

/**
 * Created by giorgiopea on 08/03/17.
 */
public class Store {

    private static Store ourInstance = new Store();

    public static Store getInstance() {
        return ourInstance;
    }

    private ObservableServerState observableState;
    private final Map<String, Reducer> actionTypeToReducer = new HashMap<String, Reducer>();
    private final Map<String, Effect> actionTypeToEffect = new HashMap<String, Effect>();
    private final static Logger storeLogger = Logger.getLogger("Store Logger");

    private Store() {
    }


    public ServerState getState() {
        return this.observableState.getServerState();
    }

    public void registerReducer(Reducer reducer, String actionType) {
        this.actionTypeToReducer.put(actionType, reducer);
    }

    public void registerEffect(Effect effect, String actionType) {

        this.actionTypeToEffect.put(actionType, effect);
    }

    public void dispatchAction(Action action) {
        String prefix = action.type.substring(0, action.type.indexOf("_"));
        Reducer reducer = this.actionTypeToReducer.get(prefix);
        if (reducer != null) {
            storeLogger.info((new Timestamp(System.currentTimeMillis())).toString());
            storeLogger.info("| STATE BEFORE |\n" + this.observableState.toString());
            storeLogger.info("| ACTION |\n" + action.toString());
            //this.observableState.setServerState(reducer.reduce(action, this.observableState.getServerState()), action);
            storeLogger.info("| STATE AFTER |\n" + this.observableState.toString());
            this.dispatchSideEffect(action);
        } else {
            throw new NoSuchElementException("A reducer for the given action does not exist");
        }

    }

    private void dispatchSideEffect(Action action) {
        Effect effect = this.actionTypeToEffect.get(action.type);
        if (effect != null) {
            //effect.apply(action);
        }
    }

    public void observeState(Observer observer) {
        this.observableState.addObserver(observer);
    }

    public void init(ServerState initialState) {
        this.observableState = new ObservableServerState(initialState);
    }
}
