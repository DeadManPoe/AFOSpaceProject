package client_store_actions;

import server_store.StoreAction;

/**
 * Created by giorgiopea on 04/04/17.
 */
public class ClientConnectRetrieveGamesAction extends StoreAction {

    public boolean payload;

    public ClientConnectRetrieveGamesAction(boolean toStart) {
        this.type = "@CLIENT_CONNECT_RETRIEVE_GAMES";
        this.payload = toStart;
    }
}
