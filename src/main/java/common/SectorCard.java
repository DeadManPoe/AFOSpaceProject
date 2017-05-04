package common;

/**
 * Represents a sector card in the game
 */
public class SectorCard extends Card {
	// A field automatically created for serialization purposes
	private static final long serialVersionUID = 1L;

	// Indicates if this sector card is associated with an object card
	private final boolean hasObject;

	/**
	 * Constructs a sector card from a boolean value that indicates if the card
	 * has an associated object card
	 * 
	 * @param hasObject
	 *            the boolean value that indicates if the card has an associated
	 *            object card
	 */
	public SectorCard(boolean hasObject) {
		this.hasObject = hasObject;
	}

	/**
	 * Checks if the sector card has or not an associated object card
	 * 
	 * @return true if the sector card has an associated object card, otherwise
	 *         false
	 */
	public boolean hasObjectAssociated() {
		return hasObject;
	}
}
