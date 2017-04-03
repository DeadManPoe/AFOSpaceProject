package client_store_actions;

import server_store.StoreAction;

/**
 * Created by giorgiopea on 03/04/17.
 */
public class ClientSetConnectionStatusAction extends StoreAction {

    public boolean payload;

    public ClientSetConnectionStatusAction(boolean connectionStatus) {
        this.type = "@CLIENT_SET_CONNECTION_STATUS";
        this.payload = connectionStatus;
    }
}
