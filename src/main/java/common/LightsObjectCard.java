package common;

/**
 * Represents a lights object card
 * 
 * @author Andrea Sessa
 * @author Giorgio Pea
 * @version 1.0
 */
public class LightsObjectCard extends ObjectCard {
	private static final long serialVersionUID = 1L;
	// The target of the lights effect
	private final Sector centralSector;


	public LightsObjectCard(Sector centralSector) {
		this.centralSector = centralSector;
	}

	/**
	 * Gets the sector whose neighbors must be checked in order to find out if
	 * there is any player inside
	 * 
	 * @return The target of the lights effect
	 */
	public Sector getTarget() {
		return this.centralSector;
	}

	@Override
	public String toString() {
		return "LightObjectCard";
	}
}
