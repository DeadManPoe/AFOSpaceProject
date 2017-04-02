package effects;

import java.util.ArrayList;
import java.util.List;

import common.*;
import server_store.Game;
import server_store.Player;

/**
 * This class represents the effect of a lights effect
 * 
 * @see ObjectCardEffect
 * @see LightsObjectCard
 * @author Andrea Sessa
 * @author Giorgio Pea
 * @version 1.1
 */
public class LightObjectCardEffect extends ObjectCardEffect {
	public static boolean executeEffect(Game game, ObjectCard objectCard) {
		LightsObjectCard castedObjectCard = (LightsObjectCard) objectCard;
		Sector targetSector = castedObjectCard.getTarget();
		List<Sector> neighboorSectors = game.gameMap.getSearchableGraph()
				.neighborListOf(targetSector);
		neighboorSectors.add(targetSector);
		List<Sector> incriminatedSectors = new ArrayList<>();
		String globalMessage = "\n[GLOBAL MESSAGE]: Players spotted:";
        String msg = "";
		for (Sector sector : neighboorSectors) {
			if (!sector.getPlayers().isEmpty()){
                incriminatedSectors.add(sector);
                game.lastRRclientNotification.addSector(sector);
            }
		}
		for (Sector sector : incriminatedSectors){
            for (Player player : sector.getPlayers()){
                if (msg.equals("")){
                    msg += player.name + " in sector "+sector.getCoordinate().toString();
                }
                else {
                    msg += ", "+player.name + " in sector "+sector.getCoordinate().toString();
                }

            }
        }
		if ( msg.equals("")){
            msg = "none";
        }
        game.lastRRclientNotification.setMessage(game.lastRRclientNotification.getMessage()+". Players spotted: "+msg);
		game.lastPSclientNotification.setMessage(game.lastPSclientNotification.getMessage() + globalMessage+msg);
		return true;
	}
}
