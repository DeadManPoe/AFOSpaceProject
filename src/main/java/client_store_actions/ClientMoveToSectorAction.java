package client_store_actions;

import common.Sector;
import server_store.StoreAction;

/**
 * Created by giorgiopea on 25/03/17.
 */
public class ClientMoveToSectorAction extends StoreAction {

    public final Sector targetSector;
    public final boolean isServerValidated;

    public ClientMoveToSectorAction(Sector targetSector, boolean isServerValidated) {
        super("@CLIENT_MOVE_TO_SECTOR");
        this.targetSector = targetSector;
        this.isServerValidated = isServerValidated;
    }
}
