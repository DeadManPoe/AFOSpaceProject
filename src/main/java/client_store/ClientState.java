package client_store;

import common.GamePublicData;
import common.PlayerToken;
import server_store.Player;
import server_store.PubSubHandler;
import server_store.ReqRespHandler;

import java.io.Serializable;
import java.util.List;

/**
 * Created by giorgiopea on 24/03/17.
 */
public class ClientState implements Serializable {

    public int tcpPort;
    public PubSubHandler currentPubSubHandler;
    public ReqRespHandler currentReqRespHandler;
    public List<GamePublicData> availableGames;
    public Player player;
    public String host;

}
