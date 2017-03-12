package server_store;

import common.PlayerToken;
import common.RemoteMethodCall;

/**
 * Created by giorgiopea on 12/03/17.
 */
public class SubscriberNotification {

    public RemoteMethodCall remoteMethodCall;
    public Integer gameId;

    public SubscriberNotification(RemoteMethodCall remoteMethodCall, Integer gameId) {
        this.remoteMethodCall = remoteMethodCall;
        this.gameId = gameId;
    }
}
