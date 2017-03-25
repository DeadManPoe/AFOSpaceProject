package client_store_actions;

import common.ObjectCard;
import server_store.StoreAction;

/**
 * Created by giorgiopea on 25/03/17.
 */
public class ClientRemoveObjCardAction extends StoreAction {

    public ObjectCard payload;

    public ClientRemoveObjCardAction(ObjectCard card) {
        this.type = "@CLIENT_REMOVE_OBJ_CARD";
        this.payload = card;
    }
}
