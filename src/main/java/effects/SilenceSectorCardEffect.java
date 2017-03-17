package effects;

import it.polimi.ingsw.cg_19.Game;
import common.PSClientNotification;
import common.RRClientNotification;
import common.SilenceSectorCard;

/**
 * Represents the effect of silence sector card
 * 
 * @see SectorCardEffect
 * @see SilenceSectorCard
 * @author Andrea Sessa
 * @author Giorgio Pea
 * @version 1.0
 *
 */
public class SilenceSectorCardEffect extends SectorCardEffect {
	/**
	 * Constructs an effect of a silence sector card. This effect is constructed
	 * from a {@link common.SilenceSectorCard}
	 * 
	 * @param silenceSectorCard
	 *            the {@link common.SilenceSectorCard} that needs to be enriched
	 *            with its effect
	 */
	public SilenceSectorCardEffect(SilenceSectorCard silenceSectorCard) {
		super(silenceSectorCard);
	}

	/**
	 * Constructs an effect of a silence sector card. This effect is constructed
	 * from a {@link common.SilenceSectorCard} that is null
	 * 
	 */
	public SilenceSectorCardEffect() {
		this(null);
	}

	/**
	 * @see effects.SectorCardEffect#executeEffect
	 */
	@Override
	public boolean executeEffect(server_store.Game game,
								 RRClientNotification rrNotification,
								 PSClientNotification psNotification) {
		rrNotification.setMessage("You've said SILENCE");
		psNotification.setMessage(psNotification.getMessage()
				+ "\n[GLOBAL MESSAGE]: " + game.currentPlayer.name
				+ " says SILENCE!");
		return true;
	}

}
