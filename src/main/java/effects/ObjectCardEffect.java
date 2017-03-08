package effects;

import it.polimi.ingsw.cg_19.Game;
import common.ObjectCard;
import common.PSClientNotification;
import common.RRClientNotification;

/**
 * Represents the effect associated with an object card
 * 
 * @see ObjectCard
 * @author Andrea Sessa
 * @author Giorgio Pea
 * @version 1.0
 */
public abstract class ObjectCardEffect {
	protected ObjectCard objectCard;

	/**
	 * Constructs an effect associated with an object card. This effect is
	 * constructed from an {@link common.ObjectCard}
	 * 
	 * @param objectCard
	 *            the {@link common.ObjectCard} associated with the effect
	 */
	public ObjectCardEffect(ObjectCard objectCard) {
		this.objectCard = objectCard;
	}

	/**
	 * Defines and executes the effect associated with an
	 * {@link common.ObjectCard}.
	 * 
	 * @param game
	 *            the game this object card effect refers to
	 * @param rrNotification
	 *            the notification to be sent to the client (through
	 *            Request/Response protocol) as a response to its request
	 * @param psNotification
	 *            the notification to be sent to all the subscribers of the game
	 *            above mentioned(through Publisher/Subscriber protocol)
	 * @return true if the object card effect has been executed properly
	 */
	public abstract boolean executeEffect(Game game,
			RRClientNotification clientNotification,
			PSClientNotification psNotification);

	/**
	 * Sets the object card the effect refers to
	 * 
	 * @param objectCard
	 *            the new object card to be associated with the effect
	 */
	public void setObject(ObjectCard objectCard) {
		this.objectCard = objectCard;
	}

	/**
	 * Gets the object card the effect refers to
	 * 
	 * @return the object card the effect refers to
	 */
	public ObjectCard getObjectCard() {
		return this.objectCard;
	}

}
