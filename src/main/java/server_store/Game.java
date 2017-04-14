package server_store;

import common.*;
import decks.ObjectDeck;
import decks.RescueDeck;
import decks.SectorDeck;
import it.polimi.ingsw.cg_19.GameMap;

import java.util.*;

/**
 * Created by giorgiopea on 13/03/17.
 */
public class Game {
    public final static long TURN_TIMEOUT = 10 * 60 * 1000;
    // Communication related stuff
    public static int counter = 0;

    public List<server_store.Player> players;
    public ObjectDeck objectDeck;
    public RescueDeck rescueDeck;
    public SectorDeck sectorDeck;
    public server_store.Player currentPlayer;
    public server_store.Player previousPlayer;
    public int turnNumber;
    public GamePublicData gamePublicData;
    public String mapName;
    public GameMap gameMap;
    public StoreAction lastAction;
    public List<String> nextActions;
    public RRClientNotification lastRRclientNotification;
    public PSClientNotification lastPSclientNotification;
    public Timer currentTimer;
    public boolean lastActionResult;
    public boolean didAlienWin;
    public boolean didHumansWin;

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
    }

    @Override
    public String toString() {
        return "Game{" +
                "players=" + players +
                ", objectDeck=" + objectDeck +
                ", rescueDeck=" + rescueDeck +
                ", sectorDeck=" + sectorDeck +
                ", currentPlayer=" + currentPlayer +
                ", turnNumber=" + turnNumber +
                ", gamePublicData=" + gamePublicData +
                ", mapName='" + mapName + '\'' +
                ", gameMap=" + gameMap +
                ", lastAction=" + lastAction +
                ", nextActions=" + nextActions +
                ", lastRRclientNotification=" + lastRRclientNotification +
                ", lastPSclientNotification=" + lastPSclientNotification +
                ", currentTimer=" + currentTimer +
                ", lastActionResult=" + lastActionResult +
                ", didAlienWin=" + didAlienWin +
                ", didHumansWin=" + didHumansWin +
                '}';
    }
}
