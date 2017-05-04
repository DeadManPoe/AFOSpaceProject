package client;

import common.*;
import common.GameMap;
import common.Player;
import server_store.State;

import java.io.Serializable;
import java.util.List;

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
    public StatefulTimer gamePollingTimer;
    public String lastChatMessage;
    public PSClientNotification currentPubSubNotification;
    public long delayReturnToGameList;
    public boolean connectionActive;
    public SectorCard drawnSectorCard;
    public boolean aliensHaveWon;
    public boolean humansHaveWon;

    public ClientState() {
        this.tcpPort = 29999;
        this.host = "localhost";
        this.delayReturnToGameList = 10000;
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
                ", delayReturnToGameList=" + delayReturnToGameList +
                ", connectionActive=" + connectionActive +
                ", drawnSectorCard=" + drawnSectorCard +
                ", aliensHaveWon=" + aliensHaveWon +
                ", humansHaveWon=" + humansHaveWon +
                '}';
    }
}
