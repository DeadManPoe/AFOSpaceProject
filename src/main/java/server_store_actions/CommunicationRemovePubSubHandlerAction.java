package server_store_actions;

import server.PubSubHandler;
import server_store.StoreAction;

/**
 * Created by giorgiopea on 17/03/17.
 */
public class CommunicationRemovePubSubHandlerAction extends StoreAction {

    private final PubSubHandler handler;

    public CommunicationRemovePubSubHandlerAction(PubSubHandler handler) {
        super("@COMMUNICATION_REMOVE_PUBSUB_HANDLER");
        this.handler =  handler;
    }

    public PubSubHandler getHandler() {
        return handler;
    }
}
