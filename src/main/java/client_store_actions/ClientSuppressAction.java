package client_store_actions;

import server_store.StoreAction;

/**
 * Created by giorgiopea on 30/03/17.
 */
public class ClientSuppressAction extends StoreAction {

    public final boolean isSuppressed;

    public ClientSuppressAction(boolean isSuppressed) {
        super("@CLIENT_SUPPRESS");
        this.isSuppressed = isSuppressed;
    }
}
