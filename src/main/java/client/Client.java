package client;

import it.polimi.ingsw.cg_19.GameMap;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;

import common.Action;
import common.AttackObjectCard;
import common.Coordinate;
import common.DefenseObjectCard;
import common.DiscardAction;
import common.EndTurnAction;
import common.GamePublicData;
import common.GlobalNoiseSectorCard;
import common.LightsObjectCard;
import common.MoveAction;
import common.MoveAttackAction;
import common.ObjectCard;
import common.PSClientNotification;
import common.PlayerToken;
import common.PrivateDeck;
import common.RRClientNotification;
import common.Sector;
import common.SectorCard;
import common.TeleportObjectCard;
import common.UseObjAction;
import common.UseSectorCardAction;

/**
 * Represents a client in the client server communication layer of the
 * application
 *
 * @author Andrea Sessa
 * @author Giorgio Pea
 */
public class Client extends Observable {
	// The client's connection details
	private ClientConnection connection;
	// The client's unique identifier
	private PlayerToken token;
	// A group of services the client offers to the server to exchange data
	private ClientRemoteServices clientServices;
	// A factory that produces communication sessions
	private RemoteDataExchangeFactory dataExcFactory;
	// The map of the game the client is playing
	private GameMap gameMap;
	// The client's(player's) private deck of object cards
	private PrivateDeck privateDeck;
	// The game map's sector in which the client(player) is located
	private Sector currentSector;
	// The current client's notification
	private RRClientNotification currentNotification;
	// The client's communication session
	private ComSession comSession;
	// A flag that indicates that the game is started
	private boolean isGameStarted;
	// A flag that indicates that the client holds the turn
	private boolean isMyTurn;
	// A flag that indicates that the client has to resolve a lights object
	// effect
	private boolean askLight;
	// A flag that indicates that the game is end
	private boolean isGameEnd;
	// A flag that indicates if the player has moved
	private boolean hasMoved;
	// A flag that indicates if the player has asked to attack
	private volatile boolean askAttack;
	private volatile boolean isDead;
	/*
	 * A thread that handles async server notification in the logic of the
	 * pub/sub pattern
	 */
	private PubSubHandler pubSubHandler;
	// The games the client could join
	private ArrayList<GamePublicData> availableGames;
	// A flag that if the player hasEscaped
	private volatile boolean hasEscaped;
	// File handler for the logger
	private Handler fileHandler;

	/**
	 * Constructs a client from its connection details. The services the client
	 * offers to the server to exchange data are automatically created, along
	 * with the client's visualization layer, and associated to the client.
	 *
	 * @see ClientConnection
	 * @param connection
	 *            the connection details the client has to know to communicate
	 *            with the server
	 * @throws IOException
	 * @throws SecurityException
	 */
	public Client(ClientConnection connection) {
		try {
			this.connection = connection;
			this.clientServices = new ClientRemoteServices(this);
			this.comSession = new ComSession(this);
			this.isGameStarted = false;
			this.fileHandler = new FileHandler("clientLog.log");
			ClientLogger.getLogger().addHandler(fileHandler);
			this.fileHandler.setLevel(Level.ALL);
			ClientLogger.getLogger().setLevel(Level.ALL);
		} catch (RemoteException e) {
			ClientLogger.getLogger().log(Level.SEVERE,
					"error in rmi interface | Client ", e);
		} catch (SecurityException e) {
			ClientLogger.getLogger().log(Level.SEVERE,
					"error in rmi interface | Client ", e);
		} catch (IOException e) {
			ClientLogger.getLogger().log(Level.SEVERE,
					"error in file logger | Client ", e);
		}

	}

	/**
	 * Gets the boolean that indicates if the game the client has joined has
	 * started or not
	 * 
	 * @return the boolean that indicates if the game the client has joined has
	 *         started or not
	 */
	public synchronized boolean isGameStarted() {
		return isGameStarted;
	}

	/**
	 * Sets the boolean that indicates if the game the client has joined has
	 * started or not
	 * 
	 * @param gameStarted
	 *            the new boolean that indicates if the game the client has
	 *            joined has started or not
	 */
	public synchronized void setGameStarted(boolean gameStarted) {
		this.isGameStarted = gameStarted;
	}

	public synchronized boolean isGameEnded() {
		return this.isGameEnd;
	}

	public synchronized void setGameEnded(boolean gameEnded) {
		this.isGameEnd = gameEnded;
	}

	/**
	 * Gets the client's unique identifier
	 *
	 * @see PlayerToken
	 * @return the identifier of the client/player
	 */
	public PlayerToken getToken() {
		return token;
	}

	/**
	 * Sets the client's unique identifier
	 *
	 * @param token
	 *            the identifier to be set for the client/player
	 */
	public void setToken(PlayerToken token) {
		this.token = token;
	}

	/**
	 * Gets the client's connection details
	 *
	 * @see ClientConnection
	 * @return the connection details of the client
	 */
	public ClientConnection getConnection() {
		return connection;
	}

	/**
	 * Gets the services the client offers to the server to exchange data
	 *
	 * @see ClientRemoteServices
	 * @return the services the client offers to the server to exchange data
	 */
	public ClientRemoteServices getClientServices() {
		return clientServices;
	}

	/**
	 * Gets the client's associated communication sessions factory
	 *
	 * @see RemoteDataExchangeFactory
	 * @return the client's associated communication sessions factory
	 */
	public RemoteDataExchangeFactory getDataExcFactory() {
		return dataExcFactory;
	}

	/**
	 * Gets the map of the game the client is playing
	 *
	 * @return the map of the game the client is playing
	 */
	public GameMap getGameMap() {
		return this.gameMap;
	}

	/**
	 * Sets the map of the game the client is playing
	 *
	 * @param map
	 *            the new map of the game the client is playinh
	 */
	public void setGameMap(GameMap map) {
		this.gameMap = map;
	}

	/**
	 * Sets the client's private deck of object cards
	 *
	 * @param privateDeck
	 *            the new client's private deck of object cards
	 */
	public void setPrivateDeck(PrivateDeck privateDeck) {
		this.privateDeck = privateDeck;

	}

	/**
	 * Sets the client's current notification to be handled
	 *
	 * @param notification
	 *            the new notification to be handled by the client
	 */
	public void setNotification(RRClientNotification notification) {
		this.currentNotification = notification;

	}

	/**
	 * Gets the game map's sector in which the client(player) is located
	 *
	 * @return the game map's sector in which the client(player) is located
	 */
	public Sector getCurrentSector() {
		return this.currentSector;
	}

	/**
	 * Gets the boolean that indicates if the current turn is the client's turn
	 * 
	 * @return the boolean that indicates if the current turn is the client's
	 *         turn
	 */
	public boolean getIsMyTurn() {
		return isMyTurn;
	}

	/**
	 * Sets the boolean that indicates if the current turn is the client's turn
	 * and notify the gui about the new turn state
	 * 
	 * @param isMyTurn
	 *            the new boolean that indicates if the current turn is the
	 *            client's turn
	 */
	public void setIsMyTurn(boolean isMyTurn) {
		this.isMyTurn = isMyTurn;
	}

	/**
	 * Sets the game map's sector in which the client(player) is located
	 *
	 * @param sector
	 *            the new game map's sector in which the client will be located
	 */
	public void setCurrentSector(Sector sector) {
		this.currentSector = sector;
	}

	/**
	 * Constructs the client's associated remote date exchange factory either as
	 * a factory that produces socket based remote data exchanges, or a factory
	 * that produces rmi remote data exchanges
	 *
	 * @see RemoteDataExchangeFactory
	 * @see RmiRemoteDataExchangeFactory
	 * @see SocketRemoteDataExchangeFactory
	 * @param typeOfFactory
	 *            the type of factory that has to be created
	 */
	public void buildDataRemoteExchangeFactory(String typeOfFactory) {
		if (typeOfFactory.equals("RMI")) {
			this.dataExcFactory = new RmiRemoteDataExchangeFactory(this);
		} else if (typeOfFactory.equals("SOCKET")) {
			this.dataExcFactory = new SocketRemoteDataExchangeFactory(this);
		} else {
			throw new IllegalArgumentException(
					"No communication sessions factory for the type inserted");
		}
	}

	/**
	 * Constructs and starts a thread that handles async notifications from the
	 * server in the context of the pub/sub pattern
	 *
	 * @see PubSubHandler
	 *            the input stream from which the async notifications come
	 * @param clientServices
	 *            the services the client offers to the server to exchange data,
	 *            in this case just notification related services will be used
	 * @throws IOException
	 */
	public void startPubSub(ObjectInputStream stream, ClientRemoteServices clientServices)
			throws IOException {
		this.pubSubHandler = new PubSubHandler(stream, clientServices);
		pubSubHandler.start();
	}

	/**
	 * Gets the notification the client has received by the server in response
	 * to a request
	 * 
	 * @return the notification the client has received by the server in
	 *         response to
	 */
	public RRClientNotification getCurrentNotification() {
		return currentNotification;
	}

	/**
	 * Gets the client's private deck
	 * 
	 * @return the client's private deck
	 */
	public PrivateDeck getPrivateDeck() {
		return this.privateDeck;
	}

	/**
	 * Gets the games the client could join
	 * 
	 * @return the games the client could join
	 */
	public ArrayList<GamePublicData> getAvailableGames() {
		return availableGames;
	}

	/**
	 * Sets the games the client could join
	 * 
	 * @param avGames
	 *            the new games the client could join
	 */
	public void setAvailableGames(ArrayList<GamePublicData> avGames) {
		this.availableGames = avGames;
	}

	/**
	 * Processes a move action request by the client. This processing consists
	 * in a remote method call to the server
	 * 
	 * @param horCoord
	 *            the horizontal coordinate of the sector in which to move
	 * @param vertCoord
	 *            the vertical coordinate of the sector in which to move
	 * @throws IllegalAccessException
	 *             signals a com. error
	 * @throws IllegalArgumentException
	 *             signals a com. error
	 * @throws InvocationTargetException
	 *             signals a com. error
	 * @throws NoSuchMethodException
	 *             signals a com. error
	 * @throws SecurityException
	 *             signals a com. error
	 * @throws ClassNotFoundException
	 *             signals a com. error
	 * @throws IOException
	 *             signals a com. error
	 * @throws NotBoundException
	 *             signals a com. error
	 */
	public void move(char horCoord, int vertCoord)
			throws IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException,
			SecurityException, ClassNotFoundException, IOException,
			NotBoundException {
		Coordinate coordinate = new Coordinate(horCoord, vertCoord);
		Sector targetSector = this.getGameMap().getSectorByCoords(coordinate);
		if (targetSector != null) {
			ArrayList<Object> parameters = new ArrayList<Object>();
			Action action = new MoveAction(targetSector);
			parameters.add(action);
			parameters.add(this.getToken());
			this.comSession.start("makeAction", parameters);
			if (this.currentNotification.getActionResult()) {
				this.currentSector = new Sector(targetSector.getCoordinate(),
						targetSector.getSectorType());
				this.hasMoved = true;
			}
		} else {
			throw new IllegalArgumentException(
					"Undefined sector, please try again");
		}
	}

	/**
	 * Processes a use object card request by the client. This processing
	 * consists in a remote method call to the server
	 * 
	 * @param objectCardIndex
	 *            the index of the object card in the client's private deck to
	 *            be used
	 * @throws IllegalAccessException
	 *             signals a com. error
	 * @throws IllegalArgumentException
	 *             signals a com. error
	 * @throws InvocationTargetException
	 *             signals a com. error
	 * @throws NoSuchMethodException
	 *             signals a com. error
	 * @throws SecurityException
	 *             signals a com. error
	 * @throws ClassNotFoundException
	 *             signals a com. error
	 * @throws IOException
	 *             signals a com. error
	 * @throws NotBoundException
	 *             signals a com. error
	 */
	public void useObjCard(int objectCardIndex) throws IllegalAccessException,
			IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException, ClassNotFoundException,
			IOException, NotBoundException {
		int cardsAmount = this.getPrivateDeck().getSize();
		if (objectCardIndex <= cardsAmount && objectCardIndex > 0) {
			ArrayList<Object> parameters = new ArrayList<Object>();
			ObjectCard objectCard = this.getPrivateDeck().getCard(
					objectCardIndex - 1);
			if (objectCard instanceof LightsObjectCard) {
				this.askLight = true;
			} else if (objectCard instanceof AttackObjectCard) {
				this.askAttack = true;
			} else {
				Action action = new UseObjAction(objectCard);
				parameters.add(action);
				parameters.add(this.token);
				this.comSession.start("makeAction", parameters);
				if (this.getCurrentNotification().getActionResult()) {
					this.getPrivateDeck().removeCard(objectCard);
					if (objectCard instanceof TeleportObjectCard) {
						this.setCurrentSector(this.gameMap.getHumanSector());
					}
				}
			}
		} else {
			throw new IllegalArgumentException(
					"Undifined card, please try again");
		}
	}

	/**
	 * Processes a request of ending the turn by the client. This processing
	 * consists in a remote method call to the server
	 * 
	 * @throws IllegalAccessException
	 *             signals a com. error
	 * @throws IllegalArgumentException
	 *             signals a com. error
	 * @throws InvocationTargetException
	 *             signals a com. error
	 * @throws NoSuchMethodException
	 *             signals a com. error
	 * @throws SecurityException
	 *             signals a com. error
	 * @throws ClassNotFoundException
	 *             signals a com. error
	 * @throws IOException
	 *             signals a com. error
	 * @throws NotBoundException
	 *             signals a com. error
	 */
	public void endTurn() throws IllegalAccessException,
			IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException, ClassNotFoundException,
			IOException, NotBoundException {
		ArrayList<Object> parameters = new ArrayList<Object>();
		Action action = new EndTurnAction();
		parameters.add(action);
		parameters.add(this.token);
		this.comSession.start("makeAction", parameters);
		if (this.getCurrentNotification().getActionResult()) {
			this.setIsMyTurn(false);
			this.hasMoved = false;
		}
	}

	/**
	 * Processes a request of joining a new game by the client. This processing
	 * consists in a remote method call to the server
	 * 
	 * @param gameMapName
	 *            the name of the map for the game
	 * @param playerName
	 *            the name of the player for the game
	 * @throws RemoteException
	 *             signals a com. error
	 * @throws IllegalAccessException
	 *             signals a com. error
	 * @throws IllegalArgumentException
	 *             signals a com. error
	 * @throws InvocationTargetException
	 *             signals a com. error
	 * @throws NoSuchMethodException
	 *             signals a com. error
	 * @throws SecurityException
	 *             signals a com. error
	 * @throws ClassNotFoundException
	 *             signals a com. error
	 * @throws IOException
	 *             signals a com. error
	 * @throws NotBoundException
	 *             signals a com. error
	 */
	public void joinNewGame(String gameMapName, String playerName)
			throws RemoteException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException, ClassNotFoundException,
			IOException, NotBoundException {
		ArrayList<Object> parameters = new ArrayList<Object>();
		parameters.add(gameMapName);
		parameters.add(playerName);
		this.comSession.start("joinNewGame", parameters);
	}

	/**
	 * Processes a request of joining an existing game by the client. This
	 * processing consists in a remote method call to the server
	 * 
	 * @param gameId
	 *            the id of the game to join
	 * @param playerName
	 *            the name of the player for the game
	 * @throws IllegalAccessException
	 *             signals a com. error
	 * @throws IllegalArgumentException
	 *             signals a com. error
	 * @throws InvocationTargetException
	 *             signals a com. error
	 * @throws NoSuchMethodException
	 *             signals a com. error
	 * @throws SecurityException
	 *             signals a com. error
	 * @throws ClassNotFoundException
	 *             signals a com. error
	 * @throws IOException
	 *             signals a com. error
	 * @throws NotBoundException
	 *             signals a com. error
	 */
	public void joinGame(int gameId, String playerName)
			throws IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException,
			SecurityException, ClassNotFoundException, IOException,
			NotBoundException {
		ArrayList<Object> parameters = new ArrayList<Object>();
		parameters.add(gameId);
		parameters.add(playerName);
		this.comSession.start("joinGame", parameters);

	}

	/**
	 * Processes the request of getting all the available games by the client.
	 * This processing consists in a remote method call to the server
	 * 
	 * @return the list of all the available games
	 * @throws RemoteException
	 *             signals a com. error
	 * @throws IllegalAccessException
	 *             signals a com. error
	 * @throws InvocationTargetException
	 *             signals a com. error
	 * @throws NoSuchMethodException
	 *             signals a com. error
	 * @throws SecurityException
	 *             signals a com. error
	 * @throws ClassNotFoundException
	 *             signals a com. error
	 * @throws IOException
	 *             signals a com. error
	 * @throws NotBoundException
	 *             signals a com. error
	 */
	public List<GamePublicData> getGames() throws RemoteException,
			IllegalAccessException, InvocationTargetException,
			NoSuchMethodException, SecurityException, ClassNotFoundException,
			IOException, NotBoundException, java.net.ConnectException, java.rmi.ConnectException {
		this.comSession.start("getGames");
		return this.availableGames;

	}

	/**
	 * Processes a global noise sector card effect resolution request by the
	 * client. This processing consists in a remote method call to the server
	 * 
	 * @param horCoord
	 *            the horizontal coordinate of the sector of noise
	 * @param vertCoord
	 *            the vertical coordinate of the sector of noise
	 * @param hasObject
	 *            a boolean value that indicates if the card the effect is
	 *            associated has an associated object card
	 * @throws IllegalAccessException
	 *             signals a com. error
	 * @throws IllegalArgumentException
	 *             signals a com. error
	 * @throws InvocationTargetException
	 *             signals a com. error
	 * @throws NoSuchMethodException
	 *             signals a com. error
	 * @throws SecurityException
	 *             signals a com. error
	 * @throws ClassNotFoundException
	 *             signals a com. error
	 * @throws IOException
	 *             signals a com. error
	 * @throws NotBoundException
	 *             signals a com. error
	 */
	public void globalNoise(char horCoord, int vertCoord, boolean hasObject)
			throws IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException,
			SecurityException, ClassNotFoundException, IOException,
			NotBoundException {
		Coordinate coordinate = new Coordinate(horCoord, vertCoord);
		Sector targetSector = this.getGameMap().getSectorByCoords(coordinate);
		if (targetSector != null) {
			SectorCard globalNoiseCard = new GlobalNoiseSectorCard(hasObject,
					targetSector);
			ArrayList<Object> parameters = new ArrayList<Object>();
			Action action = new UseSectorCardAction(globalNoiseCard);
			parameters.add(action);
			parameters.add(this.getToken());
			this.comSession.start("makeAction", parameters);
		} else {
			throw new IllegalArgumentException(
					"Undefined sector, please try again");
		}
	}

	/**
	 * Processing a discarding an object card request by the client. This
	 * processing consists in a remote method call to the server
	 * 
	 * @param objectCardIndex
	 *            the index of the object card to discard from the client's
	 *            private deck
	 * @throws IllegalAccessException
	 *             signals a com. error
	 * @throws IllegalArgumentException
	 *             signals a com. error
	 * @throws InvocationTargetException
	 *             signals a com. error
	 * @throws NoSuchMethodException
	 *             signals a com. error
	 * @throws SecurityException
	 *             signals a com. error
	 * @throws ClassNotFoundException
	 *             signals a com. error
	 * @throws IOException
	 *             signals a com. error
	 * @throws NotBoundException
	 *             signals a com. error
	 */
	public void discardCard(int objectCardIndex) throws IllegalAccessException,
			IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException, ClassNotFoundException,
			IOException, NotBoundException {
		int cardsAmount = this.getPrivateDeck().getSize();
		if (objectCardIndex <= cardsAmount && objectCardIndex > 0) {
			ObjectCard objectCardToDiscard = this.getPrivateDeck().getCard(
					objectCardIndex - 1);
			ArrayList<Object> parameters = new ArrayList<Object>();
			Action action = new DiscardAction(objectCardToDiscard);
			parameters.add(action);
			parameters.add(this.token);
			this.comSession.start("makeAction", parameters);

			if (this.getCurrentNotification().getActionResult()) {
				this.getPrivateDeck().removeCard(objectCardToDiscard);
			}

		} else {
			throw new IllegalArgumentException(
					"Undifined card, please try again");
		}

	}

	/**
	 * Completes the effect of a lights object card. This completion consists in
	 * a remote method call to the server
	 * 
	 * @param horCoord
	 *            the horizontal coordinate of the sector indicated for the
	 *            lights object card's effect
	 * @param vertCoord
	 *            the vertical coordinate of the sector indicated for the lights
	 *            object card's effect
	 * @throws IllegalAccessException
	 *             signals a com. error
	 * @throws IllegalArgumentException
	 *             signals a com. error
	 * @throws InvocationTargetException
	 *             signals a com. error
	 * @throws NoSuchMethodException
	 *             signals a com. error
	 * @throws SecurityException
	 *             signals a com. error
	 * @throws ClassNotFoundException
	 *             signals a com. error
	 * @throws IOException
	 *             signals a com. error
	 * @throws NotBoundException
	 *             signals a com. error
	 */
	public void lights(char horCoord, int vertCoord)
			throws IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException,
			SecurityException, ClassNotFoundException, IOException,
			NotBoundException {
		Coordinate coordinate = new Coordinate(horCoord, vertCoord);
		Sector targetSector = this.getGameMap().getSectorByCoords(coordinate);
		if (targetSector != null) {
			ObjectCard lightsCard = new LightsObjectCard(targetSector);
			ArrayList<Object> parameters = new ArrayList<Object>();
			Action action = new UseObjAction(lightsCard);
			parameters.add(action);
			parameters.add(this.getToken());
			this.comSession.start("makeAction", parameters);
			this.askLight = false;
			this.getPrivateDeck().removeCard(lightsCard);
		} else {
			throw new IllegalArgumentException(
					"Undefined sector, please try again");
		}

	}

	/**
	 * Gets the flag that indicates if the player has to complete a lights
	 * object card effect
	 * 
	 * @return the flag that indicates if the player has to complete a lights
	 *         object card effect
	 */
	public boolean isAskLight() {
		return askLight;
	}

	/**
	 * Shutdowns the application
	 */
	public void shutdown() {
		System.err.println("THE APPLICATION WILL CLOSE");
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			ClientLogger.getLogger().log(Level.SEVERE,
					"thread error | shutdown", e);
		}
		System.exit(-1);
	}

	/**
	 * Processes the request of sending a chat message by the client. This
	 * processing consists in a remote method call to the server
	 * 
	 * @param message
	 *            the chat message to be sent to all the players of the game
	 * @throws IllegalAccessException
	 *             signals a com. error
	 * @throws IllegalArgumentException
	 *             signals a com. error
	 * @throws InvocationTargetException
	 *             signals a com. error
	 * @throws NoSuchMethodException
	 *             signals a com. error
	 * @throws SecurityException
	 *             signals a com. error
	 * @throws ClassNotFoundException
	 *             signals a com. error
	 * @throws IOException
	 *             signals a com. error
	 * @throws NotBoundException
	 *             signals a com. error
	 */
	public void sendMessage(String message) throws IllegalAccessException,
			IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException, ClassNotFoundException,
			IOException, NotBoundException {
		ArrayList<Object> parameters = new ArrayList<Object>();
		parameters.add(message);
		parameters.add(this.token);

		this.comSession.start("publishGlobalMessage", parameters);
	}

	/**
	 * Processes the request of attacking a sector by the client. This
	 * processing consists in a remote method call to the server
	 * 
	 * @param horCoord
	 *            the horizontal coordinate of the sector to attack
	 * @param vertCoord
	 *            the vertical coordinate of the sector to attack
	 * @param humanAttack
	 *            the boolean value that indicates if the attack is coming from
	 *            a human player or not
	 * @throws IllegalAccessException
	 *             signals a com. error
	 * @throws IllegalArgumentException
	 *             signals a com. error
	 * @throws InvocationTargetException
	 *             signals a com. error
	 * @throws NoSuchMethodException
	 *             signals a com. error
	 * @throws SecurityException
	 *             signals a com. error
	 * @throws ClassNotFoundException
	 *             signals a com. error
	 * @throws IOException
	 *             signals a com. error
	 * @throws NotBoundException
	 *             signals a com. error
	 */
	public void attack(char horCoord, int vertCoord, boolean humanAttack)
			throws IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException,
			SecurityException, ClassNotFoundException, IOException,
			NotBoundException {
		Coordinate coordinate = new Coordinate(horCoord, vertCoord);
		Sector targetSector = this.getGameMap().getSectorByCoords(coordinate);
		if (targetSector != null) {
			ArrayList<Object> parameters = new ArrayList<Object>();
			AttackObjectCard card = null;
			if (humanAttack) {
				card = new AttackObjectCard(targetSector);
				Action action = new UseObjAction(card);
				parameters.add(action);
				parameters.add(this.getToken());
				this.comSession.start("makeAction", parameters);
			} else {
				Action action = new MoveAttackAction(targetSector);
				parameters.add(action);
				parameters.add(this.getToken());
				this.comSession.start("makeAction", parameters);
			}
			if (this.getCurrentNotification().getActionResult()) {
				this.currentSector = targetSector;
				this.privateDeck.removeCard(card);
				this.hasMoved = true;
			}
			this.askAttack = false;
		} else {
			throw new IllegalArgumentException(
					"Undefined sector, please try again");
		}

	}

	/**
	 * Notify the GUI/CLI about incoming pub/sub delivered notifications
	 * 
	 * @see PSClientNotification
	 * @param notification
	 *            the notification to display
	 */
	public void psNotify(PSClientNotification notification) {
		System.out.println(notification.getMessage());
		this.setChanged();
		this.notifyObservers(notification);
	}

	/**
	 * Display a message on the GUI/CLI interface
	 * 
	 * @param msg
	 *            the message to display
	 */
	public void displayMessage(String msg) {
		System.out.println(msg);
		this.setChanged();
		this.notifyObservers(msg);
	}

	/**
	 * Gets the flag that indicates if the client has moved or not
	 * 
	 * @return the flag that indicates if the client has moved or not
	 */
	public boolean getHasMoved() {
		return hasMoved;
	}

	/**
	 * Gets the flag that indicates if the client has moved or not
	 * 
	 * @param hasMoved
	 *            the new boolean value that indicates if the client has moved
	 *            or not
	 */
	public void setHasMoved(boolean hasMoved) {
		this.hasMoved = hasMoved;
	}

	/**
	 * Gets the flag that indicates if the client has to complete an attack
	 * 
	 * @return the flag that indicates if the client has to complete an attack
	 */
	public boolean isAskAttack() {
		return askAttack;
	}

	/**
	 * Allows the user to force the start of a game without waiting the timeout
	 * 
	 * @throws NotBoundException
	 *             signals a com. error
	 * @throws IOException
	 *             signals a com. error
	 * @throws ClassNotFoundException
	 *             signals a com. error
	 * @throws SecurityException
	 *             signals a com. error
	 * @throws NoSuchMethodException
	 *             signals a com. error
	 * @throws InvocationTargetException
	 *             signals a com. error
	 * @throws IllegalAccessException
	 *             signals a com. error
	 */
	public void forceGameStart() throws IllegalAccessException,
			InvocationTargetException, NoSuchMethodException,
			ClassNotFoundException, IOException, NotBoundException {
		ArrayList<Object> parameter = new ArrayList<Object>();
		parameter.add(this.getToken());
		this.comSession.start("forceGameStart", parameter);
	}

	/**
	 * Gets if the client owns a defense card and removes it from the client's
	 * private deck
	 */
	public void removeDefenseCard() {
		for (ObjectCard objectCard : this.privateDeck.getContent()) {
			if (objectCard instanceof DefenseObjectCard) {
				this.privateDeck.removeCard(objectCard);
				break;
			}
		}
	}

	/**
	 * Sets the flag that indicates if the client is dead
	 * 
	 * @param isDead
	 *            the new boolean value that indicates if the client is dead
	 */
	public void setIsDead(boolean isDead) {
		this.isDead = isDead;
	}

	/**
	 * Gets the flag that indicates if the client is dead
	 * 
	 * @return the flag that indicates if the client is dead
	 */
	public boolean getIsDead() {
		return this.isDead;
	}

	/**
	 * Gets the flag that indicates if the client has escaped from the aliens
	 * 
	 * @return the flag that indicates if the client has escaped from the aliens
	 */
	public boolean getHasEscaped() {
		return this.hasEscaped;
	}

	/**
	 * Sets the flag that indicates if the client has escaped from the aliens
	 * 
	 * @param hasEscaped
	 *            the new boolean value that indicates if the client has escaped
	 *            from the aliens
	 */
	public void setHasEscaped(boolean hasEscaped) {
		this.hasEscaped = hasEscaped;
	}

	public void subscribe() throws IllegalAccessException, IOException, NoSuchMethodException, NotBoundException, InvocationTargetException, ClassNotFoundException {
        ArrayList<Object> parameter = new ArrayList<Object>();
        parameter.add(this.getToken());
        this.comSession.start("subscribe", parameter);
	}
}
