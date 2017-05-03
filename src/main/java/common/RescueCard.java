package common;

/**
 * Represents a rescue card in the game
 * 
 * @author Andrea Sessa
 * @author Giorgio Pea
 * @version 1.0
 */
public class RescueCard extends Card {
	// A field automatically created for serialization purposes
	private static final long serialVersionUID = 1L;
	private RescueType type;

	/**
	 * Constructs a rescue card from its type
	 * 
	 * @see RescueType
	 * @param type
	 *            the type of the rescue card
	 */
	public RescueCard(RescueType type) {
		this.type = type;
	}

	/**
	 * Gets the rescue card's type
	 * 
	 * @return the rescue card type
	 */
	public RescueType getType() {
		return type;
	}

	@Override
	public String toString() {
		return "RescueCard" + this.type;
	}
}
