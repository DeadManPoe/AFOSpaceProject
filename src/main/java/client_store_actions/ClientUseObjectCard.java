package client_store_actions;

import common.ObjectCard;
import server_store.StoreAction;

/**
 * Created by giorgiopea on 25/03/17.
 */
public class ClientUseObjectCard extends StoreAction {

    private final ObjectCard objectCard;

    public ClientUseObjectCard(ObjectCard objectCard) {
        super("@CLIENT_USE_OBJECT_CARD");
        this.objectCard = objectCard;
    }

    public ObjectCard getObjectCard() {
        return objectCard;
    }
}
