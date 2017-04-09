package common;

import server_store.StoreAction;

/**
 * Represents the action of drawing a sector card from the sector card deck
 * 
 * @author Andrea Sessa
 * @author Giorgio Pea
 * @version 1.0
 */
public class DrawSectorCardAction extends StoreAction {

	public DrawSectorCardAction() {
        super("@GAMEACTION_DRAW_SECTOR_CARD");
	}
}
