package effects;

import common.EndTurnAction;
import common.PSClientNotification;
import common.RRClientNotification;
import it.polimi.ingsw.cg_19.PlayerState;
import server_store.Game;
import server_store.ServerState;
import server_store.StoreAction;

import java.util.ArrayList;

/**
 * Represents the effect associated to the action of ending a turn.
 *
 * @author Andrea Sessa
 * @author Giorgio Pea
 * @version 1.1
 * @see ActionEffect
 * @see EndTurnAction
 */

public class EndTurnEffect extends ActionEffect {

    public static boolean executeEffect(Game game, StoreAction action) {
        EndTurnAction castedAction = (EndTurnAction) action;
        game.currentPlayer.isAdrenalined = false;
        game.currentPlayer.isSedated = false;
        game.currentPlayer.hasMoved = false;
        // Set the new current player


        game.lastPSclientNotification = new PSClientNotification();
        game.lastPSclientNotification.setMessage("\n[GLOBAL MESSAGE]: "
                + game.currentPlayer.name
                + " has ended its turn.\n[GLOBAL MESSAGE]: ");

        shiftCurrentplayer(game);
        game.lastPSclientNotification.setMessage(game.lastPSclientNotification.getMessage() + game.currentPlayer.name + " now is your turn");

        // Notify the client
        game.lastAction = castedAction;
        return true;
    }

    private static void shiftCurrentplayer(Game game) {
        int currentPlayerIndex = game.players.indexOf(game.currentPlayer);
        do {
            currentPlayerIndex++;
            if (currentPlayerIndex == game.players.size())
                currentPlayerIndex = 0;
        } while (game.players.get(currentPlayerIndex).playerState == PlayerState.DEAD);

        game.currentPlayer = game.players.get(currentPlayerIndex);
    }

}
