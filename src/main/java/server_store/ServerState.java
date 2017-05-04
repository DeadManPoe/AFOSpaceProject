package server_store;

import server.Game;
import server.PubSubHandler;
import server.ReqRespHandler;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by giorgiopea on 11/03/17.
 *
 */
public class ServerState extends State implements Serializable {

    private List<Game> games;
    private List<ReqRespHandler> reqRespHandlers;
    private List<PubSubHandler> pubSubHandlers;
    private int tcpPort;
    private long turnTimeout;
    private boolean isServerListening;

    public ServerState() {
        this.games = new ArrayList<>();
        this.reqRespHandlers = new ArrayList<>();
        this.pubSubHandlers = new ArrayList<>();
        this.tcpPort = 29999;
        this.turnTimeout = 5*60*1000;
        this.isServerListening = true;
    }

    public boolean isServerListening() {
        return isServerListening;
    }

    public void setServerListening(boolean serverListening) {
        isServerListening = serverListening;
    }

    public List<Game> getGames() {
        return games;
    }

    public void setGames(List<Game> games) {
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

    public Integer getTcpPort() {
        return tcpPort;
    }

    public void setTcpPort(int tcpPort) {
        this.tcpPort = tcpPort;
    }

    public long getTurnTimeout() {
        return turnTimeout;
    }

}
