package client_store_actions;

import server_store.StoreAction;

/**
 * Created by giorgiopea on 28/03/17.
 */
public class ClientStartTurnAction extends StoreAction {

    public ClientStartTurnAction() {
        this.type = "@CLIENT_START_TURN";
    }
}
