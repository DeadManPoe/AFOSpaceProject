package common;

/**
 * Represents a silence sector card in the game
 *
 */
public class SilenceSectorCard extends SectorCard {
	// A field automatically created for serialization purposes
	private static final long serialVersionUID = 1L;

	/**
	 * Constructs a silence sector. A silence sector card has always no
	 * associated object card
	 *
	 */
	public SilenceSectorCard() {
		super(false);
	}

	@Override
	public String toString() {
		return "SilenceSectorCard";
	}
}
