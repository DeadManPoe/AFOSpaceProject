package common;

import server_store.StoreAction;

/**
 * Represents a moveToSector action combined with an attack
 * 
 * @author Andrea Sessa
 * @author Giorgio Pea
 * @version 1.0
 * @see Action
 */
public class MoveAttackAction extends StoreAction {

    private final Sector targetSector;

	public MoveAttackAction(Sector target) {
        super("@GAMEACTION_MOVE_ATTACK");
        this.targetSector= target;
	}

    public Sector getTargetSector() {
        return targetSector;
    }
}
