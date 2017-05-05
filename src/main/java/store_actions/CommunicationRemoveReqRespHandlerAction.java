package store_actions;

import server.ReqRespHandler;
import server_store.StoreAction;

import java.util.UUID;

/**
 * Created by giorgiopea on 17/03/17.
 */
public class CommunicationRemoveReqRespHandlerAction extends StoreAction {

    private final ReqRespHandler handler;
    public CommunicationRemoveReqRespHandlerAction(ReqRespHandler handler) {
        super("@COMMUNICATION_REMOVE_REQRESP_HANDLER");
        this.handler = handler;
    }

    public ReqRespHandler getHandler() {
        return handler;
    }
}
