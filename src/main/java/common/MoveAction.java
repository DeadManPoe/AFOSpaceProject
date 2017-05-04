package common;

import server_store.StoreAction;

/**
 * Represents a moveToSector action in the game
 * 
 * @author Andrea Sessa
 * @author Giorgio Pea
 */
public class MoveAction extends StoreAction {

    private final Sector targetSector;

    public MoveAction(Sector target) {
        super("@GAMEACTION_MOVE");
        this.targetSector = target;
    }

    public Sector getTargetSector() {
        return targetSector;
    }
}
