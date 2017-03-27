package client_store_actions;

import server_store.StoreAction;

/**
 * Created by giorgiopea on 27/03/17.
 */
public class ClientAskLightsAction extends StoreAction {

    public boolean payload;

    public ClientAskLightsAction(boolean ask) {
        this.type = "@CLIENT_ASK_LIGHTS";
        this.payload = ask;
    }
}
