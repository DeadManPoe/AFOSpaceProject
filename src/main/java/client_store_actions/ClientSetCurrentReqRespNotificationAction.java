package client_store_actions;

import common.RRClientNotification;
import server_store.StoreAction;

/**
 * Created by giorgiopea on 29/03/17.
 */
public class ClientSetCurrentReqRespNotificationAction extends StoreAction {

    private final RRClientNotification rrClientNotification;

    public ClientSetCurrentReqRespNotificationAction(RRClientNotification rrClientNotification) {
        super("@CLIENT_SET_CURRENT_REQRESP_NOTIFICATION");
        this.rrClientNotification = rrClientNotification;
    }

    public RRClientNotification getRrClientNotification() {
        return rrClientNotification;
    }
}
