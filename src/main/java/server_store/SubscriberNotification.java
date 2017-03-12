package server_store;

import common.PlayerToken;
import common.RemoteMethodCall;

/**
 * Created by giorgiopea on 12/03/17.
 */
public class SubscriberNotification {

    public PlayerToken playerToken;
    public RemoteMethodCall remoteMethodCall;
    public Integer gameId;
    public boolean isGlobal;

    public SubscriberNotification(PlayerToken playerToken, RemoteMethodCall remoteMethodCall, Integer gameId, boolean isGlobal) {
        this.playerToken = playerToken;
        this.remoteMethodCall = remoteMethodCall;
        this.gameId = gameId;
        this.isGlobal = isGlobal;
    }
}
