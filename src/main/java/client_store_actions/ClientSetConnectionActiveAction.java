package client_store_actions;

import server_store.StoreAction;

/**
 * Created by giorgiopea on 05/04/17.
 */
public class ClientSetConnectionActiveAction extends StoreAction {

    private final boolean isConnectionActive;

    public ClientSetConnectionActiveAction(boolean isConnectionActive) {
        super("@CLIENT_SET_CONNECTION_ACTIVE");
        this.isConnectionActive = isConnectionActive;
    }

    public boolean isConnectionActive() {
        return isConnectionActive;
    }
}
