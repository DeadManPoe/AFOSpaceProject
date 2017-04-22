package effects;

import common.*;
import it.polimi.ingsw.cg_19.Game;
import it.polimi.ingsw.cg_19.Player;
import decks.ObjectDeck;

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
	public static boolean executeEffect(Game game,
										RRClientNotification rrNotification,
										PSClientNotification psNotification, Action action) {
		Player currentPlayer = game.getCurrentPlayer();
		DiscardAction discardAction = (DiscardAction) action;
		ObjectDeck objectDeck = game.getObjectDeck();
		ObjectCard discardedCard = discardAction.getObjectCard();
		currentPlayer.getPrivateDeck().removeCard(discardedCard);
		objectDeck.addToDiscard(discardedCard);
		objectDeck.refill();
		// Notifications setting
		rrNotification.setMessage("You have discarded a "
				+ discardedCard.toString() + " object card");
		psNotification.setMessage("[GLOBAL MESSAGE]: "
				+ currentPlayer.getName() + " has discarded an object card\n");
		//
		game.setLastAction(action);
		return true;
	}
}
