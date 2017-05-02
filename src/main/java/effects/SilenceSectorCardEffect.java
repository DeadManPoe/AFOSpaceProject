package effects;

import common.SectorCard;
import it.polimi.ingsw.cg_19.Game;
import common.PSClientNotification;
import common.RRClientNotification;
import common.SilenceSectorCard;

/**
 * Represents the effect of silence sector card
 *
 *
 */
public class SilenceSectorCardEffect extends SectorCardEffect {
	public static boolean executeEffect(Game game,
								 RRClientNotification rrNotification,
								 PSClientNotification psNotification, SectorCard sectorCard) {
		rrNotification.setMessage("You've said SILENCE");
		psNotification.setMessage(psNotification.getMessage()
				+ "\n[GLOBAL MESSAGE]: " + game.getCurrentPlayer().getName()
				+ " says SILENCE!");
		return true;
	}

}
