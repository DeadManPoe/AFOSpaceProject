package store_actions;

import server_store.StoreAction;

import java.util.UUID;

/**
 * Created by giorgiopea on 17/03/17.
 */
public class CommunicationRemoveReqRespHandlerAction extends StoreAction {

    private UUID payload;

    public CommunicationRemoveReqRespHandlerAction(UUID handlerUUID) {
        super("@COMMUNICATION_REMOVE_REQRESP_HANDLER");
        this.payload = handlerUUID;
    }

    public UUID getPayload() {
        return payload;
    }
}
