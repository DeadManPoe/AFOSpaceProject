package it.polimi.ingsw.cg_19;

import java.util.ArrayList;
import java.util.List;

import common.*;
import common.SectorgetClass();

/**
 * Represents a human turn in the game
 * 
 * @see Turn
 * @author Andrea Sessa
 * @author Giorgio Pea
 * @version 1.0
 */
public class HumanTurn implements Turn {

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
			List<Class<? extends Action>> nextActions = new ArrayList<>();

			// Actions to be set after a moveToSector action
			if (action.getClass().equals(MoveAction.class)) {
				MoveAction move = (MoveAction) action;
				SectorType sectorType = move.getTarget().getSectorType();

				if (sectorType == SectorType.DANGEROUS) {
					if (!currentPlayer.isSedated()) {
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
			else if (action.getClass().equals(DrawSectorCardAction.class)) {
				nextActions.add(UseSectorCardAction.class);
			}
			// Actions to be set after a use sector card action
			else if (action.getClass().equals(UseSectorCardAction.class)) {
				SectorCard card = ((UseSectorCardAction) action).getCard();
				// If the sector card used has an object
				if (card.hasObjectAssociated()) {
					if (currentPlayer.getPrivateDeck().getSize() == 4) {
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
			else if (action.getClass().equals(DiscardAction.class)) {
				nextActions.add(UseObjAction.class);
				nextActions.add(EndTurnAction.class);
			}
			// Actions to be set after a use object card action
			else if (action.getClass().equals(UseObjAction.class)) {
				if (currentPlayer.getPrivateDeck().getSize() > 0) {
					nextActions.add(UseObjAction.class);
				}
				if (!currentPlayer.isHasMoved()) {
					nextActions.add(MoveAction.class);
				} else {
					nextActions.add(EndTurnAction.class);
				}
			} else if (action.getClass().equals(DrawRescueCardAction.class)) {
				nextActions.add(EndTurnAction.class);
				nextActions.add(UseObjAction.class);
			} else if (action.getClass().equals(MoveAttackAction.class)) {
				if (currentPlayer.getPrivateDeck().getSize() > 0) {
					nextActions.add(UseObjAction.class);
				}
				nextActions.add(EndTurnAction.class);
			}
			return nextActions;
		}
}
