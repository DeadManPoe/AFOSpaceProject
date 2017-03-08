package effects;

import it.polimi.ingsw.cg_19.Game;
import common.DefenseObjectCard;
import common.PSClientNotification;
import common.RRClientNotification;

/**
 * Represents the effect of a defense card
 * 
 * @see ObjectCardEffect
 * @see DefenseObjectCard
 * @author Andrea Sessa
 * @author Giorgio Pea
 * @version 1.0
 */
public class DefenseObjCardEffect extends ObjectCardEffect {

	/**
	 * Constructs an effect of a defense object card. This effect is constructed
	 * from a {@link common.DefenseObjectCard}
	 * 
	 * @param defenseObjCard
	 *            the {@link common.DefenseObjectCard} that needs to be enriched
	 *            with its effect
	 */
	public DefenseObjCardEffect(DefenseObjectCard defenseObjCard) {
		super(defenseObjCard);
	}

	/**
	 * Constructs an effect of a defense object card. This effect is constructed
	 * from a {@link common.DefenseObjectCard} that is null. This constructor is
	 * only used for test purposes.
	 * 
	 * @param defenseObjCard
	 *            the {@link common.DefenseObjectCard} that needs to be enriched
	 *            with its effect
	 */
	public DefenseObjCardEffect() {
		this(null);
	}

	/**
	 * @see ObjectCardEffect#executeEffect(Game)
	 */
	@Override
	public boolean executeEffect(Game game,
			RRClientNotification rrNotification,
			PSClientNotification psNotification) {
		rrNotification.setMessage("You've defended from an attack");
		return true;
	}
}
