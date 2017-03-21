package effects;

import common.DrawObjectCardAction;
import common.ObjectCard;
import server_store.Game;

/**
 * Represents the effect of drawing an object card from the deck containing
 * object cards
 * 
 * @see ActionEffect
 * @see DrawObjectCardAction
 * @author Andrea Sessa
 * @author Giorgio Pea
 * @version 1.1
 */
public class DrawObjectCardEffect extends ActionEffect {


	public static boolean executeEffect(Game game) {
		// Get a new card from the correct deck
		ObjectCard objectCard = (ObjectCard) game.objectDeck.popCard();
		// Notify the client
		if (objectCard == null) {
			game.lastPSclientNotification.setMessage(game.lastPSclientNotification.getMessage()
					+ "\n[GLOBAL MESSAGE]: No more object cards");
		} else {
			game.lastRRclientNotification.addCard(objectCard);
			game.currentPlayer.privateDeck.addCard(objectCard);
			game.lastPSclientNotification.setMessage(game.lastPSclientNotification.getMessage()
					+ "\n[GLOBAL MESSAGE]: "
					+ game.currentPlayer.name
					+ " has drawn a object card");
		}
		return true;
	}
}
