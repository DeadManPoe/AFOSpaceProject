package server;

import it.polimi.ingsw.cg_19.Game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;

import client.GameTimeout;
import common.PlayerToken;
import common.RemoteMethodCall;

/**
 * Represents a manager of all the games running on the server, it also offers
 * services to build new games and destroy old ones
 * 
 * @see PlayerToken
 * @see Game
 * @author Andrea Sessa
 * @author Giorgio Pea
 */
public class GameManager {
	// The only game manager instance (singleton pattern)
	private static final GameManager instance = new GameManager();
	private final List<Game> games;

	/**
	 * Constructs a manager of all the games running or to be run on the server,
	 * two empty maps that associate a game id with a game and a player token
	 * with a player are automatically created as well.
	 */
	private GameManager() {
		this.games = new ArrayList<>();
	}

	/**
	 * Gets the only game manager instance(pattern singleton)
	 * 
	 * @return the only game manager instance(pattern singleton)
	 */
	public static GameManager getInstance() {
		return instance;
	}

	/**
	 * Gets a game from its id
	 * 
	 * @param gameId
	 *            the id of the game to be returned
	 * @return the game that corresponds to the given id
	 */
	public Game getGame(int gameId) {
		for (Game game : this.games){
			if (game.getId() == gameId){
				return game;
			}
		}
	}

	/**
	 * Adds a game to the games managed by the game manager. The game manager's
	 * map between a game-id and a game is updated. A timer is started to manage
	 * the starting of the game
	 * 
	 * @see Game
	 * @param game
	 *            the game to be inserted among the games managed by the game
	 *            manager
	 */
	public void addGame(Game game) {
        this.games.add(game);
	}

	/**
	 * Gets the list of all the games managed by the game manager
	 * 
	 * @return the list of all the games managed by the game manager
	 */
	public List<Game> getGames() {
        return this.games;
    }
	/**
	 * Removes a game from the list of all the games managed by the game manager
	 * updating also the game manager's maps
	 * 
	 * @param game
	 *            the game to be removed from the list of all the games managed
	 *            by the game manager
	 */
	public void endGame(Game game) {
        this.games.remove(game);
	}
}
