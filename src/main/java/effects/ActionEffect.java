package effects;

import it.polimi.ingsw.cg_19.Game;
import common.*;

/**
 * Represents the effect associated with an action
 * 
 * @see Action
 * @author Andrea Sessa
 * @author Giorgio Pea
 * @version 1.0
 */
public abstract class ActionEffect {
	// The action the effect refers to
	protected Action action;

	/**
	 * Constructs an effect associated with an action. This effect is
	 * constructed from an action.
	 * 
	 * @param action
	 *            the action associated with the effect
	 */
	public ActionEffect(Action action) {
		this.action = action;
	}

	/**
	 * Defines and executes the effect associated with an action.
	 * 
	 * @param game
	 *            the game this action effect refers to
	 * @param rrNotification
	 *            the notification to be sent to the client (through
	 *            Request/Response protocol) as a response to its request
	 * @param psNotification
	 *            the notification to be sent to all the subscribers of the game
	 *            above mentioned(through Publisher/Subscriber protocol)
	 * @return true if the action has been executed properly
	 */
	public abstract boolean executeEffect(server_store.Game game,
										  RRClientNotification rrNotification,
										  PSClientNotification psNotification);

	/**
	 * Sets the action the effect refers to
	 * 
	 * @param action
	 *            the new action to be associated with the effect
	 */
	public void setAction(Action action) {
		this.action = action;
	}

	/**
	 * Gets the action the effect refers to
	 * 
	 * @return the action the effect refers to
	 */
	public Action getAction() {
		return this.action;
	}

}