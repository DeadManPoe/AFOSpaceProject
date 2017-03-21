package common;

import server_store.StoreAction;

/**
 * Represents a move action combined with an attack
 * 
 * @author Andrea Sessa
 * @author Giorgio Pea
 * @version 1.0
 * @see Action
 */
public class MoveAttackAction extends StoreAction {

    public Sector payload;
    public int gameId;

	public MoveAttackAction(Sector target, int gameId) {
        this.type = "@GAMEACTION_MOVEATTACK";
        this.payload = target;
        this.gameId = gameId;
	}
}
