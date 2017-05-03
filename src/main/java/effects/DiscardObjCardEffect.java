package effects;

import common.DiscardAction;
import common.ObjectCard;
import decks.ObjectDeck;
import server_store.Game;
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
		Player currentPlayer = game.currentPlayer;
		ObjectDeck objectDeck = game.objectDeck;
		ObjectCard discardedCard = castedAction.payload;
		currentPlayer.privateDeck.removeCard(discardedCard);
		objectDeck.addToDiscard(discardedCard);
		objectDeck.refill();
		// Notifications setting
		game.lastRRclientNotification.setMessage("You have discarded a "
				+ discardedCard.toString() + " object card");
		game.lastPSclientNotification.setMessage("[GLOBAL MESSAGE]: "
				+ currentPlayer.name + " has discarded an object card\n");
		//
		game.lastAction = castedAction;
		return true;
	}
}
