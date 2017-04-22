package effects;

import common.*;
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
	public boolean executeEffect(Game game,
								 RRClientNotification rrNotification,
								 PSClientNotification psNotification, ObjectCard objectCard) {

		AttackObjectCard card = (AttackObjectCard) objectCard;
		Sector sectorToAttack = card.getAttackTarget();
		// Executing an attack object card action effect is like executing a
		// move and attack action effect
		return MoveAttackActionEffect.executeEffect(game, rrNotification, psNotification, new MoveAttackAction(sectorToAttack));
	}
}
