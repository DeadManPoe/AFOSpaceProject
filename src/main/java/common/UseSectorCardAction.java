package common;

/**
 * Represents the action of using a sector card in the game
 * 
 * @author Andrea Sessa
 * @author Giorgio Pea
 * @version 1.0
 */
public class UseSectorCardAction extends Action {
	// A field automatically created for serialization purposes
	private static final long serialVersionUID = 1L;

	private final SectorCard card;

	/**
	 * Constructs an action of using a sector card from the sector it refers to
	 * 
	 * @param card
	 */
	public UseSectorCardAction(SectorCard card) {
		this.card = card;
	}

	/**
	 * Gets the sector card associated with the action
	 * 
	 * @return the sector card associated with the action
	 */
	public SectorCard getCard() {
		return card;
	}

}
