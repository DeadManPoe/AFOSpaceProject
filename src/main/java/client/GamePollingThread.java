package client;

import java.util.TimerTask;

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
