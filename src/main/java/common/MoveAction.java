package common;

/**
 * Represents a move action in the game
 * 
 * @author Andrea Sessa
 * @author Giorgio Pea
 */
public class MoveAction extends Action {
	private static final long serialVersionUID = 1L;

	private final Sector target;

	/**
	 * Constructs a move action from the sector that is the target of the action
	 * 
	 * @param target
	 *            the sector that is the target of the action
	 */
	public MoveAction(Sector target) {
		this.target = target;
	}

	/**
	 * Gets the sector that is the target of the action
	 * 
	 * @return the sector that is the target of the action
	 */
	public Sector getTarget() {
		return target;
	}

	/**
	 * @see Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((target == null) ? 0 : target.hashCode());
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
		return true;
	}

	/**
	 * @see Object#toString()
	 */
	@Override
	public String toString() {
		return "MoveAction [target=" + target + "]";
	}
}
