package client_store_actions;

import server_store.StoreAction;

/**
 * Created by giorgiopea on 30/03/17.
 */
public class ClientSuppressAction extends StoreAction {

    private final boolean isSedated;

    public ClientSuppressAction(boolean isSedated) {
        super("@CLIENT_SUPPRESS");
        this.isSedated = isSedated;
    }

    public boolean isSedated() {
        return isSedated;
    }
}
