package client_store_actions;

import server_store.StoreAction;

/**
 * Created by giorgiopea on 28/03/17.
 */
public class ClientSetCurrentChatMessage extends StoreAction {
    private final String message;
    public ClientSetCurrentChatMessage(String msg) {
        super("@CLIENT_PUBLISH_CHAT_MSG");
        this.message = msg;
    }

    public String getMessage() {
        return message;
    }
}
