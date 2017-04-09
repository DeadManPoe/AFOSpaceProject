package client;

import common.*;
import it.polimi.ingsw.cg_19.GameMap;
import server_store.Player;
import client.PubSubHandler;
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
    public boolean connectionActive;
    public SectorCard drawnSectorCard;
    public boolean aliensHaveWon;
    public boolean humansHaveWon;

    public ClientState() {
        this.tcpPort = 29999;
        this.host = "localhost";
        this.player = new Player(null,null);
        this.gameListPollingPeriod = 10000;
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
                ", startableGame=" + startableGame +
                ", currentReqRespNotification=" + currentReqRespNotification +
                ", askLights=" + askLights +
                ", askAttack=" + askAttack +
                ", gamePollingTimer=" + gamePollingTimer +
                ", lastChatMessage='" + lastChatMessage + '\'' +
                ", currentPubSubNotification=" + currentPubSubNotification +
                ", gameListPollingPeriod=" + gameListPollingPeriod +
                ", connectionActive=" + connectionActive +
                ", drawnSectorCard=" + drawnSectorCard +
                ", aliensHaveWon=" + aliensHaveWon +
                ", humansHaveWon=" + humansHaveWon +
                '}';
    }
}
