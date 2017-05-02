package effects;

import common.*;
import decks.SectorDeck;
import it.polimi.ingsw.cg_19.Game;

/**
 * This class represents the effect associated to a draw action from the sector
 * cards deck
 *
 */
public class DrawSectorCardEffect extends ActionEffect {
	public static boolean executeEffect(Game game,
			RRClientNotification rrNotification,
			PSClientNotification psNotification, Action action) {
		// Get the reference for the sector deck
		SectorDeck sectorDeck = game.getSectorDeck();
		// Draws a new card from the deck
		SectorCard sectorCard = (SectorCard) sectorDeck.popCard();
		// Notify the client
		rrNotification.addCard(sectorCard);

		psNotification.setMessage(psNotification.getMessage()
				+ "\n[GLOBAL MESSAGE]: " + game.getCurrentPlayer().getName()
				+ " has drawn a sector card");

		sectorDeck.addToDiscard(sectorCard);
		sectorDeck.refill();

		game.setLastAction(action);

		// Now i need to execute the effect of the sector card

		if (sectorCard.hasObjectAssociated()) {
			DrawObjectCardEffect.executeEffect(game,rrNotification,psNotification);
		}
		if (!(sectorCard instanceof GlobalNoiseSectorCard)) {
			UseSectorCardEffect.executeEffect(game,rrNotification, psNotification,new UseSectorCardAction(sectorCard));
		}
		return true;
	}
}
