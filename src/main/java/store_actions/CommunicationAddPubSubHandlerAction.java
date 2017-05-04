package store_actions;

import server.PubSubHandler;
import server_store.StoreAction;

/**
 * Created by giorgiopea on 14/03/17.
 */
public class CommunicationAddPubSubHandlerAction extends StoreAction {

    private final PubSubHandler pubSubHandler;

    public CommunicationAddPubSubHandlerAction(PubSubHandler pubSubHandler) {
        super("@COMMUNICATION_ADD_PUBSUB_HANDLER");
        this.pubSubHandler = pubSubHandler;
    }

    public PubSubHandler getPubSubHandler() {
        return this.pubSubHandler;
    }
}
