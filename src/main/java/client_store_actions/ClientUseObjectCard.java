package client_store_actions;

import common.ObjectCard;
import server_store.StoreAction;

/**
 * Created by giorgiopea on 25/03/17.
 */
public class ClientUseObjectCard extends StoreAction {

    public ObjectCard objectCard;

    public ClientUseObjectCard(ObjectCard objectCard) {
        this.type = "@CLIENT_USE_OBJECT_CARD";
        this.objectCard = objectCard;
    }
}
