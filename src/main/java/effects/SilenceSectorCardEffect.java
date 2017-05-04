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
		game.lastRRclientNotification.setMessage("You've said SILENCE");
		game.lastPSclientNotification.setMessage(game.lastPSclientNotification.getMessage()
				+ "\n[GLOBAL MESSAGE]: " + game.currentPlayer.name
				+ " says SILENCE!");
		return true;
	}

}
