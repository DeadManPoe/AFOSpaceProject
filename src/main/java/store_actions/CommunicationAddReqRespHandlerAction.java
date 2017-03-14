package store_actions;

import server_store.ReqRespHandler;

/**
 * Created by giorgiopea on 14/03/17.
 */
public class CommunicationAddReqRespHandlerAction extends StoreAction {

    private ReqRespHandler payload;

    public CommunicationAddReqRespHandlerAction(ReqRespHandler reqRespHandler) {
        this.type = "@COMMUNICATION_ADD_REQRESP_HANDLER";
        this.payload = reqRespHandler;
    }

    public ReqRespHandler getPayload() {
        return payload;
    }
}
