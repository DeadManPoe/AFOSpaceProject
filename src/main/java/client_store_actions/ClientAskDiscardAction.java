package client_store_actions;

import server_store.StoreAction;

/**
 * Created by giorgiopea on 04/04/17.
 */
public class ClientAskDiscardAction extends StoreAction {

    public boolean isAsking;

    public ClientAskDiscardAction(boolean toAsk) {
        this.type = "@CLIENT_ASK_DISCARD";
        this.isAsking = toAsk;
    }
}
