package client;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.TimerTask;

import client_store.ClientStore;
import client_store.InteractionManager;
import client_store_actions.ClientSetConnectionActiveAction;

/**
 * Represents a task that periodically query the server about the list of
 * available games and updates the gui
 * 
 * @author Andrea Sessa
 * @author Giorgio Pea
 */
public class GamePollingThread extends TimerTask {


	/**
	 * Updates the JTable with the list of games
	 */
	@Override
	public void run() {
		InteractionManager.getInstance().getGames();
	}

}
