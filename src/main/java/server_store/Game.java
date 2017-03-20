package server_store;

import common.*;
import decks.ObjectDeck;
import decks.RescueDeck;
import decks.SectorDeck;
import factories.FermiGameMapFactory;
import factories.GalileiGameMapFactory;
import factories.GalvaniGameMapFactory;
import it.polimi.ingsw.cg_19.GameMap;
import it.polimi.ingsw.cg_19.Player;
import server.SubscriberHandler;

import java.util.*;

/**
 * Created by giorgiopea on 13/03/17.
 */
public class Game {
    public final static long TURN_TIMEOUT = 10 * 60 * 1000;
    // Communication related stuff
    public static int counter = 0;

    public List<server_store.Player> players;
    public Map<String, PlayerToken> playerNameToToken;
    public ObjectDeck objectDeck;
    public RescueDeck rescueDeck;
    public SectorDeck sectorDeck;
    public server_store.Player currentPlayer;
    public int turnNumber;
    public GamePublicData gamePublicData;
    public String mapName;
    public GameMap gameMap;
    public Action lastAction;
    public List<Class<? extends Action>> nextActions;
    public RRClientNotification lastRRclientNotification;
    public PSClientNotification lastPSclientNotification;
    public Timer currentTimer;

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
    }
}
