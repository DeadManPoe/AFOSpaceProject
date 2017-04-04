package client_store_actions;

import common.Card;
import common.Sector;
import common.SectorCard;
import server_store.StoreAction;

/**
 * Created by giorgiopea on 04/04/17.
 */
public class ClientDrawSectorCardAction extends StoreAction {

    public SectorCard currentSectorCard;
    public String cardIdentifier;

    public ClientDrawSectorCardAction(SectorCard sectorCard, String cardIdentifier) {
        this.type = "@CLIENT_DRAW_SECTOR_CARD";
        this.currentSectorCard = sectorCard;
        this.cardIdentifier = cardIdentifier;
    }
}
