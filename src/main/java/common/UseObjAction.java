package common;

import server_store.StoreAction;

/**
 * Represents the action of using an object card in the game
 * 
 * @see Action
 * @author Andrea Sessa
 * @author Giorgio Pea
 * @version 1.0
 */
public class UseObjAction extends StoreAction {

    public ObjectCard payload;
    public int gameId;

    public UseObjAction(ObjectCard objectCard, int gameId) {
        this.type = "@GAMEACTION_USE_OBJ_CARD";
        this.payload = objectCard;
        this.gameId = gameId;
    }
}
