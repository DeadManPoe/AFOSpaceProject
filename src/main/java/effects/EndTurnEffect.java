package effects;

import java.util.ArrayList;

import it.polimi.ingsw.cg_19.AlienTurn;
import it.polimi.ingsw.cg_19.Game;
import it.polimi.ingsw.cg_19.HumanTurn;
import it.polimi.ingsw.cg_19.Player;
import it.polimi.ingsw.cg_19.PlayerState;
import it.polimi.ingsw.cg_19.PlayerType;
import common.EndTurnAction;
import common.PSClientNotification;
import common.RRClientNotification;
import common.RemoteMethodCall;

/**
 * Represents the effect associated to the action of ending a turn.
 * 
 * @see ActionEffect
 * @see EndTurnAction
 * @author Andrea Sessa
 * @author Giorgio Pea
 * 
 * @version 1.1
 */

public class EndTurnEffect extends ActionEffect {

	/**
	 * Constructs the effect associated to the action of ending a turn. The
	 * effect is constructed from a {@link common.EndTurnAction }
	 * 
	 * @param endTurnAction
	 *            the {@link EndTurnAction } that needs to be enriched with its
	 *            effect
	 */
	public EndTurnEffect(EndTurnAction endTurnAction) {
		super(endTurnAction);
	}

	/**
	 * Constructs the effect associated to the action of ending a turn. The
	 * effect is constructed from a {@link common.EndTurnAction } that is null.
	 * This constructor is used only for test purposes.
	 * 
	 */
	public EndTurnEffect() {
		super(null);
	}

	@Override
	public boolean executeEffect(Game game,
			RRClientNotification clientNotification,
			PSClientNotification psNotification) {
		Player currentPlayer = game.getCurrentPlayer();
		currentPlayer.setAdrenaline(false);
		currentPlayer.setSedated(false);
		currentPlayer.setHasMoved(false);
		// Set the new current player
		game.shiftCurrentplayer();

		if (game.getCurrentPlayer().getPlayerType() == PlayerType.HUMAN) {
			game.setTurn(new HumanTurn(game));
		} else {
			game.setTurn(new AlienTurn(game));
		}
		// Notify the client
		if(game.getCurrentPlayer().getPlayerState() != PlayerState.ESCAPED) 
			clientNotification.setMessage("You have ended your turn now wait until its your turn");
		psNotification.setMessage(psNotification.getMessage()+"\n[GLOBAL MESSAGE]: "
				+ currentPlayer.getName()
				+ " has ended its turn.\n[GLOBAL MESSAGE]: "
				+ game.getCurrentPlayer().getName() + " now is your turn");
		game.setLastAction(action);
		ArrayList<Object> parameters = new ArrayList<Object>();
		parameters.add(game.fromPlayerToToken(game.getCurrentPlayer()));
		game.notifyListeners(new RemoteMethodCall("allowTurn", parameters));
		return true;
	}

}
