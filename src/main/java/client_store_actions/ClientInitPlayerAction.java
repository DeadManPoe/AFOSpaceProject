package client_store_actions;

import server_store.StoreAction;

/**
 * Created by giorgiopea on 25/03/17.
 */
public class ClientInitPlayerAction extends StoreAction {

    public final String payload;

    public ClientInitPlayerAction(String playerName) {
        this.type = "@CLIENT_INIT_PLAYER";
        this.payload = playerName;
    }
}
