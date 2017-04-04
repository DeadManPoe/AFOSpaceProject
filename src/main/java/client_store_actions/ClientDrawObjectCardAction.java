package client_store_actions;

import common.Card;
import common.ObjectCard;
import server_store.StoreAction;

/**
 * Created by giorgiopea on 04/04/17.
 */
public class ClientDrawObjectCardAction extends StoreAction {

    public ObjectCard drawnObjectCard;

    public ClientDrawObjectCardAction(ObjectCard objectCard) {
        this.type = "@CLIENT_DRAW_OBJ_CARD";
        this.drawnObjectCard = objectCard;
    }
}
