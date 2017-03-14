package server_store;

import common.PlayerToken;
import it.polimi.ingsw.cg_19.Game;
import it.polimi.ingsw.cg_19.Player;
import server.ServerConnection;
import server.SocketRemoteDataExchange;
import server.SocketSubscriberHandler;
import store_actions.StoreAction;
import sts.*;

import java.lang.reflect.Array;
import java.net.Socket;
import java.sql.Timestamp;
import java.util.*;
import java.util.logging.Logger;

/**
 * Created by giorgiopea on 09/03/17.
 *
 */
public class ServerStore {

    private final static ServerStore instance = new ServerStore();
    private final static Logger storeLogger = Logger.getLogger("Store Logger");

    private ObservableServerState observableState;
    private final Map<String, Reducer> actionTypeToReducer = new HashMap<String, Reducer>();
    private final Map<String, Effect> actionTypeToEffect = new HashMap<String, Effect>();


    public static ServerStore getInstance(){
        return instance;
    }

    private ServerStore(){

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

    public void dispatchAction(StoreAction action) {
        String prefix = action.getType().substring(0, action.getType().indexOf("_"));
        Reducer reducer = this.actionTypeToReducer.get(prefix);
        if (reducer != null) {
            storeLogger.info((new Timestamp(System.currentTimeMillis())).toString());
            storeLogger.info("| STATE BEFORE |\n" + this.observableState.toString());
            storeLogger.info("| ACTION |\n" + action.toString());
            this.observableState.setServerState(reducer.reduce(action, this.observableState.getServerState()), action);
            storeLogger.info("| STATE AFTER |\n" + this.observableState.toString());
            this.dispatchEffect(action);
        } else {
            throw new NoSuchElementException("A reducer for the given action does not exist");
        }

    }
    private void dispatchEffect(StoreAction action) {
        Effect effect = this.actionTypeToEffect.get(action.getType());
        if (effect != null) {
            effect.apply(action);
        }
    }

    public void observeState(Observer observer) {
        this.observableState.addObserver(observer);
    }

    public void init(ServerState initialState) {
        this.observableState = new ObservableServerState(initialState);
    }

    private void produceInitialState(){
        ServerState initialState = new ServerState();
        this.init(initialState);
    }
    private void produceActions(){
        List<String> actions = new ArrayList<String>();
        //Games level actions
        actions.add("@COMMUNICATION_SET_TCPORT");
        actions.add("@COMMUNICATION_ADD_SOCKET");
        actions.add("@COMMUNICATION_REMOVE_SOCKET");
        actions.add("@COMMUNICATION_MOVE_SOCKET_TO_PUBSUB");
        actions.add("@GAMES_ADD_GAME");
        actions.add("@GLOBAL_REMOVE_GAME");
        //Game level actions
        actions.add("@GAME_ADD_PLAYER");
        actions.add("@GAME_START_GAME");
        actions.add("@GAME_MAKE_ACTION");
    }
    private void registerReducers(){
        this.registerReducer(new GamesReducer(new ArrayList<String>(Arrays.asList(new String[]{
                "GAMES_BY_ID","GAMES_BY_PLAYERTOKEN"
        }))),"@GAMES");
        this.registerReducer(new CommunicationReducer(new ArrayList<String>(Arrays.asList(
                new String[]{"TCP_PORT"}
        ))),"@COMMUNICATION");
    }
}
