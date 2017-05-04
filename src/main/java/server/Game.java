package server;

import common.*;
import decks.ObjectDeck;
import decks.RescueDeck;
import decks.SectorDeck;
import common.GameMap;
import server_store.StoreAction;

import java.util.*;

/**
 * Represents a generic game.
 *
 */
public class Game {
    // Communication related stuff
    public static int counter = 0;

    private List<Player> players;
    private ObjectDeck objectDeck;
    private RescueDeck rescueDeck;
    private SectorDeck sectorDeck;
    private Player currentPlayer;
    private Player previousPlayer;
    private int turnNumber;
    private GamePublicData gamePublicData;
    private String mapName;
    private GameMap gameMap;
    private StoreAction lastAction;
    private List<String> nextActions;
    private RRClientNotification lastRRclientNotification;
    private PSClientNotification lastPSclientNotification;
    private Timer currentTimer;
    private boolean lastActionResult;
    private boolean didAlienWin;
    private boolean didHumansWin;
    private String lastChatMsg;

    public Game(String gameMapName) {
        counter++;
        this.mapName = gameMapName;
        this.gameMap = null;
        this.players = new ArrayList<>();
        this.gamePublicData = new GamePublicData(counter, "Game_" + counter);
        this.turnNumber = 0;
        this.lastAction = null;
        this.lastPSclientNotification = null;
        this.lastRRclientNotification = null;
        this.currentTimer = null;
        this.lastActionResult = true;
        this.didHumansWin = false;
        this.didHumansWin = false;
        this.lastChatMsg = "";
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public ObjectDeck getObjectDeck() {
        return objectDeck;
    }

    public void setObjectDeck(ObjectDeck objectDeck) {
        this.objectDeck = objectDeck;
    }

    public RescueDeck getRescueDeck() {
        return rescueDeck;
    }

    public void setRescueDeck(RescueDeck rescueDeck) {
        this.rescueDeck = rescueDeck;
    }

    public SectorDeck getSectorDeck() {
        return sectorDeck;
    }

    public void setSectorDeck(SectorDeck sectorDeck) {
        this.sectorDeck = sectorDeck;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public Player getPreviousPlayer() {
        return previousPlayer;
    }

    public void setPreviousPlayer(Player previousPlayer) {
        this.previousPlayer = previousPlayer;
    }

    public int getTurnNumber() {
        return turnNumber;
    }

    public void setTurnNumber(int turnNumber) {
        this.turnNumber = turnNumber;
    }

    public GamePublicData getGamePublicData() {
        return gamePublicData;
    }

    public void setGamePublicData(GamePublicData gamePublicData) {
        this.gamePublicData = gamePublicData;
    }

    public String getMapName() {
        return mapName;
    }

    public void setMapName(String mapName) {
        this.mapName = mapName;
    }

    public GameMap getGameMap() {
        return gameMap;
    }

    public void setGameMap(GameMap gameMap) {
        this.gameMap = gameMap;
    }

    public StoreAction getLastAction() {
        return lastAction;
    }

    public void setLastAction(StoreAction lastAction) {
        this.lastAction = lastAction;
    }

    public List<String> getNextActions() {
        return nextActions;
    }

    public void setNextActions(List<String> nextActions) {
        this.nextActions = nextActions;
    }

    public RRClientNotification getLastRRclientNotification() {
        return lastRRclientNotification;
    }

    public void setLastRRclientNotification(RRClientNotification lastRRclientNotification) {
        this.lastRRclientNotification = lastRRclientNotification;
    }

    public PSClientNotification getLastPSclientNotification() {
        return lastPSclientNotification;
    }

    public void setLastPSclientNotification(PSClientNotification lastPSclientNotification) {
        this.lastPSclientNotification = lastPSclientNotification;
    }

    public Timer getCurrentTimer() {
        return currentTimer;
    }

    public void setCurrentTimer(Timer currentTimer) {
        this.currentTimer = currentTimer;
    }

    public boolean isLastActionResult() {
        return lastActionResult;
    }

    public void setLastActionResult(boolean lastActionResult) {
        this.lastActionResult = lastActionResult;
    }

    public boolean isDidAlienWin() {
        return didAlienWin;
    }

    public void setDidAlienWin(boolean didAlienWin) {
        this.didAlienWin = didAlienWin;
    }

    public boolean isDidHumansWin() {
        return didHumansWin;
    }

    public void setDidHumansWin(boolean didHumansWin) {
        this.didHumansWin = didHumansWin;
    }

    public void setLastChatMsg(String message) {
        this.lastChatMsg = message;
    }

    public String getLastChatMsg() {
        return lastChatMsg;
    }
}
