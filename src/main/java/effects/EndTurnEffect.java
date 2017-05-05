package effects;

import common.*;
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
        game.getCurrentPlayer().setAdrenalined(false);
        game.getCurrentPlayer().setSedated(false);
        game.getCurrentPlayer().setHasMoved(false);
        game.getLastRRclientNotification().setMessage("\nYou have ended your turn");
        game.getLastPSclientNotification().setMessage("\n[GLOBAL MESSAGE]: "
                + game.getCurrentPlayer().getName()
                + " has ended its turn.\n[GLOBAL MESSAGE]: ");
        game.setPreviousPlayer(game.getCurrentPlayer());
        shiftCurrentPlayer(game);
        game.getLastPSclientNotification().setMessage(game.getLastPSclientNotification().getMessage() + game.getCurrentPlayer().getName() + " now is your turn");
        // Notify the client
        game.setLastAction(action);
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
