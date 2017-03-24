package effects;

import common.ObjectCard;
import common.SuppressorObjectCard;
import server_store.Game;
import server_store.Player;

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
		Player currentPlayer = game.currentPlayer;
		currentPlayer.isSedated = true;
		game.lastRRclientNotification
				.setMessage("You will not draw any sector card this turn");
		game.lastPSclientNotification
				.setMessage(game.lastPSclientNotification.getMessage()
						+ "\n[GLOBAL MESSAGE]: He/she will not draw any sector card this turn");
		return true;
	}
}
