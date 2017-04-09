package client_store_actions;

import server_store.StoreAction;

/**
 * Created by giorgiopea on 27/03/17.
 */
public class ClientAskSectorToLightAction extends StoreAction {

    public final boolean toBeAsked;

    public ClientAskSectorToLightAction(boolean toBeAsked) {
        super("@CLIENT_ASK_SECTOR_TO_LIGHT");
        this.toBeAsked = toBeAsked;
    }
}
