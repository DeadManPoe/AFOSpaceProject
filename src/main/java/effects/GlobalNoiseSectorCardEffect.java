package effects;

import common.*;
import it.polimi.ingsw.cg_19.Game;

/**
 * Represents the effect associated with a global noise sector card
 *
 */
public class GlobalNoiseSectorCardEffect extends SectorCardEffect {

	public static boolean executeEffect(Game game,
										RRClientNotification rrNotification,
										PSClientNotification psNotification, SectorCard sectorCard) {
		// Notify all the player
		String name = game.getCurrentPlayer().getName();
		Sector target = ((GlobalNoiseSectorCard) sectorCard).getSector();
		rrNotification.setMessage("You've indicated the sector: "
				+ target.getCoordinate().toString());
		psNotification.setMessage("[GLOBAL MESSAGE]: "
				+ psNotification.getMessage() + name
				+ " has made noise in sector "
				+ target.getCoordinate().toString());
		return true;
	}
}