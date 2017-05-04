package server;

import common.*;
import server_store.StoreAction;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by giorgiopea on 13/03/17.
 */
public class AlienTurn {

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
        switch (action.type) {
            case "@GAMEACTION_MOVE":
                MoveAction moveAction = (MoveAction) action;
                if (moveAction.getTargetSector().getSectorType().equals(SectorType.DANGEROUS)) {
                    nextActions.add("@GAMEACTION_DRAW_SECTOR_CARD");
                } else {
                    nextActions.add("@GAMEACTION_END_TURN");
                }
                break;
            // Actions to be set after a draw sector card action
            case "@GAMEACTION_DRAW_SECTOR_CARD":
                nextActions.add("@GAMEACTION_USE_SECTOR_CARD");
                break;
            // Actions to be set after a use sector card action
            case "@GAMEACTION_USE_SECTOR_CARD":
                SectorCard card = ((UseSectorCardAction) action).getSectorCard();
                if (card.hasObjectAssociated()) {
                    // Need to discard an object card
                    if (currentPlayer.getPrivateDeck().getSize() == 4) {
                        nextActions.add("@GAMEACTION_DISCARD_OBJ_CARD");
                    } else {
                        nextActions.add("@GAMEACTION_END_TURN");
                    }
                } else {
                    nextActions.add("@GAMEACTION_END_TURN");
                }

                break;
            // Actions to be set after a discard object card action
            case "@GAMEACTION_DISCARD_OBJ_CARD":
                nextActions.add("@GAMEACTION_END_TURN");
                break;
            // Actions to be set after an attack action
            case "@GAMEACTION_MOVE_ATTACK":
                nextActions.add("@GAMEACTION_END_TURN");
                break;
        }
        return nextActions;
    }
}
