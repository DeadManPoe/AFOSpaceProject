package client_store_actions;

import server_store.StoreAction;

/**
 * Created by giorgiopea on 09/04/17.
 *
 */
public class ClientRemovePubSubHandlerAction extends StoreAction {

    public ClientRemovePubSubHandlerAction() {
        super("@COMMUNICATION_REMOVE_PUBSUB_HANDLER");
    }
}
