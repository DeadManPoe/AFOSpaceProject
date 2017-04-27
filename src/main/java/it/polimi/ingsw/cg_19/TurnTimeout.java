package it.polimi.ingsw.cg_19;

import java.util.TimerTask;
import java.util.logging.Level;

/**
 * Represents a thread that waits for a given time(in seconds)
 * 
 * @author Andrea Sessa
 * @author Giorgio Pea
 * @version 1.0
 */
public class TurnTimeout extends TimerTask {
	private Game game;

	public TurnTimeout(Game game) {
		this.game = game;
	}

	public Game getGame() {
		return this.game;
	}

	@Override
	public void run() {
		try {
			game.timeoutUpdate();
		} catch (InstantiationException | IllegalAccessException e) {
			ClientLogger.getLogger().log(Level.SEVERE, e.getMessage(), e);
		}
	}
}