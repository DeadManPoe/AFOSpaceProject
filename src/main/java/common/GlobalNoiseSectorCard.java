package common;

/**
 * Represents a global noise sector card
 *
 *
 */
public class GlobalNoiseSectorCard extends SectorCard {
	private static final long serialVersionUID = 1L;
	// Represents the sector of noise
	private Sector sector;

	/**
	 * Constructs a global noise sector card from: a boolean value that
	 * indicates if the card has or not an associated object and from a sector
	 * that is the sector of noise
	 * 
	 * @param hasObject
	 *            true if the card has an associated object card
	 * @param sector
	 *            the sector of noise
	 */
	public GlobalNoiseSectorCard(boolean hasObject, Sector sector) {
		super(hasObject);
		this.sector = sector;
	}

	/**
	 * @return The sector of the noise
	 */
	public Sector getSector() {
		return sector;
	}

	/**
	 * @see Object#toString()
	 */
	@Override
	public String toString() {
		return "GlobalNoiseSectorCard";
	}
}