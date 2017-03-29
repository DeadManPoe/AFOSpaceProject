package client_store_actions;

import common.PSClientNotification;
import server_store.StoreAction;

/**
 * Created by giorgiopea on 29/03/17.
 */
public class ClientSetCurrentPubSubNotificationAction extends StoreAction {

    public PSClientNotification payload;

    public ClientSetCurrentPubSubNotificationAction(PSClientNotification notification) {
        this.type = "@CLIENT_SET_CURRENT_PUBSUB_NOTIFICATION";
        this.payload = notification;
    }
}
