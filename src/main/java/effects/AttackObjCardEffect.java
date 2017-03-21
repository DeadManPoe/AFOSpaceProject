package effects;

import common.AttackObjectCard;
import common.MoveAttackAction;
import common.Sector;
import server_store.Game;

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
	public boolean executeEffect(Game game, AttackObjectCard attackObjectCard) {
		Sector sectorToAttack = attackObjectCard.getAttackTarget();
		// Executing an attack object card action effect is like executing a
		// move and attack action effect
		return MoveAttackActionEffect.executeEffect(game,new MoveAttackAction(sectorToAttack));
	}
}
