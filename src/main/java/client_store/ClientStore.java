package client_store;

import client_reducers.ClientReducer;
import client_store_effects.AddPubSubHandlerEffect;
import server_store.*;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Observer;
import java.util.logging.Logger;

/**
 * Created by giorgiopea on 24/03/17.
 *
 */
public class ClientStore {


    private final static ClientStore instance = new ClientStore();
    private final static Logger storeLogger = Logger.getLogger("Store Logger");

    private ObservableClientState observableState;
    private final Map<String, Reducer> actionTypeToReducer = new HashMap<String, Reducer>();
    private final Map<String, Effect> actionTypeToEffect = new HashMap<String, Effect>();


    public static ClientStore getInstance(){
        return instance;
    }

    private ClientStore(){
        this.produceInitialState();
        this.registerReducers();
        this.registerEffects();
    }


    public ClientState getState() {
        return this.observableState.getClientState();
    }

    private void registerReducer(Reducer reducer, String actionType) {
        this.actionTypeToReducer.put(actionType, reducer);
    }

    private void registerEffect(Effect effect, String actionType) {
        this.actionTypeToEffect.put(actionType, effect);
    }

    public void dispatchAction(StoreAction action) {
        String prefix = action.type.substring(0, action.type.indexOf("_"));
        Reducer reducer = this.actionTypeToReducer.get(prefix);
        if (reducer != null) {
            storeLogger.info((new Timestamp(System.currentTimeMillis())).toString());
            storeLogger.info("| STATE BEFORE |\n" + this.observableState.toString());
            storeLogger.info("| ACTION |\n" + action.toString());
            this.observableState.setClientState(reducer.reduce(action, this.observableState.getClientState()), action);
            storeLogger.info("| STATE AFTER |\n" + this.observableState.toString());
            this.dispatchEffect(action);
        } else {
            throw new NoSuchElementException("A reducer for the given action does not exist");
        }

    }
    private void dispatchEffect(StoreAction action) {
        Effect effect = this.actionTypeToEffect.get(action.type);
        if (effect != null) {
            effect.apply(action,this.getState());
        }
    }

    public void observeState(Observer observer) {
        this.observableState.addObserver(observer);
    }

    private void init(ClientState initialState) {
        this.observableState = new ObservableClientState(initialState);
    }

    private void produceInitialState(){
        ClientState initialState = new ClientState();
        this.init(initialState);
    }
    private void registerReducers(){
        this.registerReducer(new ClientReducer(),"@CLIENT");
        this.registerReducer(new client_reducers.CommunicationReducer(),"@COMMUNICATION");
    }
    private void registerEffects(){
        this.registerEffect(new AddPubSubHandlerEffect(),"@COMMUNICATION_ADD_PUB_SUB_HANDLER");
    }


}