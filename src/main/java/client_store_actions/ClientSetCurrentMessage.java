package client_store_actions;

import server_store.StoreAction;

/**
 * Created by giorgiopea on 28/03/17.
 */
public class ClientSetCurrentMessage extends StoreAction {
    public String payload;
    public ClientSetCurrentMessage(String msg) {
        this.type = "@CLIENT_PUBLISH_MSG";
        this.payload = msg;
    }
}
