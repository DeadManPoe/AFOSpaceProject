package server_store;

import common.PlayerToken;
import it.polimi.ingsw.cg_19.Game;
import it.polimi.ingsw.cg_19.Player;
import server.ServerConnection;
import server.SocketRemoteDataExchange;
import server.SocketSubscriberHandler;
import sts.ActionFactory;
import sts.Store;

import java.lang.reflect.Array;
import java.net.Socket;
import java.util.*;

/**
 * Created by giorgiopea on 09/03/17.
 *
 */
public class ServerStore {

    public static Store serverStore = Store.getInstance();
    public static ActionFactory actionFactory = ActionFactory.getInstance();


    public static void produceInitialState(){
        ServerState initialState = new ServerState(null,new HashMap<PlayerToken,Game>(),new HashMap<Integer, Game>(),null);
        serverStore.init(initialState);
    }
    public static void produceActions(){
        List<String> actions = new ArrayList<String>();
        //Games level actions
        actions.add("@COMMUNICATION_SET_TCPORT");
        actions.add("@COMMUNICATION_ADD_SOCKET");
        actions.add("@COMMUNICATION_REMOVE_SOCKET");
        actions.add("@COMMUNICATION_MOVE_SOCKET_TO_PUBSUB");
        actions.add("@GAMES_ADD_GAME");
        actions.add("@GLOBAL_REMOVE_GAME");
        //Debatable
        actions.add("@GAMES_ADD_PLAYER_TO_GAME");
        //
        //Game level actions
        actions.add("@GAME_ADD_PLAYER");
        actions.add("@GAME_START_GAME");
        actions.add("@GAME_MAKE_ACTION");

        actionFactory.init(actions);
    }
    public static void registerReducers(){
        serverStore.registerReducer(new GamesReducer(new ArrayList<String>(Arrays.asList(new String[]{
                "GAMES_BY_ID","GAMES_BY_PLAYERTOKEN"
        }))),"@GAMES");
        serverStore.registerReducer(new CommunicationReducer(new ArrayList<String>(Arrays.asList(
                new String[]{"TCP_PORT"}
        ))),"@COMMUNICATION");
    }

}

