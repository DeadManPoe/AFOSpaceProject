package server_store;

import it.polimi.ingsw.cg_19.Game;
import server.ServerConnection;

import java.net.Socket;
import java.util.Map;

/**
 * Created by giorgiopea on 11/03/17.
 */
public class ServerState {

    public Integer TCP_PORT;
    public Map<Integer, Socket> REQ_RESP_SOCKETS_BY_HASHCODE;
    public Map<Integer, Socket> PUB_SUB_SOCKETS_BY_HASHCODE;
    public Map<Integer, Game> GAMES_BY_ID;

    public ServerState(Integer TCP_PORT, Map<Integer, Socket> REQ_RESP_SOCKETS_BY_HASHCODE, Map<Integer, Socket> PUB_SUB_SOCKETS_BY_HASHCODE, Map<Integer, Game> GAMES_BY_ID) {
        this.TCP_PORT = TCP_PORT;
        this.REQ_RESP_SOCKETS_BY_HASHCODE = REQ_RESP_SOCKETS_BY_HASHCODE;
        this.PUB_SUB_SOCKETS_BY_HASHCODE = PUB_SUB_SOCKETS_BY_HASHCODE;
        this.GAMES_BY_ID = GAMES_BY_ID;
    }
}
