package effects;

import common.*;
import it.polimi.ingsw.cg_19.PlayerState;
import it.polimi.ingsw.cg_19.RescueType;
import server_store.Game;

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
		RescueCard card = (RescueCard) game.rescueDeck.popCard();
		if (card.getType() == RescueType.GREEN) {
			game.currentPlayer.playerState = PlayerState.ESCAPED;
			game.currentPlayer.currentSector
					.setSectorType(SectorType.CLOSED_RESCUE);
			game.lastPSclientNotification.setMessage(game.lastPSclientNotification.getMessage()
					+ "\n[GLOBAL MESSAGE]: "
					+ game.currentPlayer.name
					+ " has escaped from aliens!");
			game.lastRRclientNotification.addCard(card);
			game.lastPSclientNotification.setEscapedPlayer(game.currentPlayer.playerToken);
			EndTurnEffect.executeEffect(game, new EndTurnAction(game.gamePublicData.getId()));
			return true;
		} else {
			game.lastRRclientNotification.addCard(card);
			game.currentPlayer.currentSector
					.setSectorType(SectorType.CLOSED_RESCUE);
			game.lastPSclientNotification.setMessage(game.lastPSclientNotification.getMessage()
					+ "\n[GLOBAL MESSAGE]: "
					+ game.currentPlayer.name
					+ " has not escaped from aliens");
			return false;
		}

	}

}
