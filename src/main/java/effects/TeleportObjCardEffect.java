package effects;

import common.*;
import server.Game;

/**
 * Represents the effect of a teleport object card
 * 
 * @see ObjectCardEffect
 * @see TeleportObjectCard
 * @author Andrea Sessa
 * @author Giorgio Pea
 * @version 1.0
 */
public class TeleportObjCardEffect extends ObjectCardEffect {

	public static boolean executeEffect(Game game, ObjectCard card) {
		GameMap map = game.getGameMap();
		Player curr = game.getCurrentPlayer();
		Sector humanSector = map.getHumanSector();

		// Move the player(can be only human) to the starting sector
		curr.getCurrentSector().removePlayer(curr);
		curr.setCurrentSector(humanSector);
		humanSector.addPlayer(curr);
		game.getLastRRclientNotification().setMessage("You've teleported to the human sector");
		game.getLastPSclientNotification()
				.setMessage(game.getLastPSclientNotification().getMessage()
						+ "\n[GLOBAL MESSAGE]: He/She will be teleported to the human sector");
		return true;
	}
}
