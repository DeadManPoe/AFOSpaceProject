package common;

import server_store.StoreAction;

/**
 * Represents the action of discarding an object card in the game
 * 
 * @see Action
 * @author Andrea Sessa
 * @author Giorgio Pea
 * @version 1.0
 */
public class DiscardAction extends StoreAction {

    public ObjectCard payload;

    public DiscardAction(ObjectCard objectCard) {
        super("@GAMEACTION_DISCARD_OBJ_CARD");
        this.payload = objectCard;
    }
}
