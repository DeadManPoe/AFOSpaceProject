package effects;

import common.DrawObjectCardAction;
import common.DrawSectorCardAction;
import common.GlobalNoiseSectorCard;
import common.PSClientNotification;
import common.RRClientNotification;
import common.SectorCard;
import common.UseSectorCardAction;
import decks.SectorDeck;
import it.polimi.ingsw.cg_19.Game;

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
	 * Constructs an effect of discarding an object card. This effect is
	 * constructed from a {@link common.DrawSectorCardAction}
	 * 
	 * @param drawSectorCardAction
	 *            the {@link common.DrawSectorCardAction} that needs to be
	 *            enriched with its effect
	 */
	public DrawSectorCardEffect(DrawSectorCardAction drawSectorCardAction) {
		super(drawSectorCardAction);
	}

	/**
	 * Constructs an effect of discarding an object card. This effect is
	 * constructed from a {@link common.DrawSectorCardAction} that is null. This
	 * constructor is used only for test purposes.
	 * 
	 */
	public DrawSectorCardEffect() {
		super(null);
	}

	@Override
	/**
	 * Implements the abstract method defined in ActionEffect according to DrawActionFromSector
	 * @param game A reference to Decks,map,...
	 */
	public boolean executeEffect(server_store.Game game,
								 RRClientNotification rrNotification,
								 PSClientNotification psNotification) {
		// Get the reference for the sector deck
		SectorDeck sectorDeck = game.sectorDeck;
		// Draws a new card from the deck
		SectorCard sectorCard = (SectorCard) sectorDeck.popCard();
		// Notify the client
		rrNotification.addCard(sectorCard);

		psNotification.setMessage(psNotification.getMessage()
				+ "\n[GLOBAL MESSAGE]: " + game.currentPlayer.name
				+ " has drawn a sector card");

		sectorDeck.addToDiscard(sectorCard);
		sectorDeck.refill();

		game.lastAction = action;

		// Now i need to execute the effect of the sector card

		if (sectorCard.hasObjectAssociated()) {
			DrawObjectCardEffect effect = new DrawObjectCardEffect(
					new DrawObjectCardAction());
			effect.executeEffect(game, rrNotification, psNotification);
		}
		if (!(sectorCard instanceof GlobalNoiseSectorCard)) {
			UseSectorCardEffect effect = new UseSectorCardEffect(
					new UseSectorCardAction(sectorCard));
			effect.executeEffect(game, rrNotification, psNotification);
		}
		return true;
	}
}
