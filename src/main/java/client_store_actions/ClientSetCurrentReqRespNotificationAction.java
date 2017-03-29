package client_store_actions;

import common.RRClientNotification;
import server_store.StoreAction;

/**
 * Created by giorgiopea on 29/03/17.
 */
public class ClientSetCurrentReqRespNotificationAction extends StoreAction {

    public RRClientNotification payload;

    public ClientSetCurrentReqRespNotificationAction(RRClientNotification rrClientNotification) {
        this.type = "@CLIENT_SET_CURRENT_REQRESP_NOTIFICATION";
        this.payload = rrClientNotification;
    }
}
