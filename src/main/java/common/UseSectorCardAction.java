package common;

import server_store.StoreAction;

/**
 * Represents the action of using a sector card in the game
 * 
 * @author Andrea Sessa
 * @author Giorgio Pea
 * @version 1.0
 */
public class UseSectorCardAction extends StoreAction {

    public SectorCard payload;

    public UseSectorCardAction(SectorCard sectorCard) {
        super("@GAMEACTION_USE_SECTOR_CARD");
        this.payload = sectorCard;
    }
}
