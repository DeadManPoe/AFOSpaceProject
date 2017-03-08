package effects;

import it.polimi.ingsw.cg_19.Game;
import it.polimi.ingsw.cg_19.PlayerState;
import it.polimi.ingsw.cg_19.RescueType;
import common.DrawRescueCardAction;
import common.EndTurnAction;
import common.PSClientNotification;
import common.RRClientNotification;
import common.RescueCard;
import common.SectorType;

/**
 * Represents the effect associated to the action of drawing a rescue card from
 * the deck containing rescue cards
 * 
 * @see ActionEffect
 * @see DrawRescueCardAction
 * @author Andrea Sessa
 * @author Giorgio Pea
 * @version 1.0
 */
public class DrawRescueCardEffect extends ActionEffect {

	/**
	 * Constructs the effect of the action of drawing a rescue card from the
	 * deck containing rescue cards. This effect is constructed from a
	 * {@link common.DrawRescueCardAction}
	 * 
	 * @param action
	 *            the {@link common.DrawRescueCardAction} that needs to be
	 *            enriched with its effect
	 */
	public DrawRescueCardEffect(DrawRescueCardAction action) {
		super(action);
	}

	/**
	 * Constructs the effect of the action of drawing a rescue card from the
	 * deck containing rescue cards. This effect is constructed from a
	 * {@link common.DrawRescueCardAction} that is null. This constructor is
	 * only used for test purposes.
	 */
	public DrawRescueCardEffect() {
		super(null);
	}

	/**
	 * @see ActionEffect#executeEffect
	 */
	@Override
	public boolean executeEffect(Game game,
			RRClientNotification rrNotification,
			PSClientNotification psNotification) {
		RescueCard card = (RescueCard) game.getRescueDeck().popCard();
		if (card.getType() == RescueType.GREEN) {
			game.getCurrentPlayer().setPlayerState(PlayerState.ESCAPED);
			game.getCurrentPlayer().getSector()
					.setSectorType(SectorType.CLOSED_RESCUE);
			psNotification.setMessage(psNotification.getMessage()
					+ "\n[GLOBAL MESSAGE]: "
					+ game.getCurrentPlayer().getName()
					+ " has escaped from aliens!");
			rrNotification.addCard(card);
			psNotification.setEscapedPlayer(game.fromPlayerToToken(game.getCurrentPlayer()));
			EndTurnAction action = new EndTurnAction();
			EndTurnEffect effect = new EndTurnEffect(action);
			effect.executeEffect(game, rrNotification, psNotification);
			return true;
		} else {
			rrNotification.addCard(card);
			game.getCurrentPlayer().getSector()
					.setSectorType(SectorType.CLOSED_RESCUE);
			psNotification.setMessage(psNotification.getMessage()
					+ "\n[GLOBAL MESSAGE]: "
					+ game.getCurrentPlayer().getName()
					+ " has not escaped from aliens");
			return false;
		}

	}

}
