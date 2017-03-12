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
    public Map<PlayerToken, Game> GAMES_BY_PLAYERTOKEN;
    public Map<Integer, Game> GAMES_BY_ID;
    public Map<PlayerToken, Socket> PUBSUBSOCKETS_BY_PLAYERTOKEN;

    public ServerState(Integer TCP_PORT, Map<PlayerToken, Game> GAMES_BY_PLAYERTOKEN, Map<Integer, Game> GAMES_BY_ID, Map<PlayerToken, Socket> PUBSUBSOCKETS_BY_PLAYERTOKEN) {
        this.TCP_PORT = TCP_PORT;
        this.GAMES_BY_PLAYERTOKEN = GAMES_BY_PLAYERTOKEN;
        this.GAMES_BY_ID = GAMES_BY_ID;
        this.PUBSUBSOCKETS_BY_PLAYERTOKEN = PUBSUBSOCKETS_BY_PLAYERTOKEN;
    }


    public ServerState(ServerState state) {
        this.TCP_PORT = state.TCP_PORT;
        this.GAMES_BY_PLAYERTOKEN = new HashMap<>(state.GAMES_BY_PLAYERTOKEN);
        this.GAMES_BY_ID = new HashMap<>(state.GAMES_BY_ID);
    }
}
