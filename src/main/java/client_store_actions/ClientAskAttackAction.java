package client_store_actions;

import server_store.StoreAction;

/**
 * Created by giorgiopea on 27/03/17.
 */
public class ClientAskAttackAction extends StoreAction {

    public boolean payload;

    public ClientAskAttackAction(boolean askAttack) {
        this.type = "@CLIENT_ASK_ATTACK";
        this.payload = askAttack;
    }
}
