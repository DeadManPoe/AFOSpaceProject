package server_store;

import common.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by giorgiopea on 13/03/17.
 */
public class HumanTurn extends Turn {

    private static List<Class<? extends Action>> initialActions;
    public static List<Class<? extends Action>> getInitialActions(){
        if(initialActions == null){
            initialActions = new ArrayList<Class<? extends Action>>();
            initialActions.add(MoveAction.class);
            initialActions.add(UseObjAction.class);
        }
        return initialActions;
    }
    public static List<Class<? extends Action>> nextAction(Action action, Player currentPlayer){
        List<Class<? extends Action>> nextActions = new ArrayList<Class<? extends Action>>();
        Class<? extends Action> actionType = action.getClass();

        // Actions to be set after a move action
        if (actionType.equals(MoveAction.class)) {
            MoveAction move = (MoveAction) action;
            SectorType sectorType = move.getTarget().getSectorType();

            if (sectorType == SectorType.DANGEROUS) {
                if (!currentPlayer.isSedated) {
                    nextActions.add(DrawSectorCardAction.class);
                }
                nextActions.add(UseObjAction.class);
                nextActions.add(EndTurnAction.class);
            } else if (sectorType == SectorType.OPEN_RESCUE) {
                nextActions.add(DrawRescueCardAction.class);
            } else {
                nextActions.add(UseObjAction.class);
                nextActions.add(EndTurnAction.class);
            }
        }
        // Actions to be set after a draw sector card action
        else if (actionType.equals(DrawSectorCardAction.class)) {
            nextActions.add(UseSectorCardAction.class);
        }
        // Actions to be set after a use sector card action
        else if (actionType.equals(UseSectorCardAction.class)) {
            SectorCard card = ((UseSectorCardAction) action).getCard();
            // If the sector card used has an object
            if (card.hasObjectAssociated()) {
                if (currentPlayer.privateDeck.getSize() == 4) {
                    nextActions.add(DiscardAction.class);
                } else {
                    nextActions.add(EndTurnAction.class);
                }
            } else {
                nextActions.add(EndTurnAction.class);
            }
            nextActions.add(UseObjAction.class);
        }

        // Actions to be set after a discard object card action
        else if (actionType.equals(DiscardAction.class)) {
            nextActions.add(UseObjAction.class);
            nextActions.add(EndTurnAction.class);
        }
        // Actions to be set after a use object card action
        else if (actionType.equals(UseObjAction.class)) {
            if (currentPlayer.privateDeck.getSize() > 0) {
                nextActions.add(UseObjAction.class);
            }
            if (currentPlayer.hasMoved) {
                nextActions.add(MoveAction.class);
            } else {
                nextActions.add(EndTurnAction.class);
            }
        } else if (actionType.equals(DrawRescueCardAction.class)) {
            nextActions.add(EndTurnAction.class);
            nextActions.add(UseObjAction.class);
        } else if (actionType.equals(MoveAttackAction.class)) {
            if (currentPlayer.privateDeck.getSize() > 0) {
                nextActions.add(UseObjAction.class);
            }
            nextActions.add(EndTurnAction.class);
        }
        return nextActions;
    }
}
