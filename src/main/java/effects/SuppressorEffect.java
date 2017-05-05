package effects;

import common.ObjectCard;
import common.SuppressorObjectCard;
import server.Game;
import common.Player;

/**
 * Represents the effect of a suppressor object card
 * 
 * @see ObjectCardEffect
 * @see SuppressorObjectCard
 * @author Andrea Sessa
 * @author Giorgio Pea
 * @version 1.0
 */
public class SuppressorEffect extends ObjectCardEffect {

	public static boolean executeEffect(Game game, ObjectCard objectCard) {
		Player currentPlayer = game.getCurrentPlayer();
		currentPlayer.setSedated(true);
		game.getLastRRclientNotification()
				.setMessage("You will not draw any sector card this turn");
		game.getLastPSclientNotification()
				.setMessage(game.getLastPSclientNotification().getMessage()
						+ "\n[GLOBAL MESSAGE]: He/she will not draw any sector card this turn");
		return true;
	}
}
