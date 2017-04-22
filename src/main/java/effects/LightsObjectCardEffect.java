package effects;

import java.util.List;

import common.*;
import it.polimi.ingsw.cg_19.Game;
import it.polimi.ingsw.cg_19.Player;

/**
 * This class represents the effect of a lights effect
 * 
 * @see ObjectCardEffect
 * @see LightsObjectCard
 * @author Andrea Sessa
 * @author Giorgio Pea
 * @version 1.1
 */
public class LightsObjectCardEffect extends ObjectCardEffect {
	public static boolean executeEffect(Game game,
								 RRClientNotification rrNotification,
								 PSClientNotification psNotification, ObjectCard objectCard) {
		LightsObjectCard lightsObjectCard = (LightsObjectCard) objectCard;
		List<Sector> neighboorSectors = game.getMap().getSearchableGraph()
				.neighborListOf(lightsObjectCard.getTarget());
		String playerName;
		String globalMessage = "\n[GLOBAL MESSAGE]: Players spotted:";
		for (Sector sector : neighboorSectors) {
			for (Player player : sector.getPlayers()) {
				playerName = player.getName();
				globalMessage += " " + playerName;
			}
			rrNotification.addSector(sector);
		}
		if (globalMessage.equals("\n[GLOBAL MESSAGE]: Players spotted:"))
			globalMessage += " none";
		psNotification.setMessage(psNotification.getMessage() + globalMessage);
		return true;
	}
}
