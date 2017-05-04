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

    private final ObjectCard objectCard;

    public UseObjAction(ObjectCard objectCard) {
        super("@GAMEACTION_USE_OBJ_CARD");
        this.objectCard = objectCard;
    }

    public ObjectCard getObjectCard() {
        return objectCard;
    }
}
