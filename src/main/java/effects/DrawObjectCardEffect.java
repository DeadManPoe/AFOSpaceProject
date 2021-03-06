package effects;

import common.DrawObjectCardAction;
import common.ObjectCard;
import common.PSClientNotification;
import common.RRClientNotification;
import decks.ObjectDeck;
import it.polimi.ingsw.cg_19.Game;

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
	/**
	 * Constructs the effect of drawing an object card from the deck containing
	 * object cards. This effect is constructed from a
	 * {@link common.DrawObjectCardAction}
	 * 
	 * @param action
	 *            the {@link common.DrawObjectCardAction} that needs to be
	 *            enriched with its effect
	 */
	public DrawObjectCardEffect(DrawObjectCardAction drawObjectCardAction) {
		super(drawObjectCardAction);
	}

	/**
	 * Constructs the effect of drawing an object card from the deck containing
	 * object cards. This effect is constructed from a
	 * {@link common.DrawObjectCardAction} that is null. This constructor is
	 * only used for test purposes.
	 */
	public DrawObjectCardEffect() {
		this(null);
	}

	@Override
	/**
	 * @see ActionEffect#executeEffect
	 */
	public boolean executeEffect(Game game,
			RRClientNotification rrNotification,
			PSClientNotification psNotification) {
		ObjectDeck objectDeck = game.getObjectDeck();
		// Get a new card from the correct deck
		ObjectCard objectCard = (ObjectCard) objectDeck.popCard();
		// Notify the client
		if (objectCard == null) {
			psNotification.setMessage(psNotification.getMessage()
					+ "\n[GLOBAL MESSAGE]: No more object cards");
		} else {
			rrNotification.addCard(objectCard);
			game.getCurrentPlayer().getPrivateDeck().addCard(objectCard);
			psNotification.setMessage(psNotification.getMessage()
					+ "\n[GLOBAL MESSAGE]: "
					+ game.getCurrentPlayer().getName()
					+ " has drawn a object card");
		}

		return true;
	}
}
