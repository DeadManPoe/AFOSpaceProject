package effects;


import common.SilenceSectorCard;
import server_store.Game;
import server_store.ServerState;

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


	public static boolean executeEffect(Game game) {
		game.lastRRclientNotification.setMessage("You've said SILENCE");
		game.lastPSclientNotification.setMessage(game.lastPSclientNotification.getMessage()
				+ "\n[GLOBAL MESSAGE]: " + game.currentPlayer.name
				+ " says SILENCE!");
		return true;
	}

}
