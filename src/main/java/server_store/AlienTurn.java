package server_store;

import common.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by giorgiopea on 13/03/17.
 */
public class AlienTurn extends Turn {

    public AlienTurn() {
        this.initialActions = new ArrayList<Class<? extends Action>>();
        this.initialActions.add(MoveAction.class);
        this.initialActions.add(MoveAttackAction.class);
    }

    public static List<Class<? extends Action>> nextAction(Action action, Player currentPlayer) {
        List<Class<? extends Action>> nextActions = new ArrayList<Class<? extends Action>>();
        Class<? extends Action> actionType = action.getClass();
        // Actions to be set after a move action
        if (actionType.equals(MoveAction.class)) {
            MoveAction moveAction = (MoveAction) action;
            if (moveAction.getTarget().getSectorType() == SectorType.DANGEROUS) {
                nextActions.add(DrawSectorCardAction.class);
            } else {
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
            if (card.hasObjectAssociated()) {
                // Need to discard an object card
                if (currentPlayer.privateDeck.getSize() == 4) {
                    nextActions.add(DiscardAction.class);
                } else {
                    nextActions.add(EndTurnAction.class);
                }
            } else {
                nextActions.add(EndTurnAction.class);
            }

        }
		/*
		 * // DRAW Obj else if (actionType.equals(DrawActionFromObject.class)) {
		 * if (getGame().getCurrentPlayer().getPrivateDeck().getSize() == 3) {
		 * nextActions.add(DiscardAction.class); } else {
		 * //nextActions.add(AttackAction.class);
		 * nextActions.add(EndTurnAction.class); } }
		 */
        // Actions to be set after a discard object card action
        else if (actionType.equals(DiscardAction.class)) {
            nextActions.add(EndTurnAction.class);
        }
        // Actions to be set after an attack action
        else if (actionType.equals(MoveAttackAction.class)) {
            nextActions.add(EndTurnAction.class);
        }
        return nextActions;
    }
}
