package client_store_actions;

import common.ObjectCard;
import server_store.StoreAction;

/**
 * Created by giorgiopea on 06/04/17.
 */
public class ClientSetDrawnObjectCardAction extends StoreAction {

    public final ObjectCard drawnObjectCard;

    public ClientSetDrawnObjectCardAction(ObjectCard objectCard) {
        this.type = "@CLIENT_SET_DRAWN_OBJECT_CARD";
        this.drawnObjectCard = objectCard;
    }
}
