package it.polimi.ingsw.cg_19;

import java.util.List;

import common.Action;

/**
 * Represents a generic turn in the game,
 * 
 * @author Andrea Sessa
 * @author Giorgio Pea
 */
public interface Turn {
	// The game the turn refers to
	//protected final Game game;
	// The list of the possible initial actions for the turn in terms of class
	// names
	//protected List<Class<? extends Action>> initialActions;

	/**
	 * Constructs a generic turn in the game. This turn is constructed from a
	 * game
	 * 
	 * @param game
	 *            the game the turn refers to
	 */
	//public Turn(Game game) {
		//this.game = game;
	//}

	/**
	 * Gets the game this turn refers to
	 * 
	 * @return the game this turn refers to
	 */
	//public Game getGame() {
		//return game;
	//}

	/**
	 * Gets the list of possible initial actions defined for the turn
	 * 
	 * @return the list of possible initial actions defined for the turn
	 */
	//public List<Class<? extends Action>> getInitialActions() {
		//return this.initialActions;
	//}

	/**
	 * Gets the list of actions that follow the given one in the turn
	 * 
	 * @param action
	 *            the action that has been made by a player
	 * @return the list of actions that follow the above mentioned in the turn.
	 *         If the action above mentioned is null, an empty list is return
	 */
	//public abstract static List<Class<? extends Action>> getNextActions(Action action);

	public static List<Class<? extends Action>> getInitialAction();
}
