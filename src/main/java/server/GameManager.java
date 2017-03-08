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
	private static final int GAME_TIMEOUT = 10 * 1000 * 60;

	// From game id to game to game
	private volatile Map<Integer, Game> fromGameIdToGame;
	// From player token to game
	private volatile Map<PlayerToken, Game> fromPlayerTokenToGame;
	// The only game manager instance (singleton pattern)
	private static final GameManager instance = new GameManager();

	/**
	 * Constructs a manager of all the games running or to be run on the server,
	 * two empty maps that associate a game id with a game and a player token
	 * with a player are automatically created as well.
	 */
	private GameManager() {
		this.fromGameIdToGame = new HashMap<Integer, Game>();
		this.fromPlayerTokenToGame = new HashMap<PlayerToken, Game>();
	}

	/**
	 * Gets the only game manager instance(pattern singleton)
	 * 
	 * @return the only game manager instance(pattern singleton)
	 */
	public static GameManager getInstance() {
		return GameManager.instance;
	}

	/**
	 * Gets a game from its id
	 * 
	 * @param gameId
	 *            the id of the game to be returned
	 * @return the game that corresponds to the given id
	 */
	public Game getGame(int gameId) {
		return this.fromGameIdToGame.get(gameId);
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
		Timer timer = new Timer();
		GameTimeout time = new GameTimeout(this, game, timer);
		timer.schedule(time, GAME_TIMEOUT);
		game.setGameManager(this);
		this.fromGameIdToGame.put(game.getPublicData().getId(), game);
	}

	/**
	 * Adds a game to the games managed by the game manager. The game manager's
	 * map between a game id and a game is updated. No timer is started. This
	 * method is only used for test purposes
	 * 
	 * @see Game
	 * @param game
	 *            the game to be inserted among the games managed by the game
	 *            manager
	 */
	public void addGameNoTimer(Game game) {
		game.setGameManager(this);
		this.fromGameIdToGame.put(game.getPublicData().getId(), game);
	}

	/**
	 * Updates the game manager's map between a player token and a gameid
	 * 
	 * @param playerToken
	 *            a player token to be inserted in the game manager's map
	 *            between a player token and a gameid
	 * @param gameId
	 *            a game id that corresponds to the above mentioned player token
	 */
	public void addPlayerToGame(PlayerToken playerToken, int gameId) {
		Game game = this.getGame(gameId);
		fromPlayerTokenToGame.put(playerToken, game);
	}

	/**
	 * Gets the list of all the games managed by the game manager
	 * 
	 * @return the list of all the games managed by the game manager
	 */
	public List<Game> getGames() {
		return new ArrayList<Game>(this.fromGameIdToGame.values());
	}

	/**
	 * Gets the game associated with a given player token
	 * 
	 * @param playerToken
	 *            the player token whose associated game is to get
	 * @return the game associated with the given player token
	 */
	public Game getGame(PlayerToken playerToken) {
		return this.fromPlayerTokenToGame.get(playerToken);
	}

	/**
	 * Removes a game from the list of all the games managed by the game manager
	 * updating also the game manager's maps
	 * 
	 * @param game
	 *            the game to be removed from the list of all the games managed
	 *            by the game manager
	 */
	public void removeGame(Game game) {
		Map<PlayerToken, Game> fromPlayerTokenToGameCopy = new HashMap<PlayerToken, Game>(
				fromPlayerTokenToGame);
		Map<Integer, Game> fromGameIdToGameCopy = new HashMap<Integer, Game>(
				fromGameIdToGame);
		// Removes game from the playerTokenToGameMap
		for (PlayerToken playerToken : fromPlayerTokenToGameCopy.keySet()) {
			if (fromPlayerTokenToGame.get(playerToken) == game) {
				fromPlayerTokenToGame.remove(playerToken);
			}
		}
		// Removes game from GameIdToGameMap
		for (int i : fromGameIdToGameCopy.keySet()) {
			if (fromGameIdToGame.get(i) == game) {
				fromGameIdToGame.remove(i);
			}
		}
	}

	/**
	 * Starts or ends a game based on the number of its players
	 * 
	 * @param game
	 *            the game to be start or stop accordingly to the number of its
	 *            players
	 */
	public void update(Game game) {
		if (game.getSubscriberHandlers().size() <= 1) {
			ArrayList<Object> parameters = new ArrayList<Object>();
			parameters.add("The game is end!");
			game.notifyListeners(new RemoteMethodCall("publishChatMsg",
					parameters));
			this.removeGame(game);
			game.notifyListeners(new RemoteMethodCall("endGame"));
		}
		// Otherwise starts the game
		else {
			if (game.getPublicData().getStatus() == GameStatus.OPEN)
				game.startGame();
		}
	}
}
