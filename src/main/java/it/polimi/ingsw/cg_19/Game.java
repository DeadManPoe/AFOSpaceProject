package it.polimi.ingsw.cg_19;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Set;
import java.util.Timer;

import server.GameManager;
import server.GameStatus;
import server.SubscriberHandler;
import common.Action;
import common.ClientNotification;
import common.EndTurnAction;
import common.GamePublicData;
import common.PSClientNotification;
import common.PlayerToken;
import common.RRClientNotification;
import common.RemoteMethodCall;
import decks.*;
import effects.ActionEffect;
import effects.GameActionMapper;
import factories.*;

/**
 * Represents a generic game(non-immutable class)
 * 
 * @author Andrea Sessa
 * @author Giorgio Pea
 * @version 1.0
 */
public class Game extends Observable {
	private final static long TURN_TIMEOUT = 10 * 60 * 1000;

	private volatile List<Player> players;

	private volatile ObjectDeck objectDeck;
	private volatile RescueDeck rescueDeck;
	private volatile SectorDeck sectorDeck;
	private volatile GameMap gameMap;
	private volatile Player currentPlayer;
	private volatile int turnNumber;

	private volatile List<Class<? extends Action>> nextActions;
	private volatile Action lastAction;

	private volatile Turn turn;

	// Communication related stuff
	private static int counter = 0;
	private volatile List<SubscriberHandler> subscriberList;

	private TurnTimeout timeout;
	private Timer timer;

	private volatile GameMapFactory gameMapFactory;
	private volatile Map<PlayerToken, Player> playerTokenToPlayerMap;
	private volatile GamePublicData gamePublicData;

	private volatile GameActionMapper actionMapper;

	private volatile GameManager gameManager;

	/**
	 * Constructs a game from the name of its associated map. The resources of
	 * the game are not initialized until the game is started. From the name of
	 * the game's associated map the right map factory is automatically created
	 * along with an empty list of players, an empty list of threads
	 * representing the game's subscribers in the logic of the pub/sub pattern,
	 * an empty container of the game's public data and an empty map between a
	 * player's unique identifier and a player
	 * 
	 * @param gameMapName
	 *            name of the game's associated map
	 */
	public Game(String gameMapName) {
		if (gameMapName.equals("GALILEI")) {
			this.gameMapFactory = new GalileiGameMapFactory();
		} else if (gameMapName.equals("FERMI")) {
			this.gameMapFactory = new FermiGameMapFactory();
		} else if (gameMapName.equals("GALVANI")) {
			this.gameMapFactory = new GalvaniGameMapFactory();
		} else {
			throw new IllegalArgumentException("The map's type is undefined");
		}
		this.subscriberList = new ArrayList<SubscriberHandler>();
		this.players = new ArrayList<Player>();
		this.playerTokenToPlayerMap = new HashMap<PlayerToken, Player>();
		counter++;
		this.gamePublicData = new GamePublicData(counter, "Game_" + counter);
		this.turnNumber = 0;
		players = new ArrayList<Player>();
	}

	/**
	 * Constructs a game from its associated map. The resources of the game are
	 * not initialized until the game is started. an empty list of players, an
	 * empty list of threads representing the game's subscribers in the logic of
	 * the pub/sub pattern, an empty container of the game's public data and an
	 * empty map between a player's unique identifier and a player are
	 * automatically created. This constructor is only used for test purposes
	 * 
	 * @param gameMap
	 *            the game's associated map
	 */
	public Game(GameMap gameMap) {
		this.gameMap = gameMap;
		this.subscriberList = new ArrayList<SubscriberHandler>();
		this.players = new ArrayList<Player>();
		this.playerTokenToPlayerMap = new HashMap<PlayerToken, Player>();
		counter++;
		this.gamePublicData = new GamePublicData(counter, "Game_" + counter);
		this.turnNumber = 0;
		DeckFactory deckFactory = new ObjectDeckFactory();
		this.objectDeck = (ObjectDeck) deckFactory.makeDeck();
		deckFactory = new SectorDeckFactory();
		this.sectorDeck = (SectorDeck) deckFactory.makeDeck();
		deckFactory = new RescueDeckFactory();
		this.rescueDeck = (RescueDeck) deckFactory.makeDeck();
	}

	/**
	 * Starts the game by initializing its resources and sending a remote method
	 * call to all the game's subscribers to signal them that the game is going
	 * to start
	 */
	public void startGame() {
		// Resources init
		DeckFactory deckFactory = new ObjectDeckFactory();
		this.objectDeck = (ObjectDeck) deckFactory.makeDeck();
		deckFactory = new SectorDeckFactory();
		this.sectorDeck = (SectorDeck) deckFactory.makeDeck();
		deckFactory = new RescueDeckFactory();
		this.rescueDeck = (RescueDeck) deckFactory.makeDeck();
		this.gameMap = gameMapFactory.makeMap();
		this.turnNumber = 0;
		//this.actionMapper = new GameActionMapper();
		ArrayList<Object> parameters = new ArrayList<Object>();
		parameters.add(gameMap.getName());
		parameters.add(this.fromPlayerToToken(currentPlayer));
		// Setting players' starting sector
		for (Player player : players) {
			if (player.getPlayerType().equals(PlayerType.HUMAN)) {
				player.setSector(gameMap.getHumanSector());
				//gameMap.getHumanSector().addPlayer(player);
			} else {
				player.setSector(gameMap.getAlienSector());
				//gameMap.getAlienSector().addPlayer(player);
			}
		}
		// Init of the first game turn
		if (currentPlayer.getPlayerType() == PlayerType.HUMAN) {
			turn = new HumanTurn(this);
		} else {
			turn = new AlienTurn(this);
		}
		nextActions = turn.getInitialActions();
		this.gamePublicData.setStatus(GameStatus.CLOSED);

		timer = new Timer();
		timeout = new TurnTimeout(this, timer);
		timer.schedule(timeout, TURN_TIMEOUT);
		// Notification to the subscribers
		this.notifyListeners(new RemoteMethodCall("sendMap", parameters));
	}

	/**
	 * Adds a player to the game
	 *
	 * @param playerName
	 *            the thread that will handle the pub/sub communication with the
	 *            client
	 * @return the unique identifier(token) of the player inserted
	 */
	public synchronized PlayerToken addPlayer(String playerName) {
		PlayerType playerType = assignTypeToPlayer(players.size() + 1);
		PlayerToken playerToken = new PlayerToken(playerType);
		Player player = new Player(playerType, playerName);
		playerTokenToPlayerMap.put(playerToken, player);
		players.add(player);
		gamePublicData.addPlayer();
		if (currentPlayer == null)
			this.currentPlayer = player;
		return playerToken;
	}

	/**
	 * Add a player to the game. This method is used only for test purposes
	 * 
	 * @param player
	 *            the player to be added to the game
	 */
	public void addPlayer(Player player) {
		this.players.add(player);
		playerTokenToPlayerMap.put(new PlayerToken(player.getPlayerType()),
				player);
		if (currentPlayer == null)
			this.currentPlayer = player;
	}

	/**
	 * Produces a player type based on the number of players already in game .
	 * If the number of players already in game is even, the returned player
	 * type is "HUMAN", otherwise is "ALIEN". This procedure is adopted in order
	 * to guarantee a balanced number of aliens and humans
	 *
	 * @param numberOfPlayers
	 *            the number of players already in game
	 * @return a player type, either "HUMAN" or "ALIEN"
	 */
	public PlayerType assignTypeToPlayer(int numberOfPlayers) {
		if (numberOfPlayers % 2 == 0) {
			return PlayerType.HUMAN;
		} else {
			return PlayerType.ALIEN;
		}

	}

	/**
	 * Gets the public data of the game
	 * 
	 * @return the public data of the game
	 */
	public synchronized GamePublicData getPublicData() {
		return gamePublicData;
	}

	/**
	 * Gets the list of threads representing the game's subscribers in the logic
	 * of the pub/sub pattern
	 * 
	 * @return the list of threads representing the game's subscribers in the
	 *         logic of the pub/sub pattern
	 */
	public List<SubscriberHandler> getSubscriberHandlers() {
		return this.subscriberList;
	}

	/**
	 * Sets the next current player in the game
	 *
	 */
	public void shiftCurrentplayer() {
		int currentPlayerIndex = players.indexOf(currentPlayer);
		do {
			currentPlayerIndex++;
			if (currentPlayerIndex == players.size())
				currentPlayerIndex = 0;
		} while (players.get(currentPlayerIndex).getPlayerState() == PlayerState.DEAD);

		this.currentPlayer = players.get(currentPlayerIndex);
	}

	/**
	 * Gets the game's current player
	 * 
	 * @return the game's current player
	 */
	public Player getCurrentPlayer() {
		return this.currentPlayer;
	}

	/**
	 * Gets the game's associated deck of object cards
	 * 
	 * @return the game's associated deck of object cards
	 */
	public ObjectDeck getObjectDeck() {
		return objectDeck;
	}

	/**
	 * Gets the game's associated deck of rescue cards
	 * 
	 * @return the game's associated deck of rescue cards
	 */
	public RescueDeck getRescueDeck() {
		return rescueDeck;
	}

	/**
	 * Gets the game's associated deck of sector cards
	 * 
	 * @return the game's associated deck of sector cards
	 */
	public SectorDeck getSectorDeck() {
		return sectorDeck;
	}

	/**
	 * Gets the game's associated map
	 * 
	 * @return the game's associated map
	 */
	public GameMap getMap() {
		return this.gameMap;
	}

	/**
	 * Gets the game's turn number
	 * 
	 * @return the game's turn number
	 */
	public int getTurnNumber() {
		return turnNumber;
	}

	/**
	 * Sets the game's turn number. This method is used only for test purposes
	 * 
	 * @param turnNumber
	 *            the new game's turn number
	 */
	public void setTurnNumber(int turnNumber) {
		this.turnNumber = turnNumber;
	}

	/**
	 * Gets the game's last action performed
	 * 
	 * @return the game's last performed action
	 */
	public Action getLastAction() {
		return lastAction;
	}

	/**
	 * Sets the game's last action performed
	 * 
	 * @param lastAction
	 *            the new game's last action performed
	 */
	public void setLastAction(Action lastAction) {
		this.lastAction = lastAction;
	}

	/**
	 * Performs a game action
	 * 
	 * @see ClientNotification
	 * @see Action
	 * @param action
	 *            the action to be performed
	 *            the token of the player that wants to perform the action
	 * @return an array of two notifications to be sent to the game's
	 *         subscribers. The first one is to be sent only to the player that
	 *         wants to perform the action, the second one is to be sent to all
	 *         the game's subscribers
	 * @throws IllegalAccessException
	 *             if Action is null or not mapped to an effect
	 * @throws InstantiationException
	 *             if Action is null or not mapped to an effect
	 */

	public synchronized ClientNotification[] makeAction(Action action,
			PlayerToken playerToken, boolean forced)
			throws InstantiationException, IllegalAccessException {
		RRClientNotification clientNotification = new RRClientNotification();
		PSClientNotification psNotification = new PSClientNotification();
		Player actualPlayer = playerTokenToPlayerMap.get(playerToken);
		// if(turn.getInitialAction().contains(action.class)) &&
		if (!currentPlayer.equals(actualPlayer)) {
			clientNotification.setActionResult(false);
		} else {
			// If the player is ok then checks if the action is ok
			if (nextActions.contains(action.getClass()) || forced) {
				// Retrieve the related effect
				ActionEffect effect = actionMapper.getEffect(action);

				// Executes the effect and get the result
				//boolean actionResult = effect.executeEffect(this,
				//		clientNotification, psNotification);

				if (true) {
					/*
					 * If the last action has been and an end turn action the
					 * there is no need to update the nextAction field
					 */
					if (!lastAction.getClass().equals(EndTurnAction.class)) {
						nextActions = turn.getNextActions(lastAction);
					} else {
						
						nextActions = turn.getInitialActions();
						turnNumber++;

						// Reset the timeout

						timer.purge();
						timer.cancel();
						timeout.cancel();
						timer = new Timer();
						timeout = new TurnTimeout(this, timer);
						timer.schedule(timeout, TURN_TIMEOUT);
					}
					boolean winH = checkWinConditions(PlayerType.HUMAN);
					boolean winA = checkWinConditions(PlayerType.ALIEN);

					if (winH) {
						psNotification
								.setMessage(psNotification.getMessage()
										+ "\n[GLOBAL MESSAGE]: The game has ended, HUMANS WIN!");
					}
					if (winA) {
						psNotification
								.setMessage(psNotification.getMessage()
										+ "\n[GLOBAL MESSAGE]: The game has ended, ALIENS WIN!");

					}
					if (winH || winA) {
						psNotification.setAlienWins(winA);
						psNotification.setHumanWins(winH);
						clientNotification.setActionResult(true);
						ClientNotification[] toReturn = { clientNotification,
								psNotification };
						this.gameManager.removeGame(this);
						return toReturn;
					}
					clientNotification.setActionResult(true);
				}
			} else {
				clientNotification.setActionResult(false);
			}

		}
		ClientNotification[] toReturn = { clientNotification, psNotification };
		return toReturn;
	}

	/**
	 * Performs a game action. This method is used only for test purposes
	 * 
	 * @see Game#makeAction
	 */
	public ClientNotification[] makeAction(Action action, Player player)
			throws InstantiationException, IllegalAccessException {
		return this.makeAction(action, fromPlayerToToken(player), false);
	}

	/**
	 * Sets a new game's turn
	 * 
	 * @param turn
	 *            the new game's turn
	 */
	public void setTurn(Turn turn) {
		this.turn = turn;
	}

	/**
	 * Gets the game's current turn
	 * 
	 * @return the game's current turn
	 */
	public Turn getTurn() {
		return this.turn;
	}

	/**
	 * This method is called by the timeout thread when the current player has
	 * not concluded its turn in time
	 * 
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public void timeoutUpdate() throws InstantiationException,
			IllegalAccessException {

		this.deleteObserver(subscriberList.get(players.indexOf(this
				.getCurrentPlayer())));
		this.subscriberList.remove(players.indexOf(this.getCurrentPlayer()));

		Player exPlayer = this.getCurrentPlayer();
		EndTurnAction action = new EndTurnAction();
		ClientNotification[] notifications = this.makeAction(action,
				fromPlayerToToken(this.getCurrentPlayer()), true);
		ArrayList<Object> parameters = new ArrayList<Object>();
		parameters.add(notifications[1]);
		notifications[1].setMessage(notifications[1].getMessage()
				+ "\n[GLOBAL MESSAGE]: " + exPlayer.getName()
				+ " has disconnected!");
		this.notifyListeners(new RemoteMethodCall("sendPubNotification",
				parameters));

		exPlayer.setPlayerState(PlayerState.DEAD);
		this.players.remove(exPlayer);
	}

	/**
	 * Notifies the game's subscribers with a remote method call that has to be
	 * performed on them
	 * 
	 * @param remoteMethodCall
	 *            the remote method call to be performed on the game's
	 *            subscribers
	 */
	public synchronized void notifyListeners(RemoteMethodCall remoteMethodCall) {
		this.setChanged();
		this.notifyObservers(remoteMethodCall);
	}

	/**
	 * Gets the game's id
	 * 
	 * @return the game's id
	 */
	public int getId() {
		return this.gamePublicData.getId();
	}

	/**
	 * Adds a subscriber to the game
	 * 
	 * @param handler
	 *            the thread used by the game to communicate with the subscriber
	 */
	public synchronized void addSubscriber(SubscriberHandler handler) {
		this.addObserver(handler);
		subscriberList.add(handler);
	}

	/**
	 * Return the unique identifier(token) of a given player
	 * 
	 * @param player
	 *            the player whose unique identifier(token) is to be returned
	 */
	public PlayerToken fromPlayerToToken(Player player) {
		Set<PlayerToken> tokens = playerTokenToPlayerMap.keySet();
		for (PlayerToken t : tokens) {
			if (playerTokenToPlayerMap.get(t).equals(player))
				return t;
		}
		return null;

	}

	/**
	 * Checks the number of player of PlayerType type still alive
	 * 
	 * @param type
	 *            The type of the player
	 * @return The number of alive player
	 */
	public int getNumOfAlivePlayer(PlayerType type) {
		int count = 0;
		for (Player p : players) {
			if (p.getPlayerState() == PlayerState.ALIVE
					&& p.getPlayerType() == type)
				count++;
		}
		return count;
	}

	/**
	 * Checks if the game has finished
	 * 
	 * @return true if someone(Alien, Human or both) has won the game and the
	 *         game has ended, false otherwise
	 */
	public boolean checkWinConditions(PlayerType playerType) {
		if (playerType == PlayerType.HUMAN) {
			// If all human players are escaped then Human wins!
			if (checkStateAll(PlayerType.HUMAN, PlayerState.ESCAPED))
				return true;
			// Only one human player left with an escape possibility
			else if (this.getNumOfAlivePlayer(PlayerType.ALIEN) == 0
					&& this.getNumOfAlivePlayer(PlayerType.HUMAN) == 1
					&& this.gameMap.existEscapes())
				return true;
			// All human player are dead or escaped
			else if (!this.checkStateAll(PlayerType.HUMAN, PlayerState.DEAD)
					&& this.getNumOfAlivePlayer(PlayerType.HUMAN) == 0)
				return true;
		} else {
			// If all human player are all dead, alien wins!
			if (this.getNumOfAlivePlayer(PlayerType.HUMAN) == 0
					&& !checkStateAll(PlayerType.HUMAN, PlayerState.ESCAPED))
				return true;

			if (this.turnNumber == 39) {
				// Some human player is still alive, but turn = 39, so alien
				// wins
				if (this.getNumOfAlivePlayer(PlayerType.HUMAN) > 0)
					return true;
				return false;
			} else {
				if (!this.gameMap.existEscapes()
						&& this.getNumOfAlivePlayer(PlayerType.HUMAN) > 0)
					return true;
			}
		}
		return false;
	}

	/**
	 * Checks if all the players of a given player's type are in the given state
	 * 
	 * @param playerType
	 *            the player's type to be considered for the checking
	 *            the state of a player to be consider for the checking
	 * @return true if all the players of a given player type are in the given
	 *         state
	 */
	private boolean checkStateAll(PlayerType playerType, PlayerState state) {
		for (Player player : players) {
			if (player.getPlayerState() != state
					&& player.getPlayerType() == playerType)
				return false;
		}
		return true;
	}

	/**
	 * Return the player that has the given unique identifier(token)
	 * 
	 * @return the player that has the given unique identifier(token)
	 */
	public Player fromTokenToPlayer(PlayerToken playerToken) {
		return playerTokenToPlayerMap.get(playerToken);

	}

	/**
	 * Sets the game's associated game manager
	 * 
	 * @see GameManager
	 * @param gameManager
	 *            the game manager to be associated with the game
	 */
	public void setGameManager(GameManager gameManager) {
		this.gameManager = gameManager;
	}
}
