package it.polimi.ingsw.cg_19;

import java.util.ArrayList;
import java.util.List;

import common.*;

/**
 * Represents an alien turn in the game
 * 
 * @see Turn
 * @author Andrea Sessa
 * @author Giorgio Pea
 * @version 1.0
 */
public class AlienTurn extends Turn {

	/**
	 * Constructs an alien's turn in the game and sets its initial possible
	 * actions
	 * 
	 * @see Turn#Turn(Game)
	 */
	public AlienTurn(Game game) {
		super(game);
		this.initialActions = new ArrayList<Class<? extends Action>>();
		this.initialActions.add(MoveAction.class);
		this.initialActions.add(MoveAttackAction.class);
	}

	/**
	 * @see Turn#getNextActions(Action)
	 */
	@Override
	public List<Class<? extends Action>> getNextActions(Action action) {
		List<Class<? extends Action>> nextActions = new ArrayList<Class<? extends Action>>();
		Class<? extends Action> actionType = action.getClass();
		// Actions to be set after a move action
		if (actionType.equals(MoveAction.class)) {
			MoveAction moveAction = (MoveAction) action;
			if (moveAction.getTarget().getSectorType() == SectorType.DANGEROUS) {
				nextActions.add(DrawSectorCardAction.class);
			} else {
				nextActions.add("@GAMEACTION_END_TURN");
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
				if (game.getCurrentPlayer().getPrivateDeck().getSize() == 4) {
					nextActions.add(DiscardAction.class);
				} else {
					nextActions.add("@GAMEACTION_END_TURN");
				}
			} else {
				nextActions.add("@GAMEACTION_END_TURN");
			}

		}
		/*
		 * // DRAW Obj else if (actionType.equals(DrawActionFromObject.class)) {
		 * if (getGame().getCurrentPlayer().getPrivateDeck().getSize() == 3) {
		 * nextActions.add(DiscardAction.class); } else {
		 * //nextActions.add(AttackAction.class);
		 * nextActions.add("@GAMEACTION_END_TURN"); } }
		 */
		// Actions to be set after a discard object card action
		else if (actionType.equals(DiscardAction.class)) {
			nextActions.add("@GAMEACTION_END_TURN");
		}
		// Actions to be set after an attack action
		else if (actionType.equals(MoveAttackAction.class)) {
			nextActions.add("@GAMEACTION_END_TURN");
		}
		return nextActions;
	}

}