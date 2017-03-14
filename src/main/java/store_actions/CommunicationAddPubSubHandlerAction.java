package store_actions;

import server_store.PubSubHandler;

/**
 * Created by giorgiopea on 14/03/17.
 */
public class CommunicationAddPubSubHandlerAction extends StoreAction {

    private PubSubHandler payload;

    public CommunicationAddPubSubHandlerAction(PubSubHandler pubSubHandler) {
        this.type = "@COMMUNICATION_ADD_PUBSUB_HANDLER";
        this.payload = pubSubHandler;
    }

    public PubSubHandler getPayload() {
        return this.payload;
    }
}
