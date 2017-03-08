package common;

/**
 * Represents a local noise sector card
 * 
 * @author Andrea Sessa
 * @author Giorgio Pea
 *
 */
public class LocalNoiseSectorCard extends SectorCard {
	private static final long serialVersionUID = 1L;

	/**
	 * Constructs a local noise sector card from a boolean value that indicates
	 * if the card has or not an associated object
	 * 
	 * @param hasObject
	 *            True if this card is associated to an object card
	 */
	public LocalNoiseSectorCard(boolean hasObject) {
		super(hasObject);
	}

	/**
	 * @see Object#toString()
	 */
	@Override
	public String toString() {
		return "LocalNoiseSectorCard";
	}
}
