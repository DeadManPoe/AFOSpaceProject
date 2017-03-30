package client_store_actions;

import server_store.StoreAction;

/**
 * Created by giorgiopea on 30/03/17.
 */
public class ClientSuppressAction extends StoreAction {

    public boolean payload;

    public ClientSuppressAction(boolean isSuppressed) {
        this.type = "@CLIENT_SUPPRESS";
        this.payload = isSuppressed;
    }
}
