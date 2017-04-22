package it.polimi.ingsw.cg_19;

import java.util.ArrayList;
import java.util.List;

import common.*;

/**
 * Represents an alien turn in the game
 *
 * @author Andrea Sessa
 * @author Giorgio Pea
 * @version 1.0
 */
public class AlienTurn {

    private static List<Class<? extends Action>> initialActions;

    public static List<Class<? extends Action>> getInitialActions() {
        if (initialActions == null) {
            initialActions = new ArrayList<>();
            initialActions.add(MoveAction.class);
            initialActions.add(MoveAttackAction.class);
        }
        return initialActions;
    }

    public static List<Class<? extends Action>> nextAction(Action action, Player currentPlayer) {
        List<Class<? extends Action>> nextActions = new ArrayList<>();
        // Actions to be set after a move action
        if (action.getClass().equals(MoveAction.class)) {
            MoveAction moveAction = (MoveAction) action;
            if (moveAction.getTarget().getSectorType() == SectorType.DANGEROUS) {
                nextActions.add(DrawSectorCardAction.class);
            } else {
                nextActions.add(EndTurnAction.class);
            }
        }
        // Actions to be set after a draw sector card action
        else if (action.getClass().equals(DrawSectorCardAction.class)) {
            nextActions.add(UseSectorCardAction.class);
        }
        // Actions to be set after a use sector card action
        else if (action.getClass().equals(UseSectorCardAction.class)) {
            SectorCard card = ((UseSectorCardAction) action).getCard();
            if (card.hasObjectAssociated()) {
                // Need to discard an object card
                if (currentPlayer.getPrivateDeck().getSize() == 4) {
                    nextActions.add(DiscardAction.class);
                } else {
                    nextActions.add(EndTurnAction.class);
                }
            } else {
                nextActions.add(EndTurnAction.class);
            }

        }
        // Actions to be set after a discard object card action
        else if (action.getClass().equals(DiscardAction.class)) {
            nextActions.add(EndTurnAction.class);
        }
        // Actions to be set after an attack action
        else if (action.getClass().equals(MoveAttackAction.class)) {
            nextActions.add(EndTurnAction.class);
        }
        return nextActions;
    }
}