package effects;

import it.polimi.ingsw.cg_19.Game;
import it.polimi.ingsw.cg_19.Player;
import common.PSClientNotification;
import common.RRClientNotification;
import common.SuppressorObjectCard;

/**
 * Represents the effect of a suppressor object card
 * 
 * @see ObjectCardEffect
 * @see SuppressorObjectCard
 * @author Andrea Sessa
 * @author Giorgio Pea
 * @version 1.0
 */
public class SuppressorEffect extends ObjectCardEffect {
	/**
	 * Constructs an effect of a suppressor object card. This effect is
	 * constructed from a {@link common.SuppressorObjectCard}
	 * 
	 * @param suppressorObjectCard
	 *            the {@link common.SuppressorObjectCard} that needs to be
	 *            enriched with its effect
	 */
	public SuppressorEffect(SuppressorObjectCard suppressorObjectCard) {
		super(suppressorObjectCard);
	}

	/**
	 * Constructs an effect of a suppressor object card. This effect is
	 * constructed from a {@link common.SuppressorObjectCard} that is null
	 * 
	 */
	public SuppressorEffect() {
		super(null);
	}

	/**
	 * @see ObjectCardEffect#executeEffect(Game)
	 */
	@Override
	public boolean executeEffect(Game game,
			RRClientNotification rrNotification,
			PSClientNotification psNotification) {
		Player currentPlayer = game.getCurrentPlayer();
		currentPlayer.setSedated(true);
		rrNotification
				.setMessage("You will not draw any sector card this turn");
		psNotification
				.setMessage(psNotification.getMessage()
						+ "\n[GLOBAL MESSAGE]: He/she will not draw any sector card this turn");
		return true;
	}
}
