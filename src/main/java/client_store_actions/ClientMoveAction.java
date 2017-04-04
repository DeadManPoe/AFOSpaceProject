package client_store_actions;

import common.Sector;
import server_store.StoreAction;

/**
 * Created by giorgiopea on 25/03/17.
 */
public class ClientMoveAction extends StoreAction {

    public Sector targetSector;
    public boolean succesfully;

    public ClientMoveAction(Sector targetSector, boolean succesfully) {
        this.type = "@CLIENT_MOVE_TO_SECTOR";
        this.targetSector = targetSector;
        this.succesfully = succesfully;
    }
}
