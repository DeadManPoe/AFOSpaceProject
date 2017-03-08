package effects;

import it.polimi.ingsw.cg_19.Game;
import common.LocalNoiseSectorCard;
import common.PSClientNotification;
import common.RRClientNotification;
import common.Sector;

/**
 * Represents the effect associated with a local noise sector card
 * 
 * @see SectorCardEffect
 * @see LocalNoiseSectorCard
 * @author Andrea Sessa
 * @author Giorgio Pea
 */
public class LocalNoiseSectorCardEffect extends SectorCardEffect {
	/**
	 * Constructs the effect associated with a local noise sector card from a
	 * {@link common.LocalNoiseSectorCard}
	 * 
	 * @param localNoiseSectorCard
	 *            the {@link common.LocalNoiseSectorCard} that needs to be
	 *            enriched with its effect
	 */
	public LocalNoiseSectorCardEffect(LocalNoiseSectorCard localNoiseSectorCard) {
		super(localNoiseSectorCard);
	}

	/**
	 * Constructs the effect associated with a local noise sector card from a
	 * {@link common.LocalNoiseSectorCard} that is null. This constructor is
	 * used only for test purposes
	 * 
	 * 
	 */
	public LocalNoiseSectorCardEffect() {
		super(null);
	}

	/**
	 * @see SectorCardEffect#executeEffect(Game)
	 */
	@Override
	public boolean executeEffect(Game game,
			RRClientNotification rrNotification,
			PSClientNotification psNotification) {
		// The local noise effect could be seen as a global noise effect with a
		// sector that is automatically
		// indicated
		// Notify all the player
		String name = game.getCurrentPlayer().getName();
		Sector target = game.getCurrentPlayer().getSector();
		psNotification.setMessage(psNotification.getMessage()
				+ "\n[GLOBAL MESSAGE]: " + name
				+ " has made noise in sector "
				+ target.getCoordinate().toString());
		return true;
	}
}
