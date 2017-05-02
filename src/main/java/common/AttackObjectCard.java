package common;

/**
 * Represents an attack object card in the game
 *
 */
public class AttackObjectCard extends ObjectCard {
	// A field automatically created for serialization purposes
	private static final long serialVersionUID = 1L;

	private final Sector attackTarget;

	/**
	 * Constructs an attack object card from the sector to be attacked using the
	 * object card itself
	 * 
	 * @param attackTarget
	 *            the sector to be attacked using the object card
	 */
	public AttackObjectCard(Sector attackTarget) {
		this.attackTarget = attackTarget;
	}

	/**
	 * the sector to be attacked using this object card
	 * 
	 * @return The target sector of this card
	 */
	public Sector getAttackTarget() {
		return this.attackTarget;
	}

	@Override
	public String toString() {
		return "AttackObjectCard";
	}
}
