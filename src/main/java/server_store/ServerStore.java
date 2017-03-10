package server_store;

import common.PlayerToken;
import it.polimi.ingsw.cg_19.Game;
import server.ServerConnection;
import server.SocketRemoteDataExchange;
import server.SocketSubscriberHandler;
import sts.ActionFactory;
import sts.Store;

import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by giorgiopea on 09/03/17.
 *
 */
public class ServerStore {

    private Store serverStore = Store.getInstance();
    private ActionFactory actionFactory = ActionFactory.getInstance();

    public ServerStore() {

        this.produceInitialState();
        this.produceActions();
    }

    private void produceInitialState(){
        Map<String,Object> initialState = new HashMap<String,Object>();
        //Games by Id
        Map<String, Game> gamesById = new HashMap<String, Game>();
        //Games by PlayerToken
        Map<PlayerToken, Game> gamesByPlayerToken = new HashMap<PlayerToken, Game>();
        //Server connection
        ServerConnection connection = null;
        List<Socket> reqRespSockets = new ArrayList<Socket>();
        List<Socket> pubSubSockets = new ArrayList<Socket>();



        initialState.put("games_by_id",gamesById);
        initialState.put("games_by_player", gamesByPlayerToken);
        initialState.put("@COMMUNICATION_CONNECTION_INFO",connection);
        initialState.put("@COMMUNICATION_REQRESPONSE_SOCKETS",reqRespSockets);
        initialState.put("@COMMUNICATION_PUBSUB_SOCKETS",pubSubSockets);


        this.serverStore.init(initialState);
    }
    private void produceActions(){
        List<String> actions = new ArrayList<String>();
        //Games level actions
        actions.add("@COMMUNICATION_SET_CONNECTION");
        actions.add("@COMMUNICATION_ADD_SOCKET");
        actions.add("@COMMUNICATION_REMOVE_SOCKET");
        actions.add("@GLOBAL_ADD_GAME");
        actions.add("@GLOBAL_REMOVE_GAME");
        //Debatable
        actions.add("@GLOBAL_ADD_PLAYER_TO_GAME");
        //
        //Game level actions
        actions.add("@GAME_ADD_PLAYER");
        actions.add("@GAME_START_GAME");
        actions.add("@GAME_MAKE_ACTION");

        actionFactory.init(actions);
    }

    public Store getServerStore() {
        return serverStore;
    }
}

