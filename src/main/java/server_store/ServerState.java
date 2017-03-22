package server_store;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by giorgiopea on 11/03/17.
 *
 */
public class ServerState implements Serializable {

    private List<server_store.Game> games;
    private List<ReqRespHandler> reqRespHandlers;
    private List<PubSubHandler> pubSubHandlers;
    private Integer tcp_port;
    private Integer turnTimeout;


    public ServerState() {
        this.games = new ArrayList<>();
        this.reqRespHandlers = new ArrayList<>();
        this.pubSubHandlers = new ArrayList<>();
        this.tcp_port = null;
        this.turnTimeout = 10*60*1000;
    }

    public List<server_store.Game> getGames() {
        return games;
    }

    public void setGames(List<server_store.Game> games) {
        this.games = games;
    }

    public List<ReqRespHandler> getReqRespHandlers() {
        return reqRespHandlers;
    }

    public void setReqRespHandlers(List<ReqRespHandler> reqRespHandlers) {
        this.reqRespHandlers = reqRespHandlers;
    }

    public List<PubSubHandler> getPubSubHandlers() {
        return pubSubHandlers;
    }

    public void setPubSubHandlers(List<PubSubHandler> pubSubHandlers) {
        this.pubSubHandlers = pubSubHandlers;
    }

    public Integer getTcp_port() {
        return tcp_port;
    }

    public void setTcp_port(Integer tcp_port) {
        this.tcp_port = tcp_port;
    }

    public Integer getTurnTimeout() {
        return turnTimeout;
    }

    @Override
    public String toString() {
        return "ServerState{" +
                "games=" + games +
                ", reqRespHandlers=" + reqRespHandlers +
                ", pubSubHandlers=" + pubSubHandlers +
                ", tcp_port=" + tcp_port +
                '}';
    }
}
