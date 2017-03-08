package effects;

import it.polimi.ingsw.cg_19.Game;
import common.GlobalNoiseSectorCard;
import common.PSClientNotification;
import common.RRClientNotification;
import common.Sector;

/**
 * Represents the effect associated with a global noise sector card
 * 
 * @see SectorCardEffect
 * @see GlobalNoiseSectorCard
 * @author Andrea Sessa
 * @author Giorgio Pea
 * @version 1.0
 */
public class GlobalNoiseSectorCardEffect extends SectorCardEffect {
	/**
	 * Constructs the effect associated with a global noise sector card from a
	 * {@link GlobalNoiseSectorCard}
	 * 
	 * @param globalNoiseSectorCard
	 *            the {@link GlobalNoiseSectorCard} that needs to be enriched
	 *            with its effect
	 */
	public GlobalNoiseSectorCardEffect(
			GlobalNoiseSectorCard globalNoiseSectorCard) {
		super(globalNoiseSectorCard);
	}

	/**
	 * Constructs the effect associated with a global noise sector card from a
	 * {@link GlobalNoiseSectorCard} that is null. This constructor is used only
	 * for test purposes.
	 * 
	 */
	public GlobalNoiseSectorCardEffect() {
		super(null);
	}

	/**
	 * @see SectorCardEffect#executeEffect(Game)
	 */
	@Override
	public boolean executeEffect(Game game,
			RRClientNotification rrNotification,
			PSClientNotification psNotification) {
		// Notify all the player
		String name = game.getCurrentPlayer().getName();
		Sector target = ((GlobalNoiseSectorCard) sectorCard).getSector();
		rrNotification.setMessage("You've indicated the sector: "
				+ target.getCoordinate().toString());
		psNotification.setMessage("[GLOBAL MESSAGE]: "
				+ psNotification.getMessage() + name
				+ " has made noise in sector "
				+ target.getCoordinate().toString());
		return true;
	}
}