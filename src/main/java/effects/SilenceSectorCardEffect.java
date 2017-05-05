package effects;


import common.SectorCard;
import common.SilenceSectorCard;
import server.Game;

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


	public static boolean executeEffect(Game game, SectorCard sectorCard) {
		game.getLastRRclientNotification().setMessage("You've said SILENCE");
		game.getLastPSclientNotification().setMessage(game.getLastPSclientNotification().getMessage()
				+ "\n[GLOBAL MESSAGE]: " + game.getCurrentPlayer().getName()
				+ " says SILENCE!");
		return true;
	}

}
