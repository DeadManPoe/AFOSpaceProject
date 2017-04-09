package server_store;

import common.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by giorgiopea on 13/03/17.
 */
public class HumanTurn extends Turn {

    private static List<String> initialActions;
    public static List<String> getInitialActions(){
        if(initialActions == null){
            initialActions = new ArrayList<String>();
            initialActions.add("@GAMEACTION_MOVE");
            initialActions.add("@GAMEACTION_USE_OBJ_CARD");
        }
        return initialActions;
    }
    public static List<String> nextAction(StoreAction action, Player currentPlayer){
        List<String> nextActions = new ArrayList<>();

        // Actions to be set after a moveToSector action
        if (action.type.equals("@GAMEACTION_MOVE")) {
            MoveAction move = (MoveAction) action;
            SectorType sectorType = move.payload.getSectorType();

            if (sectorType == SectorType.DANGEROUS) {
                if (!currentPlayer.isSedated) {
                    nextActions.add("@GAMEACTION_DRAW_SECTOR_CARD");
                }
                nextActions.add("@GAMEACTION_USE_OBJ_CARD");
                nextActions.add("@GAMEACTION_END_TURN");
            } else if (sectorType == SectorType.OPEN_RESCUE) {
                nextActions.add("@GAMEACTION_DRAW_RESCUE_CARD");
            } else {
                nextActions.add("@GAMEACTION_USE_OBJ_CARD");
                nextActions.add("@GAMEACTION_END_TURN");
            }
        }
        // Actions to be set after a draw sector card action
        else if (action.type.equals("@GAMEACTION_DRAW_SECTOR_CARD")) {
            nextActions.add("@GAMEACTION_USE_SECTOR_CARD");
        }
        // Actions to be set after a use sector card action
        else if (action.type.equals("@GAMEACTION_USE_SECTOR_CARD")) {
            SectorCard card = ((UseSectorCardAction) action).payload;
            // If the sector card used has an object
            if (card.hasObjectAssociated()) {
                if (currentPlayer.privateDeck.getSize() == 4) {
                    nextActions.add("@GAMEACTION_DISCARD_OBJ_CARD");
                } else {
                    nextActions.add("@GAMEACTION_END_TURN");
                }
            } else {
                nextActions.add("@GAMEACTION_END_TURN");
            }
            nextActions.add("@GAMEACTION_USE_OBJ_CARD");
        }

        // Actions to be set after a discard object card action
        else if (action.type.equals("@GAMEACTION_DISCARD_OBJ_CARD")) {
            nextActions.add("@GAMEACTION_USE_OBJ_CARD");
            nextActions.add("@GAMEACTION_END_TURN");
        }
        // Actions to be set after a use object card action
        else if (action.type.equals("@GAMEACTION_USE_OBJ_CARD")) {
            if (currentPlayer.privateDeck.getSize() > 0) {
                nextActions.add("@GAMEACTION_USE_OBJ_CARD");
            }
            if (!currentPlayer.hasMoved) {
                nextActions.add("@GAMEACTION_MOVE");
            } else {
                nextActions.add("@GAMEACTION_END_TURN");
            }
        } else if (action.type.equals("@GAMEACTION_DRAW_RESCUE_CARD")) {
            nextActions.add("@GAMEACTION_END_TURN");
            nextActions.add("@GAMEACTION_USE_OBJ_CARD");
        } else if (action.type.equals("@GAMEACTION_MOVE_ATTACK")) {
            if (currentPlayer.privateDeck.getSize() > 0) {
                nextActions.add("@GAMEACTION_USE_OBJ_CARD");
            }
            nextActions.add("@GAMEACTION_END_TURN");
        }
        return nextActions;
    }
}
