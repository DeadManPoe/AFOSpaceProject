package sts;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Observer;
import java.util.logging.Logger;

/**
 * Created by giorgiopea on 08/03/17.
 *
 */
public class Store {

    private static Store ourInstance = new Store();

    public static Store getInstance() {
        return ourInstance;
    }

    private ObservableState observableState;
    private final Map<String, Reducer> actionTypeToReducer = new HashMap<String, Reducer>();
    private final Map<String, Effect> actionTypeToEffect = new HashMap<String,Effect>();
    private final static Logger storeLogger = Logger.getLogger("Store Logger");

    private Store() {}

    public void init(Map<String, Object> state){
        this.observableState = new ObservableState(state);
    }

    public Map<String,Object> getState(){
        return this.observableState.getState();
    }
    public void registerReducer(Reducer reducer, String actionType){
        this.actionTypeToReducer.put(actionType, reducer);
    }
    public void registerEffect(Effect effect, String actionType){

        this.actionTypeToEffect.put(actionType, effect);
    }
    public void dispatchAction(Action action){
        Reducer reducer = this.actionTypeToReducer.get(action.type);
        if(reducer != null){
            storeLogger.info((new Timestamp(System.currentTimeMillis())).toString());
            storeLogger.info("| STATE BEFORE |\n"+this.observableState.toString());
            storeLogger.info("| ACTION |\n"+action.toString());
            this.observableState.setState(reducer.reduce(action, this.observableState.getState()), reducer.getWritableStateKeys());
            storeLogger.info("| STATE AFTER |\n"+this.observableState.toString());
            this.dispatchSideEffect(action);
        }
        else{
            throw new NoSuchElementException("A reducer for the given action does not exist");
        }
    }
    private void dispatchSideEffect(Action action){
        Effect effect = this.actionTypeToEffect.get(action.type);
        if(effect != null){
            effect.apply(action);
        }
    }
    public void observeState(Observer observer){
        this.observableState.addObserver(observer);
    }
}
