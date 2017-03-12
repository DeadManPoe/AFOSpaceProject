package server_store;

import common.ClientNotification;
import common.EndTurnAction;
import common.PSClientNotification;
import common.RRClientNotification;
import effects.ActionEffect;
import it.polimi.ingsw.cg_19.Player;
import it.polimi.ingsw.cg_19.PlayerType;
import it.polimi.ingsw.cg_19.TurnTimeout;
import sts.Action;
import sts.Reducer;

import java.util.List;
import java.util.Timer;

/**
 * Created by giorgiopea on 12/03/17.
 */
public class GameReducer extends Reducer {
    public GameReducer(List<String> writableStateKeys) {
        super(writableStateKeys);
    }

    @Override
    public ServerState reduce(Action action, ServerState state) {
        if(action.type.equals("@GAME_DISCARD_OBJ_CARD")){
            GameMakeAction gameMakeAction = (GameMakeAction) action.payload;

        }
        else if(action.type.equals("@GAME_DRAW_OBJ_CARD")){

        }
    }


    private ServerState makeAction(GameMakeAction gameMakeAction, ServerState state){
        //Prendiamoci il player e verifichiamo se è lui il player corrente
        Player actualPlayer = playerTokenToPlayerMap.get(playerToken);
        // if(turn.getInitialAction().contains(action.class)) &&
        if (!currentPlayer.equals(actualPlayer)) {
            clientNotification.setActionResult(false);
        } else {
            // If the player is ok then checks if the action is ok
            if (nextActions.contains(action.getClass()) || forced) {
                // Retrieve the related effect
                ActionEffect effect = actionMapper.getEffect(action);

                // Executes the effect and get the result
                boolean actionResult = effect.executeEffect(this,
                        clientNotification, psNotification);

                if (actionResult) {
					/*
					 * If the last action has been and an end turn action the
					 * there is no need to update the nextAction field
					 */
                    if (!lastAction.getClass().equals(EndTurnAction.class)) {
                        nextActions = turn.getNextActions(lastAction);
                    } else {

                        nextActions = turn.getInitialActions();
                        turnNumber++;

                        // Reset the timeout

                        timer.purge();
                        timer.cancel();
                        timeout.cancel();
                        timer = new Timer();
                        timeout = new TurnTimeout(this, timer);
                        timer.schedule(timeout, TURN_TIMEOUT);
                    }
                    boolean winH = checkWinConditions(PlayerType.HUMAN);
                    boolean winA = checkWinConditions(PlayerType.ALIEN);

                    if (winH) {
                        psNotification
                                .setMessage(psNotification.getMessage()
                                        + "\n[GLOBAL MESSAGE]: The game has ended, HUMANS WIN!");
                    }
                    if (winA) {
                        psNotification
                                .setMessage(psNotification.getMessage()
                                        + "\n[GLOBAL MESSAGE]: The game has ended, ALIENS WIN!");

                    }
                    if (winH || winA) {
                        psNotification.setAlienWins(winA);
                        psNotification.setHumanWins(winH);
                        clientNotification.setActionResult(true);
                        ClientNotification[] toReturn = { clientNotification,
                                psNotification };
                        this.gameManager.removeGame(this);
                        return toReturn;
                    }
                    clientNotification.setActionResult(true);
                }
            } else {
                clientNotification.setActionResult(false);
            }

        }
        ClientNotification[] toReturn = { clientNotification, psNotification };
        return toReturn;
    }
}
