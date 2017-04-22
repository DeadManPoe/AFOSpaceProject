package effects;

import common.ObjectCard;
import it.polimi.ingsw.cg_19.Game;
import common.DefenseObjectCard;
import common.PSClientNotification;
import common.RRClientNotification;

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
	public static boolean executeEffect(Game game,
										RRClientNotification rrNotification,
										PSClientNotification psNotification, ObjectCard objectCard) {
		rrNotification.setMessage("You've defended from an attack");
		return true;
	}
}
