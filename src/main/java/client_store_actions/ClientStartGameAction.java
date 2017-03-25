package client_store_actions;

import server_store.StoreAction;

/**
 * Created by giorgiopea on 25/03/17.
 */
public class ClientStartGameAction extends StoreAction {

    public ClientStartGameAction() {
        this.type = "@CLIENT_START_GAME";
    }
}
