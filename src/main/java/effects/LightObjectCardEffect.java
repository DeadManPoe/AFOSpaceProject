package effects;

import java.util.List;

import common.*;
import server_store.Game;

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
		List<Sector> neighboorSectors = game.gameMap.getSearchableGraph()
				.neighborListOf(((LightsObjectCard) objectCard).getTarget());
		String playerName;
		String globalMessage = "\n[GLOBAL MESSAGE]: Players spotted:";
		for (Sector sector : neighboorSectors) {
			for (server_store.Player player : sector.getPlayers()) {
				playerName = player.name;
				globalMessage += " " + playerName;
			}
			game.lastRRclientNotification.addSector(sector);
		}
		if (globalMessage.equals("\n[GLOBAL MESSAGE]: Players spotted:"))
			globalMessage += " none";
		game.lastPSclientNotification.setMessage(game.lastPSclientNotification.getMessage() + globalMessage);
		return true;
	}
}
