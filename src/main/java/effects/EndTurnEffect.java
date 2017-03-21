package effects;

import common.EndTurnAction;
import common.PSClientNotification;
import common.RRClientNotification;
import it.polimi.ingsw.cg_19.PlayerState;
import server_store.Game;
import server_store.ServerState;

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

    public static boolean executeEffect(Game game, EndTurnAction action) {
        game.currentPlayer.isAdrenalined = false;
        game.currentPlayer.isSedated = false;
        game.currentPlayer.hasMoved = false;
        // Set the new current player
        shiftCurrentplayer(game);
        // Notify the client
        if (game.currentPlayer.playerState != PlayerState.ESCAPED)
            game.lastRRclientNotification.setMessage("You have ended your turn now wait until its your turn");
        game.lastAction = action;
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
