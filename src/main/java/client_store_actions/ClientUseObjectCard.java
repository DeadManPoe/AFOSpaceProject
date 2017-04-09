package client_store_actions;

import common.ObjectCard;
import server_store.StoreAction;

/**
 * Created by giorgiopea on 25/03/17.
 */
public class ClientUseObjectCard extends StoreAction {

    public final ObjectCard objectCard;
    public final boolean isServerValidated;

    public ClientUseObjectCard(ObjectCard objectCard, boolean isServerValidated) {
        super("@CLIENT_USE_OBJECT_CARD");
        this.objectCard = objectCard;
        this.isServerValidated = isServerValidated;
    }
}
