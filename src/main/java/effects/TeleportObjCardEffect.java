package effects;

import common.*;
import it.polimi.ingsw.cg_19.Game;
import it.polimi.ingsw.cg_19.GameMap;
import it.polimi.ingsw.cg_19.Player;

/**
 * Represents the effect of a teleport object card
 *
 */
public class TeleportObjCardEffect extends ObjectCardEffect {

	public static boolean executeEffect(Game game,
										RRClientNotification rrNotification,
										PSClientNotification psNotification, ObjectCard objectCard) {
		GameMap map = game.getMap();
		Player curr = game.getCurrentPlayer();
		Sector humanSector = map.getHumanSector();

		// Move the player(can be only human) to the starting sector
		curr.getCurrentSector().removePlayer(curr);
		curr.setCurrentSector(humanSector);
		humanSector.addPlayer(curr);
		rrNotification.setMessage("You've teleported to the human sector");
		psNotification
				.setMessage(psNotification.getMessage()
						+ "\n[GLOBAL MESSAGE]: He/She will be teleported to the human sector");
		return true;
	}
}
