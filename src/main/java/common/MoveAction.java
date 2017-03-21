package common;

import server_store.StoreAction;

/**
 * Represents a move action in the game
 * 
 * @author Andrea Sessa
 * @author Giorgio Pea
 */
public class MoveAction extends StoreAction {

    public Sector payload;

    public MoveAction(Sector target) {
        this.type = "@GAMEACTION_MOVE";
        this.payload = target;
    }
}
