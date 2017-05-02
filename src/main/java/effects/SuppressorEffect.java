package effects;

import common.ObjectCard;
import it.polimi.ingsw.cg_19.Game;
import it.polimi.ingsw.cg_19.Player;
import common.PSClientNotification;
import common.RRClientNotification;
import common.SuppressorObjectCard;

/**
 * Represents the effect of a suppressor object card
 *
 */
public class SuppressorEffect extends ObjectCardEffect {
	public static boolean executeEffect(Game game,
										RRClientNotification rrNotification,
										PSClientNotification psNotification, ObjectCard objectCard) {
		Player currentPlayer = game.getCurrentPlayer();
		currentPlayer.setSedated(true);
		rrNotification
				.setMessage("You will not draw any sector card this turn");
		psNotification
				.setMessage(psNotification.getMessage()
						+ "\n[GLOBAL MESSAGE]: He/she will not draw any sector card this turn");
		return true;
	}
}
