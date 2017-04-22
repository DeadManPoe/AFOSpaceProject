package effects;

import common.ObjectCard;
import it.polimi.ingsw.cg_19.Game;
import it.polimi.ingsw.cg_19.Player;
import common.AdrenalineObjectCard;
import common.PSClientNotification;
import common.RRClientNotification;

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
	public static boolean executeEffect(Game game,
										RRClientNotification rrNotification,
										PSClientNotification psNotification, ObjectCard objectCard) {
		Player currentPlayer = game.getCurrentPlayer();
		// Notifications setting
		rrNotification.setMessage("You will move by two sector this turn\n");
		psNotification.setMessage("[GLOBAL MESSAGE]: "
				+ currentPlayer.getName()
				+ " has used an adrenaline object card\n");
		currentPlayer.setAdrenalined(true);
		return true;
	}
}
