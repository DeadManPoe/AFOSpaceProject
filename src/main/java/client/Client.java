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


    public static Client getInstance() {
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

    public void setPlayer(String playerName) {
        this.player = new Player(playerName);
    }

    /**
     * Gets the boolean that indicates if the game the client has joined has
     * started or not
     *
     * @return the boolean that indicates if the game the client has joined has
     * started or not
     */
    public synchronized boolean isGameStarted() {
        return isGameStarted;
    }

    /**
     * Sets the boolean that indicates if the game the client has joined has
     * started or not
     *
     * @param gameStarted the new boolean that indicates if the game the client has
     *                    joined has started or not
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
     * @param map the new map of the game the client is playinh
     */
    public void setGameMap(GameMap map) {
        this.gameMap = map;
    }

    /**
     * Sets the client's current notification to be handled
     *
     * @param notification the new notification to be handled by the client
     */
    public void setCurrentRrNotification(RRClientNotification notification) {
        this.currentRrNotification = notification;

    }

    /**
     * Gets the boolean that indicates if the current turn is the client's turn
     *
     * @return the boolean that indicates if the current turn is the client's
     * turn
     */
    public boolean getIsMyTurn() {
        return isMyTurn;
    }

    /**
     * Sets the boolean that indicates if the current turn is the client's turn
     * and notify the gui about the new turn state
     *
     * @param isMyTurn the new boolean that indicates if the current turn is the
     *                 client's turn
     */
    public void setIsMyTurn(boolean isMyTurn) {
        this.isMyTurn = isMyTurn;
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
     * @param avGames the new games the client could join
     */
    public void setAvailableGames(ArrayList<GamePublicData> avGames) {
        this.availableGames = avGames;
    }

    public void move(Coordinate coordinate) {
        Sector targetSector = this.getGameMap().getSectorByCoords(coordinate);
        this.player.setHasMoved(true);
        this.player.setCurrentSector(targetSector);
    }

    public void endTurn() {
        this.player.setSedated(false);
        this.player.setAdrenalined(false);
        this.player.setHasMoved(false);
        this.isMyTurn = false;
    }

    public PSClientNotification getCurrentPubSubNotification() {
        return this.currentPsNotification;
    }

    public void setCurrentPubSubNotification(PSClientNotification notification) {
        this.currentPsNotification = notification;
    }


    public void teleport() {
        if (this.player.getPlayerToken().getPlayerType().equals(PlayerType.HUMAN)) {
            this.player.setCurrentSector(this.gameMap.getHumanSector());
        } else {
            this.player.setCurrentSector(this.gameMap.getAlienSector());
        }

    }

    public Player getPlayer() {
        return this.player;
    }
}
