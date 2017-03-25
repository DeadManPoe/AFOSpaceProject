package client_store_actions;

import common.Sector;
import server_store.StoreAction;

/**
 * Created by giorgiopea on 25/03/17.
 */
public class ClientMoveAction extends StoreAction {

    public Sector payload;

    public ClientMoveAction(Sector targetSector) {
        this.type = "@CLIENT_MOVE_TO_SECTOR";
        this.payload = targetSector;
    }
}
