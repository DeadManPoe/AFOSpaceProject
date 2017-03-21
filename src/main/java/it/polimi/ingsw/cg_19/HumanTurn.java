package it.polimi.ingsw.cg_19;

import java.util.ArrayList;
import java.util.List;

import common.Action;
import common.DiscardAction;
import common.DrawSectorCardAction;
import common.DrawRescueCardAction;
import common.EndTurnAction;
import common.MoveAction;
import common.MoveAttackAction;
import common.SectorCard;
import common.SectorType;
import common.UseObjAction;
import common.UseSectorCardAction;

/**
 * Represents a human turn in the game
 * 
 * @see Turn
 * @author Andrea Sessa
 * @author Giorgio Pea
 * @version 1.0
 */
public class HumanTurn extends Turn {

	/**
	 * Constructs a human's turn in the game and sets its initial possible
	 * actions
	 * 
	 * @see Turn#Turn(Game)
	 */
	public HumanTurn(Game game) {
		super(game);
		this.initialActions = new ArrayList<Class<? extends Action>>();
		//this.initialActions.add(MoveAction.class);
		//this.initialActions.add(UseObjAction.class);
	}

	@Override
	public List<Class<? extends Action>> getNextActions(Action action) {
		List<Class<? extends Action>> nextActions = new ArrayList<Class<? extends Action>>();
		Class<? extends Action> actionType = action.getClass();

		// Actions to be set after a move action
		if (actionType.equals(MoveAction.class)) {
			//MoveAction move = (MoveAction) action;
			//SectorType sectorType = move.getTarget().getSectorType();

			if (sectorType == SectorType.DANGEROUS) {
				if (!game.getCurrentPlayer().isSedated()) {
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
				if (game.getCurrentPlayer().getPrivateDeck().getSize() == 4) {
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
			if (game.getCurrentPlayer().getPrivateDeck().getSize() > 0) {
				nextActions.add(UseObjAction.class);
			}
			if (!game.getCurrentPlayer().hasMoved()) {
				nextActions.add(MoveAction.class);
			} else {
				nextActions.add(EndTurnAction.class);
			}
		} else if (actionType.equals(DrawRescueCardAction.class)) {
			nextActions.add(EndTurnAction.class);
			nextActions.add(UseObjAction.class);
		} else if (actionType.equals(MoveAttackAction.class)) {
			if (game.getCurrentPlayer().getPrivateDeck().getSize() > 0) {
				nextActions.add(UseObjAction.class);
			}
			nextActions.add(EndTurnAction.class);
		}

		return nextActions;
	}

}
