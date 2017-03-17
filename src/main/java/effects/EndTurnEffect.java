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
	public boolean executeEffect(server_store.Game game,
								 RRClientNotification clientNotification,
								 PSClientNotification psNotification) {
		server_store.Player currentPlayer = game.currentPlayer;
		currentPlayer.isAdrenalined = false;
		currentPlayer.isSedated = false;
		currentPlayer.hasMoved = false;
		// Set the new current player
		shiftCurrentplayer(game);

		// Notify the client
		if(game.currentPlayer.playerState != PlayerState.ESCAPED)
			clientNotification.setMessage("You have ended your turn now wait until its your turn");
		psNotification.setMessage(psNotification.getMessage()+"\n[GLOBAL MESSAGE]: "
				+ currentPlayer.name
				+ " has ended its turn.\n[GLOBAL MESSAGE]: "
				+ game.currentPlayer.name + " now is your turn");
		game.lastAction = action;
		ArrayList<Object> parameters = new ArrayList<Object>();
		//parameters.add(game.fromPlayerToToken(game.currentPlayer));
		//game.notifyListeners(new RemoteMethodCall("allowTurn", parameters));
		return true;
	}

	private void shiftCurrentplayer(server_store.Game game) {
		int currentPlayerIndex = game.players.indexOf(game.currentPlayer);
		do {
			currentPlayerIndex++;
			if (currentPlayerIndex == game.players.size())
				currentPlayerIndex = 0;
		} while (game.players.get(currentPlayerIndex).playerState == PlayerState.DEAD);

		game.currentPlayer = game.players.get(currentPlayerIndex);
	}

}
