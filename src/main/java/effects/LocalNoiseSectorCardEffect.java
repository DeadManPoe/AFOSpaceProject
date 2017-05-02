package effects;

import common.*;
import it.polimi.ingsw.cg_19.Game;

/**
 * Represents the effect associated with a local noise sector card
 *
 */
public class LocalNoiseSectorCardEffect extends SectorCardEffect {
	public static boolean executeEffect(Game game,
								 RRClientNotification rrNotification,
								 PSClientNotification psNotification, SectorCard sectorCard) {
		// The local noise effect could be seen as a global noise effect with a
		// sector that is automatically
		// indicated
		// Notify all the player
		String name = game.getCurrentPlayer().getName();
		Sector target = game.getCurrentPlayer().getCurrentSector();
		psNotification.setMessage(psNotification.getMessage()
				+ "\n[GLOBAL MESSAGE]: " + name
				+ " has made noise in sector "
				+ target.getCoordinate().toString());
		return true;
	}
}
