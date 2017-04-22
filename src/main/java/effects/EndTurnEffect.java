package effects;

import java.util.ArrayList;

import common.*;
import it.polimi.ingsw.cg_19.AlienTurn;
import it.polimi.ingsw.cg_19.Game;
import it.polimi.ingsw.cg_19.HumanTurn;
import it.polimi.ingsw.cg_19.Player;
import it.polimi.ingsw.cg_19.PlayerState;
import it.polimi.ingsw.cg_19.PlayerType;

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
    public static boolean executeEffect(Game game,
                                        RRClientNotification rrNotification,
                                        PSClientNotification psNotification, Action action) {
        EndTurnAction castedAction = (EndTurnAction) action;
        Player currentPlayer = game.getCurrentPlayer();
        currentPlayer.setAdrenalined(false);
        currentPlayer.setSedated(false);
        currentPlayer.setHasMoved(false);
        rrNotification.setMessage("\nYou have ended your turn");
        psNotification.setMessage("\n[GLOBAL MESSAGE]: "
                + currentPlayer.getName()
                + " has ended its turn.\n[GLOBAL MESSAGE]: ");

        shiftCurrentplayer(game);
        psNotification.setMessage(psNotification.getMessage() + currentPlayer.getName() + " now is your turn");
        // Notify the client
        game.setLastAction(castedAction);

        return true;
    }

    private static void shiftCurrentplayer(Game game) {
        int currentPlayerIndex = game.getPlayers().indexOf(game.getCurrentPlayer());
        do {
            currentPlayerIndex++;
            if (currentPlayerIndex == game.getPlayers().size())
                currentPlayerIndex = 0;
        } while (game.getPlayers().get(currentPlayerIndex).getPlayerState().equals(PlayerState.DEAD));
        game.setPreviousPlayer(game.getCurrentPlayer());
        game.setCurrentPlayer(game.getPlayers().get(currentPlayerIndex));
    }


}
