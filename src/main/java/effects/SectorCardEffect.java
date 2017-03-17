package effects;

import it.polimi.ingsw.cg_19.Game;
import common.PSClientNotification;
import common.RRClientNotification;
import common.SectorCard;

/**
 * Represents the effect associated with a sector card
 * 
 * @see SectorCard
 * @author Andrea Sessa
 * @author Giorgio Pea
 * @version 1.0
 *
 */
public abstract class SectorCardEffect {
	// The sector card the effect refers to
	protected SectorCard sectorCard;

	/**
	 * Constructs an effect associated with a sector card. This effect is
	 * constructed from a sector card.
	 * 
	 * @param sectorCard
	 *            the sector card associated with the effect
	 */
	public SectorCardEffect(SectorCard sectorCard) {
		this.sectorCard = sectorCard;
	}

	/**
	 * Defines and executes the effect associated with a sector card.
	 * 
	 * @see Game
	 * @param game
	 *            the game this sector card effect refers to
	 * @param rrNotification
	 *            the notification to be sent to the client (through
	 *            Request/Response protocol) as a response to its request
	 * @param psNotification
	 *            the notification to be sent to all the subscribers of the game
	 *            above mentioned(through Publisher/Subscriber protocol)
	 * @return true if the sector card effect has been executed properly
	 */
	public abstract boolean executeEffect(server_store.Game game,
										  RRClientNotification rrNotification,
										  PSClientNotification psNotification);

	/**
	 * Sets the sectorCard the effect refers to
	 * 
	 * @param sectorCard
	 *            the new sector card to be associated with the effect
	 */
	public void setCard(SectorCard sectorCard) {
		this.sectorCard = sectorCard;
	}

	/**
	 * Gets the sector card the effect refers to
	 * 
	 * @return the sector card the effect refers to
	 */
	public SectorCard getCard() {
		return this.sectorCard;
	}
}
