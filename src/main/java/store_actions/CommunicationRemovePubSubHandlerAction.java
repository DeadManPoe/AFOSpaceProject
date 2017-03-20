package store_actions;

import server_store.StoreAction;

/**
 * Created by giorgiopea on 17/03/17.
 */
public class CommunicationRemovePubSubHandlerAction extends StoreAction {

    private Integer payload;

    public CommunicationRemovePubSubHandlerAction(Integer gameId) {
        this.type = "@COMMUNICATION_REMOVE_PUBSUB_HANDLER";
        this.payload = gameId;
    }

    public Integer getPayload() {
        return payload;
    }
}
