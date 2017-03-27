package client_store;

import common.GamePublicData;
import common.PlayerToken;
import common.RRClientNotification;
import it.polimi.ingsw.cg_19.GameMap;
import server_store.Player;
import client_store.PubSubHandler;
import server_store.ReqRespHandler;
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
    public RRClientNotification currentReqRespNotification;
    public boolean askLights;
    public boolean askAttack;
}
