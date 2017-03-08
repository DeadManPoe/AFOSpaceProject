package effects;

import it.polimi.ingsw.cg_19.Game;
import it.polimi.ingsw.cg_19.Player;
import common.AdrenalineObjectCard;
import common.PSClientNotification;
import common.RRClientNotification;

/**
 * Represents the effect of the adrenaline object card
 * 
 * @see ObjectCardEffect
 * @see AdrenalineObjCardEffect
 * @author Andrea Sessa
 * @author Giorgio Pea
 * @version 1.0
 */
public class AdrenalineObjCardEffect extends ObjectCardEffect {
	/**
	 * Constructs an effect of an adrenaline object card. This effect is
	 * constructed from an {@link common.AdrenalineObjectCard}
	 * 
	 * @see AdrenalineObjectCard
	 * @param adrenalineObjCard
	 *            the {@link common.AdrenalineObjectCard} that needs to be
	 *            enriched with its effect
	 */
	public AdrenalineObjCardEffect(AdrenalineObjectCard adrenalineObjCard) {
		super(adrenalineObjCard);
	}

	/**
	 * Constructs an effect of an adrenaline object card. This effect is
	 * constructed from an {@link common.AdrenalineObjectCard} that is null.
	 * This constructor is only used for test purposes.
	 */
	public AdrenalineObjCardEffect() {
		super(null);
	}

	/**
	 * @see effects.ObjectCardEffect#executeEffect
	 */
	@Override
	public boolean executeEffect(Game game,
			RRClientNotification rrNotification,
			PSClientNotification psNotification) {
		Player currentPlayer = game.getCurrentPlayer();
		// Notifications setting
		rrNotification.setMessage("You will move by two sector this turn\n");
		psNotification.setMessage("[GLOBAL MESSAGE]: "
				+ currentPlayer.getName()
				+ " has used an adrenaline object card\n");
		currentPlayer.setAdrenaline(true);
		return true;
	}
}
