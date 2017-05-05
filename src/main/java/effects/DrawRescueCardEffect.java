package effects;

import common.*;
import common.PlayerState;
import common.RescueType;
import server.Game;

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
	public static boolean executeEffect(Game game) {
		RescueCard card = (RescueCard) game.getRescueDeck().popCard();
		if (card.getType() == RescueType.GREEN) {
			game.getCurrentPlayer().setPlayerState(PlayerState.ESCAPED);
			game.getCurrentPlayer().getCurrentSector()
					.setSectorType(SectorType.CLOSED_RESCUE);
			game.getLastPSclientNotification().setMessage(game.getLastPSclientNotification().getMessage()
					+ "\n[GLOBAL MESSAGE]: "
					+ game.getCurrentPlayer().getName()
					+ " has escaped from aliens!");
			game.getLastRRclientNotification().addCard(card);
			game.getLastPSclientNotification().setEscapedPlayer(game.getCurrentPlayer().getPlayerToken());
			EndTurnEffect.executeEffect(game, new EndTurnAction());
			return true;
		} else {
			game.getLastRRclientNotification().addCard(card);
			game.getCurrentPlayer().getCurrentSector()
					.setSectorType(SectorType.CLOSED_RESCUE);
			game.getLastPSclientNotification().setMessage(game.getLastPSclientNotification().getMessage()
					+ "\n[GLOBAL MESSAGE]: "
					+ game.getCurrentPlayer().getName()
					+ " has not escaped from aliens");
			return false;
		}

	}

}
