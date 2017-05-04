package effects;


import common.ObjectCard;
import common.Player;
import server.Game;

/**
 * Represents the effect of the adrenaline object card
 * 
 * @see ObjectCardEffect
 * @see AdrenalineObjCardEffect
 * @author Andrea Sessa
 * @author Giorgio Pea
 * @version 1.0
 */
public class AdrenalineObjCardEffect extends ObjectCardEffect {

	public static boolean executeEffect(Game game, ObjectCard card) {
		Player currentPlayer = game.currentPlayer;
		// Notifications setting
		game.lastRRclientNotification.setMessage("You will moveToSector by two sector this turn\n");
		game.lastPSclientNotification.setMessage("[GLOBAL MESSAGE]: "
				+ currentPlayer.name
				+ " has used an adrenaline object card\n");
		currentPlayer.isAdrenalined = true;
		return true;
	}
}
