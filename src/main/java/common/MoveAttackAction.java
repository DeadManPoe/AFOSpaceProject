package common;

/**
 * Represents a move action combined with an attack
 * 
 * @author Andrea Sessa
 * @author Giorgio Pea
 * @version 1.0
 * @see Action
 */
public class MoveAttackAction extends Action {
	private static final long serialVersionUID = 1L;
	private final Sector target;

	/**
	 * Constructs a move action combined with an attack. This action is
	 * constructed from the sector that is the target of the action(both the
	 * target of the move and of the attack)
	 * 
	 * @param target
	 *            the sector that is the target of the action(both the target of
	 *            the move and of the attack)
	 */
	public MoveAttackAction(Sector target) {
		this.target = target;
	}

	/**
	 * Gets the sector that is the target of the action(both the target of the
	 * move and of the attack)
	 * 
	 * @return the sector that is the target of the action(both the target of
	 *         the move and of the attack)
	 */
	public Sector getTarget() {
		return this.target;
	}

	/**
	 * @see Object#toString()
	 */
	@Override
	public String toString() {
		return "MoveAttackAction [target=" + target + "]";
	}

}
