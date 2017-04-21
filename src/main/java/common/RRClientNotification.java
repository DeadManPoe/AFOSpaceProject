package common;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a notification to be delivered to a single client in response to
 * one of its game actions.
 * 
 * @author Andrea Sessa
 * @author Giorgio Pea
 * @version 1.0
 */
public class RRClientNotification extends ClientNotification {
	// A field automatically created for serialization purposes
	private static final long serialVersionUID = 1L;
	private boolean actionResult;
	private final ArrayList<Card> drawedCards;
	private final ArrayList<Sector> lightedSectors;
	private final PlayerToken playerToken;

	/**
	 * Constructs a notification to be delivered to a single client in response
	 * to one of its game actions. This notification is constructed from the
	 * result of the action it refers to and from the list of cards the client
	 * has drawn by performing the action above mentioned, all along with an
	 * empty text message
	 * 
	 * @param actionResult
	 *            the result of the action the notification refers to
	 * @param drawedCards
	 *            the list of cards the client has drawn by performing the
	 *            action referred by the notification
	 * @param sectors
	 *            the list of sectors to "light" on the GUI after using a light
	 *            sevtor card
	 */
	public RRClientNotification(boolean actionResult,
			ArrayList<Card> drawedCards, ArrayList<Sector> sectors, PlayerToken playerToken) {
		super("");
		this.actionResult = actionResult;
		this.drawedCards = drawedCards;
		this.lightedSectors = sectors;
	}

	/**
	 * * Constructs a notification to be delivered to a single client in
	 * response to one of its game actions. This notification is constructed
	 * from the result of the action it refers to,that is set to false, and from
	 * the list of cards the client has drawn by performing the action above
	 * mentioned,that is created, all along with an empty text message
	 */
	public RRClientNotification() {
		super("");
		this.drawedCards = new ArrayList<Card>();
		this.lightedSectors = new ArrayList<Sector>();
	}

	/**
	 * Gets the result of the action it refers to
	 * 
	 * @return the result of the action it refers to
	 */
	public boolean getActionResult() {
		return actionResult;
	}

	/**
	 * Sets the result of the action it refers to
	 * 
	 * @param result
	 *            the new action result to be set
	 */
	public void setActionResult(boolean result) {
		this.actionResult = result;
	}

	/**
	 * Gets the list of cards the client has drawn by performing the action
	 * referred by the notification
	 * 
	 * @return the list of cards the client has drawn by performing the action
	 *         referred by the notification
	 */
	public List<Card> getDrawnCards() {
		return drawedCards;
	}

	/**
	 * @return The list of sectors that the client has highlighted during its
	 *         last move
	 */
	public List<Sector> getLightedSectors() {
		return this.lightedSectors;
	}

	/**
	 * Adds a new card to the list of drawn card
	 * 
	 * @param card
	 *            the card to add to the list
	 * @throws IllegalArgumentException
	 *             if card is null
	 */
	public void addCard(Card card) {
		if (card == null)
			throw new IllegalArgumentException("card must not be null");
		this.drawedCards.add(card);
	}

	/**
	 * Add a new Sector to the list of lighted sectors
	 * 
	 * @param sector
	 *            the sector to add to the list
	 * 
	 * @throws IllegalArgumentException
	 *             if sector is null
	 */
	public void addSector(Sector sector) {
		this.lightedSectors.add(sector);
	}
}
