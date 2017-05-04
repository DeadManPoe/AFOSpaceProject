package store_actions;

import server.ReqRespHandler;
import server_store.StoreAction;

/**
 * Created by giorgiopea on 14/03/17.
 */
public class CommunicationAddReqRespHandlerAction extends StoreAction {

    private ReqRespHandler payload;

    public CommunicationAddReqRespHandlerAction(ReqRespHandler reqRespHandler) {
        super("@COMMUNICATION_ADD_REQRESP_HANDLER");
        this.payload = reqRespHandler;
    }

    public ReqRespHandler getPayload() {
        return payload;
    }
}
