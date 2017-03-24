package effects;

import common.DefenseObjectCard;
import common.ObjectCard;
import server_store.Game;

/**
 * Represents the effect of a defense card
 * 
 * @see ObjectCardEffect
 * @see DefenseObjectCard
 * @author Andrea Sessa
 * @author Giorgio Pea
 * @version 1.0
 */
public class DefenseObjCardEffect extends ObjectCardEffect {

	public static boolean executeEffect(Game game, ObjectCard card) {
		game.lastRRclientNotification.setMessage("You've defended from an attack");
		return true;
	}
}
