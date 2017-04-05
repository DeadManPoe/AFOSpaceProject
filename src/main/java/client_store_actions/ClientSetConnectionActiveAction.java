package client_store_actions;

import server_store.StoreAction;

/**
 * Created by giorgiopea on 05/04/17.
 */
public class ClientSetConnectionActiveAction extends StoreAction {

    public boolean isConnectionActive;

    public ClientSetConnectionActiveAction(boolean isConnectionActive) {
        this.type = "@CLIENT_SET_CONNECTION_ACTIVE";
        this.isConnectionActive = isConnectionActive;
    }
}
