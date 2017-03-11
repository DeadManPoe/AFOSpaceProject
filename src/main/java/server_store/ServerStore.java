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
        ServerState initialState = new ServerState(null,null,null,null);
        this.serverStore.init(initialState);
    }
    private void produceActions(){
        List<String> actions = new ArrayList<String>();
        //Games level actions
        actions.add("@COMMUNICATION_SET_CONNECTION");
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

    public Store getServerStore() {
        return serverStore;
    }
}

