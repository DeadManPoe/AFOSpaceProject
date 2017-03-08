package effects;

import common.AttackObjectCard;
import common.MoveAttackAction;
import common.PSClientNotification;
import common.RRClientNotification;
import common.Sector;
import it.polimi.ingsw.cg_19.Game;

/**
 * Represents the effect of the attack object card
 * 
 * @see ObjectCardEffect
 * @see AttackObjectCard
 * @author Andrea Sessa
 * @author Giorgio Pea
 * @version 1.0
 */
public class AttackObjCardEffect extends ObjectCardEffect {
	/**
	 * Constructs an effect of an attack object card. This effect is constructed
	 * from an {@link common.AttackObjectCard}
	 * 
	 * @param attackObjectCard
	 *            the {@link common.AttackObjectCard} that needs to be enriched
	 *            with its effect
	 */
	public AttackObjCardEffect(AttackObjectCard attackObjectCard) {
		super(attackObjectCard);
	}

	/**
	 * Constructs an effect of an attack object card. This effect is constructed
	 * from an {@link common.AttackObjectCard} that is null. This constructor is
	 * only used for test purposes.
	 * 
	 */
	public AttackObjCardEffect() {
		this(null);
	}

	/**
	 * @see ObjectCardEffect#executeEffect
	 */
	@Override
	public boolean executeEffect(Game game,
			RRClientNotification rrNotification,
			PSClientNotification psNotification) {

		AttackObjectCard card = (AttackObjectCard) objectCard;
		Sector sectorToAttack = card.getAttackTarget();
		// Executing an attack object card action effect is like executing a
		// move and attack action effect
		MoveAttackActionEffect effect = new MoveAttackActionEffect(
				new MoveAttackAction(sectorToAttack));
		return effect.executeEffect(game, rrNotification, psNotification);
	}
}
