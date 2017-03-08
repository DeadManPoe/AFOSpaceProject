package common;

/**
 * Represents the action of using an object card in the game
 * 
 * @see Action
 * @author Andrea Sessa
 * @author Giorgio Pea
 * @version 1.0
 */
public class UseObjAction extends Action {
	// A field automatically created for serialization purposes
	private static final long serialVersionUID = 1L;

	private final ObjectCard objectCard;

	/**
	 * Constructs an action of using an object card from the object card it
	 * refers to
	 * 
	 * @param objectCard
	 *            the object card it's referred by the use object action
	 */
	public UseObjAction(ObjectCard objectCard) {
		this.objectCard = objectCard;
	}

	/**
	 * Gets the object card associated with the use object action
	 * 
	 * @return the object card associated with the use object action
	 */
	public ObjectCard getCard() {
		return objectCard;
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((objectCard == null) ? 0 : objectCard.hashCode());
		return result;
	}

	/**
	 * @see Object#equals(Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UseObjAction other = (UseObjAction) obj;
		if (objectCard == null) {
			if (other.objectCard != null)
				return false;
		} else if (!objectCard.equals(other.objectCard))
			return false;
		return true;
	}

}
