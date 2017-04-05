package client_store;

import common.GamePublicData;
import common.PSClientNotification;
import common.PlayerToken;
import common.RRClientNotification;
import it.polimi.ingsw.cg_19.GameMap;
import server_store.Player;
import client_store.PubSubHandler;
import server_store.ReqRespHandler;
import server_store.State;

import java.io.Serializable;
import java.util.List;
import java.util.Timer;

/**
 * Created by giorgiopea on 24/03/17.
 */
public class ClientState extends State implements Serializable {

    public int tcpPort;
    public PubSubHandler currentPubSubHandler;
    public List<GamePublicData> availableGames;
    public Player player;
    public String host;
    public GameMap gameMap;
    public boolean isGameStarted;
    public boolean isMyTurn;
    public boolean startableGame;
    public RRClientNotification currentReqRespNotification;
    public boolean askLights;
    public boolean askAttack;
    public Timer gamePollingTimer;
    public String lastChatMessage;
    public PSClientNotification currentPubSubNotification;
    public long gameListPollingPeriod;

    public ClientState() {
        this.tcpPort = 29999;
        this.host = "localhost";
        this.player = new Player(null,null,null);
        this.gameListPollingPeriod = 5000;
    }

    @Override
    public String toString() {
        return "ClientState{" +
                "tcpPort=" + tcpPort +
                ", currentPubSubHandler=" + currentPubSubHandler +
                ", availableGames=" + availableGames +
                ", player=" + player +
                ", host='" + host + '\'' +
                ", gameMap=" + gameMap +
                ", isGameStarted=" + isGameStarted +
                ", isMyTurn=" + isMyTurn +
                ", currentReqRespNotification=" + currentReqRespNotification +
                ", askLights=" + askLights +
                ", askAttack=" + askAttack +
                ", gamePollingTimer=" + gamePollingTimer +
                '}';
    }
}
