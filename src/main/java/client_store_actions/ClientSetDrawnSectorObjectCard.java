package client_store_actions;

import common.ObjectCard;
import common.SectorCard;
import server_store.StoreAction;

/**
 * Created by giorgiopea on 06/04/17.
 */
public class ClientSetDrawnSectorObjectCard extends StoreAction {

    public final SectorCard drawnSectorCard;
    public final ObjectCard drawnObjectCard;

    public ClientSetDrawnSectorObjectCard(SectorCard drawnSectorCard, ObjectCard drawnObjectCard) {
        this.type = "@CLIENT_SET_DRAWN_SECTOR_OBJECT_CARD";
        this.drawnSectorCard = drawnSectorCard;
        this.drawnObjectCard = drawnObjectCard;
    }
}