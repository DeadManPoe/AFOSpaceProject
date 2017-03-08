package common;

/**
 * Represents a silence sector card in the game
 * 
 * @author Andrea Sessa
 * @author Giorgio Pea
 * @version 1.0
 */
public class SilenceSectorCard extends SectorCard {
	// A field automatically created for serialization purposes
	private static final long serialVersionUID = 1L;

	/**
	 * Constructs a silence sector. A silence sector card has always no
	 * associated object card
	 * 
	 * @param hasObject
	 *            True if this card is associated to an object card
	 */
	public SilenceSectorCard() {
		super(false);
	}

	@Override
	public String toString() {
		return "SilenceSectorCard";
	}
}
