package effects;

import it.polimi.ingsw.cg_19.Game;
import it.polimi.ingsw.cg_19.Player;
import common.DiscardAction;
import common.ObjectCard;
import common.PSClientNotification;
import common.RRClientNotification;
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
	/**
	 * Constructs an effect of discarding an object card. This effect is
	 * constructed from a {@link common.DiscardAction}
	 * 
	 * @param discardAction
	 *            the {@link common.DiscardAction} that needs to be enriched
	 *            with its effect
	 */
	public DiscardObjCardEffect(DiscardAction discardAction) {
		super(discardAction);
	}

	/**
	 * Constructs an effect of discarding an object card. This effect is
	 * constructed from a {@link common.DiscardAction} that is null. This
	 * constructor is only used for test purposes.
	 */
	public DiscardObjCardEffect() {
		this(null);
	}

	/**
	 * @see effects.ActionEffect#executeEffect(it.polimi.ingsw.cg_19.Game)
	 */
	@Override
	public boolean executeEffect(Game game,
			RRClientNotification rrNotification,
			PSClientNotification psNotification) {
		Player currentPlayer = game.getCurrentPlayer();
		DiscardAction discardAction = (DiscardAction) this.action;
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
