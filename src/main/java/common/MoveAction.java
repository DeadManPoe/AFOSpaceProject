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
    public int gameId;

    public MoveAction(Sector target, int gameId) {
        this.type = "@GAMEACTION_MOVE";
        this.payload = target;
        this.gameId = gameId;
    }
}
