package client_store_actions;

import common.ObjectCard;
import server_store.StoreAction;

/**
 * Created by giorgiopea on 07/04/17.
 */
public class ClientDiscardObjectCardAction extends StoreAction {

    public final ObjectCard discardedObjectCard;
    public final boolean isActionServerValidated;

    public ClientDiscardObjectCardAction(ObjectCard objectCard, boolean isActionServerValidated) {
        super("@CLIENT_DISCARD_OBJECT_CARD");
        this.discardedObjectCard = objectCard;
        this.isActionServerValidated = isActionServerValidated;
    }
}
