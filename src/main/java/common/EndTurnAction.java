package common;

import server_store.StoreAction;

/**
 * Represents the action of ending a turn in the game
 * 
 * @author Andrea Sessa
 * @author Giorgio Pea
 * @version 1.0
 */
public class EndTurnAction extends StoreAction {

    public EndTurnAction() {
        super("@GAMEACTION_END_TURN");
    }
}
