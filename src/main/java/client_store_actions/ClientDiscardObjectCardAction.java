package client_store_actions;

import common.ObjectCard;
import server_store.StoreAction;

/**
 * Created by giorgiopea on 07/04/17.
 */
public class ClientDiscardObjectCardAction extends StoreAction {

    private final ObjectCard discardedObjectCard;

    public ClientDiscardObjectCardAction(ObjectCard objectCard) {
        super("@CLIENT_DISCARD_OBJECT_CARD");
        this.discardedObjectCard = objectCard;
    }

    public ObjectCard getDiscardedObjectCard() {
        return discardedObjectCard;
    }
}
