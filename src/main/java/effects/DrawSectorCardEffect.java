package effects;

import common.*;
import decks.SectorDeck;
import server_store.Game;

/**
 * This class represents the effect associated to a draw action from the sector
 * cards deck
 * 
 * @see ActionEffect
 * @see DrawSectorCardAction
 * @author Andrea Sessa
 * @author Giorgio Pea
 * @version 1.0
 */
public class DrawSectorCardEffect extends ActionEffect {
	/**
	 * Implements the abstract method defined in ActionEffect according to DrawActionFromSector
	 * @param game A reference to Decks,map,...
	 */
	public static boolean executeEffect(Game game,DrawSectorCardAction action) {
		// Get the reference for the sector deck
		SectorDeck sectorDeck = game.sectorDeck;
		// Draws a new card from the deck
		SectorCard sectorCard = (SectorCard) sectorDeck.popCard();
		// Notify the client
		game.lastRRclientNotification.addCard(sectorCard);

		game.lastPSclientNotification.setMessage(game.lastPSclientNotification.getMessage()
				+ "\n[GLOBAL MESSAGE]: " + game.currentPlayer.name
				+ " has drawn a sector card");

		sectorDeck.addToDiscard(sectorCard);
		sectorDeck.refill();

		game.lastAction = action;

		// Now i need to execute the effect of the sector card

		if (sectorCard.hasObjectAssociated()) {
			DrawObjectCardEffect.executeEffect(game);
		}
		if (!(sectorCard instanceof GlobalNoiseSectorCard)) {
			UseSectorCardEffect.executeEffect(game,new UseSectorCardAction(sectorCard, game.gamePublicData.getId()));
		}
		return true;
	}
}
