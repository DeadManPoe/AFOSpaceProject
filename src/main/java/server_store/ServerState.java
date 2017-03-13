package server_store;

import common.PlayerToken;
import it.polimi.ingsw.cg_19.Game;
import server.ServerConnection;

import java.io.Serializable;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by giorgiopea on 11/03/17.
 *
 */
public class ServerState {

    public Integer TCP_PORT;
    public Map<Integer, server_store.Game> GAMES_BY_ID;
    public Map<Integer, PubSubHandler[]> GAME_TO_SUBSCRIBERS;


    public ServerState(Integer TCP_PORT, Map<Integer, server_store.Game> GAMES_BY_ID, Map<Integer, PubSubHandler[]> GAME_TO_SUBSCRIBERS) {
        this.TCP_PORT = TCP_PORT;
        this.GAMES_BY_ID = GAMES_BY_ID;
        this.GAME_TO_SUBSCRIBERS = GAME_TO_SUBSCRIBERS;
    }
}
