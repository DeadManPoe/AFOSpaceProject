package effects;

import common.*;
import it.polimi.ingsw.cg_19.Game;
import it.polimi.ingsw.cg_19.Player;

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

        shiftCurrentPlayer(game);
        psNotification.setMessage(psNotification.getMessage() + currentPlayer.getName() + " now is your turn");
        // Notify the client
        game.setLastAction(castedAction);

        return true;
    }

    private static void shiftCurrentPlayer(Game game) {
        int size = game.getPlayers().size();
        int index = 0;
        while (index != size){
            Player current = game.getPlayers().get(index);
            if (current.equals(game.getCurrentPlayer())){
                if ( index == size - 1){
                    index = 0;
                    break;
                }
                index++;
                break;
            }
            index++;
        }
        game.setCurrentPlayer(game.getPlayers().get(index));
    }


}
