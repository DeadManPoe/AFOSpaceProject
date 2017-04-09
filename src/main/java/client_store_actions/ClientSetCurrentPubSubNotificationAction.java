package client_store_actions;

import common.PSClientNotification;
import server_store.StoreAction;

/**
 * Created by giorgiopea on 29/03/17.
 */
public class ClientSetCurrentPubSubNotificationAction extends StoreAction {

    public final PSClientNotification psNotification;

    public ClientSetCurrentPubSubNotificationAction(PSClientNotification psNotification) {
        super("@CLIENT_SET_CURRENT_PUBSUB_NOTIFICATION");
        this.psNotification = psNotification;
    }
}
