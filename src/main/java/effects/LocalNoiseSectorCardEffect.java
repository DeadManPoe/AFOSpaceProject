package effects;

import common.LocalNoiseSectorCard;
import common.Sector;
import common.SectorCard;
import server.Game;

/**
 * Represents the effect associated with a local noise sector card
 * 
 * @see SectorCardEffect
 * @see LocalNoiseSectorCard
 * @author Andrea Sessa
 * @author Giorgio Pea
 */
public class LocalNoiseSectorCardEffect extends SectorCardEffect {

	public static boolean executeEffect(Game game, SectorCard card) {
		// The local noise effect could be seen as a global noise effect with a
		// sector that is automatically
		// indicated
		// Notify all the player
		String name = game.getCurrentPlayer().getName();
		Sector target = game.getCurrentPlayer().getCurrentSector();
		game.getLastPSclientNotification().setMessage(game.getLastPSclientNotification().getMessage()
				+ "\n[GLOBAL MESSAGE]: " + name
				+ " has made noise in sector "
				+ target.getCoordinate().toString());
		return true;
	}
}
