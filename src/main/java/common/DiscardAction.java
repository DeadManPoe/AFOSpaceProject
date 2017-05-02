package common;

/**
 * Represents the action of discarding an object card in the game
 *
 */
public class DiscardAction extends Action {
	// A field used only for serialization purposes
	private static final long serialVersionUID = 1L;
	// The object card to be discarded
	private final ObjectCard objectCard;

	/**
	 * Constructs an action of discarding an object card. This action is
	 * constructed from the object card to be discarded
	 * 
	 * @param objectCard
	 *            the object card to be discarded
	 */
	public DiscardAction(ObjectCard objectCard) {
		this.objectCard = objectCard;
	}

	/**
	 * Gets the object card to be discarded
	 * 
	 * @return the object card to be discarded
	 */
	public ObjectCard getObjectCard() {
		return objectCard;
	}

	/**
	 * @see Object#toString()
	 */
	@Override
	public String toString() {
		return "DiscardAction [objectCard=" + objectCard + "]";
	}
}
