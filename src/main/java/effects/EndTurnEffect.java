package effects;

import common.EndTurnAction;
import common.PSClientNotification;
import common.RRClientNotification;
import common.PlayerState;
import server.Game;
import server_store.StoreAction;

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
        game.lastRRclientNotification = new RRClientNotification();
        game.lastPSclientNotification = new PSClientNotification();
        game.lastRRclientNotification.setMessage("\nYou have ended your turn");
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

        game.previousPlayer = game.currentPlayer;
        game.currentPlayer = game.players.get(currentPlayerIndex);
    }

}
