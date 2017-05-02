package it.polimi.ingsw.cg_19;

import java.util.TimerTask;

/**
 * A thread that notifies an associated {@link Game}.
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
			e.printStackTrace();
		}
	}
}