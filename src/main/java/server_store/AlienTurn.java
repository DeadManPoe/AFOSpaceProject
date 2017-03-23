package server_store;

import common.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by giorgiopea on 13/03/17.
 */
public class AlienTurn extends Turn {

    private static List<String> initialActions;
    public static List<String> getInitialActions(){
        if(initialActions == null){
            initialActions = new ArrayList<String>();
            initialActions.add("@GAMEACTION_MOVE");
            initialActions.add("@GAMEACTION_MOVE_ATTACK");
        }
        return initialActions;
    }

    public static List<String> nextAction(StoreAction action, Player currentPlayer) {
        List<String> nextActions = new ArrayList<String>();
        // Actions to be set after a move action
        if (action.type.equals("@GAMEACTION_MOVE")) {
            MoveAction moveAction = (MoveAction) action;
            if (moveAction.payload.getSectorType() == SectorType.DANGEROUS) {
                nextActions.add("@GAMEACTION_DRAW_SECTOR_CARD");
            } else {
                nextActions.add("@GAMEACTION_END_TURN");
            }
        }
        // Actions to be set after a draw sector card action
        else if (action.type.equals(DrawSectorCardAction.class)) {
            nextActions.add("@GAMEACTION_USE_SECTOR_CARD");
        }
        // Actions to be set after a use sector card action
        else if (action.type.equals("@GAMEACTION_USE_SECTOR_CARD")) {
            SectorCard card = ((UseSectorCardAction) action).payload;
            if (card.hasObjectAssociated()) {
                // Need to discard an object card
                if (currentPlayer.privateDeck.getSize() == 4) {
                    nextActions.add("@GAMEACTION_DISCARD_OBJ_CARD");
                } else {
                    nextActions.add("@GAMEACTION_END_TURN");
                }
            } else {
                nextActions.add("@GAMEACTION_END_TURN");
            }

        }
		/*
		 * // DRAW Obj else if (action.type.equals(DrawActionFromObject.class)) {
		 * if (getGame().getCurrentPlayer().getPrivateDeck().getSize() == 3) {
		 * nextActions.add(DiscardAction.class); } else {
		 * //nextActions.add(AttackAction.class);
		 * nextActions.add("@GAMEACTION_END_TURN"); } }
		 */
        // Actions to be set after a discard object card action
        else if (action.type.equals("@GAMEACTION_DISCARD_OBJ_CARD")) {
            nextActions.add("@GAMEACTION_END_TURN");
        }
        // Actions to be set after an attack action
        else if (action.type.equals("@GAMEACTION_MOVE_ATTACK")) {
            nextActions.add("@GAMEACTION_END_TURN");
        }
        return nextActions;
    }
}
