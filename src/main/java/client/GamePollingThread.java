package client;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.TimerTask;

import client_store.InteractionManager;

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
		try {
			InteractionManager.getInstance().getGames();
		} catch (IOException e) {

		}
	}

}
