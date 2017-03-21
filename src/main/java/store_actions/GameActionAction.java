package store_actions;

import server_store.StoreAction;

/**
 * Created by giorgiopea on 21/03/17.
 */
public class GameActionAction extends StoreAction {

    public GameActionAction(StoreAction action) {
        this.type = action.getType();
    }
}
