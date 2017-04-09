package client_store_actions;

import server_store.StoreAction;

/**
 * Created by giorgiopea on 27/03/17.
 */
public class ClientAskAttackAction extends StoreAction {

    public final boolean toBeAsked;

    public ClientAskAttackAction(boolean toBeAsked) {
        super("@CLIENT_ASK_FOR_SECTOR_TO_ATTACK");
        this.toBeAsked = toBeAsked;
    }
}
