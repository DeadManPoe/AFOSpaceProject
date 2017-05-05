package effects;

import common.DiscardAction;
import common.ObjectCard;
import decks.ObjectDeck;
import server.Game;
import common.Player;
import server_store.StoreAction;

/**
 * Represents the effect of discarding an object card
 * 
 * @see ActionEffect
 * @see DiscardAction
 * @author Andrea Sessa
 * @author Giorgio Pea
 * @version 1.2
 */
public class DiscardObjCardEffect extends ActionEffect {
	public static boolean executeEffect(Game game, StoreAction action) {
		DiscardAction castedAction = (DiscardAction) action;
		Player currentPlayer = game.getCurrentPlayer();
		ObjectDeck objectDeck = game.getObjectDeck();
		ObjectCard discardedCard = castedAction.payload;
		currentPlayer.getPrivateDeck().removeCard(discardedCard);
		objectDeck.addToDiscard(discardedCard);
		objectDeck.refill();
		// Notifications setting
		game.getLastRRclientNotification().setMessage("You have discarded a "
				+ discardedCard.toString() + " object card");
		game.getLastPSclientNotification().setMessage("[GLOBAL MESSAGE]: "
				+ currentPlayer.getName() + " has discarded an object card\n");
		//
		game.setLastAction(castedAction);
		return true;
	}
}
