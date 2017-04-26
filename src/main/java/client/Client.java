package client;

import it.polimi.ingsw.cg_19.GameMap;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.Level;

import common.Action;
import common.AttackObjectCard;
import common.Coordinate;
import common.DefenseObjectCard;
import common.DiscardAction;
import common.GamePublicData;
import common.GlobalNoiseSectorCard;
import common.LightsObjectCard;
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
import it.polimi.ingsw.cg_19.Player;
import it.polimi.ingsw.cg_19.PlayerState;
import it.polimi.ingsw.cg_19.PlayerType;

/**
 * Represents a client in the client server communication layer of the
 * application
 *
 * @author Andrea Sessa
 * @author Giorgio Pea
 */
public class Client {
    private Player player;
	// The map of the game the client is playing
	private GameMap gameMap;
	// The current rr notification
	private RRClientNotification currentRrNotification;
    // The current ps notification
    private PSClientNotification currentPsNotification;
	// A flag that indicates that the game is started
	private boolean isGameStarted;
	// A flag that indicates that the client holds the turn
	private boolean isMyTurn;
    private boolean isGameEnded;
	/*
	 * A thread that handles async server notification in the logic of the
	 * pub/sub pattern
	 */
	private PubSubHandler pubSubHandler;
	// The games the client could join
	private ArrayList<GamePublicData> availableGames;

	private static Client instance = new Client();


    public static Client getInstance(){
        return instance;
    }

	/**
	 * Constructs a client from its connection details. The services the client
	 * offers to the server to exchange data are automatically created, along
	 * with the client's visualization layer, and associated to the client.
	 *
	 * @throws IOException
	 * @throws SecurityException
	 */
	private Client() {
        this.isGameStarted = false;
        this.isMyTurn = false;
	}

	public void setPlayer(PlayerToken playerToken, String playerName){
        this.player = new Player(playerName, playerToken);
        this.player.setPlayerState(PlayerState.ALIVE);
    }

    public PlayerToken getToken(){
        return this.player.getPlayerToken();
    }
    public PlayerType getPlayerType(){
        return this.player.getPlayerToken().getPlayerType();
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
		return this.isGameEnded;
	}

	public synchronized void setGameEnded(boolean gameEnded) {
		this.isGameEnded = gameEnded;
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
	 *            the new client's private deck of object cards
	 */
	public PrivateDeck getPrivateDeck() {
		return this.player.getPrivateDeck();
	}
	public void setPrivateDeck(PrivateDeck privateDeck){
        this.player.setPrivateDeck(privateDeck);
    }


	/**
	 * Sets the client's current notification to be handled
	 *
	 * @param notification
	 *            the new notification to be handled by the client
	 */
	public void setCurrentRrNotification(RRClientNotification notification) {
		this.currentRrNotification = notification;

	}

	/**
	 * Gets the game map's sector in which the client(player) is located
	 *
	 * @return the game map's sector in which the client(player) is located
	 */
	public Sector getCurrentSector() {
		return this.player.getCurrentSector();
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
		this.player.setCurrentSector(sector);
	}

	/*
	 * Gets the notification the client has received by the server in response
	 * to a request
	 * 
	 * @return the notification the client has received by the server in
	 *         response to
	 */
	public RRClientNotification getCurrentRrNotification() {
		return currentRrNotification;
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
	public void move(Coordinate coordinate){
		Sector targetSector = this.getGameMap().getSectorByCoords(coordinate);
		this.player.setHasMoved(true);
        this.player.setCurrentSector(targetSector);
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
				if (this.getCurrentRrNotification().getActionResult()) {
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
	public void endTurn() {
        this.player.setSedated(false);
        this.player.setAdrenalined(false);
		this.player.setHasMoved(false);
        this.isMyTurn = false;
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
			if (this.getCurrentRrNotification().getActionResult()) {
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
	 * Gets the flag that indicates if the client has moved or not
	 * 
	 * @return the flag that indicates if the client has moved or not
	 */
	public boolean getHasMoved() {
		return this.player.isHasMoved();
	}

	/**
	 * Gets the flag that indicates if the client has moved or not
	 * 
	 * @param hasMoved
	 *            the new boolean value that indicates if the client has moved
	 *            or not
	 */
	public void setHasMoved(boolean hasMoved) {
		this.player.setHasMoved(hasMoved);
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

    public PSClientNotification getCurrentPubSubNotification() {
        return this.currentPsNotification;
    }
    public void setCurrentPubSubNotification(PSClientNotification notification){
        this.currentPsNotification = notification;
    }


    public PlayerState getPlayerState() {
        return this.player.getPlayerState();
    }

    public void teleport() {
        if (this.player.getPlayerToken().getPlayerType().equals(PlayerType.HUMAN)){
            this.player.setCurrentSector(this.gameMap.getHumanSector());
        }
        else {
            this.player.setCurrentSector(this.gameMap.getAlienSector());
        }

    }
}
