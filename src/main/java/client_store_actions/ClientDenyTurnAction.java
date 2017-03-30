package client_store_actions;

import server_store.StoreAction;

/**
 * Created by giorgiopea on 30/03/17.
 */
public class ClientDenyTurnAction extends StoreAction {

    public ClientDenyTurnAction() {
        this.type = "@CLIENT_DENY_TURN";
    }
}
