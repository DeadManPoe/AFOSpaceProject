package client;

import javax.swing.JLabel;
import common.ObjectCard;

/**
 * Represents an object card label displayed in the gui
 * 
 * @author Andrea Sessa
 * @author Giorgio Pea
 * @version 1.0
 */
public class ObjectCardLabel extends JLabel {
	private ObjectCard objectCard;

	/**
	 * Constructs an object card label displayed in the gui. This label is
	 * constructed from the object card it refers to.
	 * 
	 * @param objectCard
	 *            the object card associated with this label
	 */
	public ObjectCardLabel(ObjectCard objectCard) {
		this.objectCard = objectCard;
	}

	/**
	 * Gets the object card associated with the label
	 * 
	 * @return the object card associated with the label
	 */
	public ObjectCard getCard() {
		return objectCard;
	}
}
