package effects;

import common.DrawObjectCardAction;
import common.ObjectCard;
import server.Game;

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
		ObjectCard objectCard = (ObjectCard) game.getObjectDeck().popCard();
		// Notify the client
		if (objectCard == null) {
			game.getLastPSclientNotification().setMessage(game.getLastPSclientNotification().getMessage()
					+ "\n[GLOBAL MESSAGE]: No more object cards");
		} else {
			game.getLastRRclientNotification().addCard(objectCard);
			game.getCurrentPlayer().getPrivateDeck().addCard(objectCard);
			game.getLastPSclientNotification().setMessage(game.getLastPSclientNotification().getMessage()
					+ "\n[GLOBAL MESSAGE]: "
					+ game.getCurrentPlayer().getName()
					+ " has drawn a object card");
		}
		return true;
	}
}
