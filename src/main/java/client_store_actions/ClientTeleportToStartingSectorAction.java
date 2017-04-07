package client_store_actions;

import server_store.StoreAction;

/**
 * Created by giorgiopea on 25/03/17.
 */
public class ClientTeleportToStartingSectorAction extends StoreAction {

    public ClientTeleportToStartingSectorAction() {
        this.type = "@CLIENT_TELEPORT_TO_STARTING_SECTOR";
    }
}
