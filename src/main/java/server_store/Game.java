package server_store;

import common.Action;
import common.GamePublicData;
import common.PlayerToken;
import decks.ObjectDeck;
import decks.RescueDeck;
import decks.SectorDeck;
import factories.FermiGameMapFactory;
import factories.GalileiGameMapFactory;
import factories.GalvaniGameMapFactory;
import it.polimi.ingsw.cg_19.GameMap;
import it.polimi.ingsw.cg_19.Player;
import server.SubscriberHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by giorgiopea on 13/03/17.
 */
public class Game {
    public final static long TURN_TIMEOUT = 10 * 60 * 1000;
    // Communication related stuff
    public static int counter = 0;

    public Map<PlayerToken, server_store.Player> players;
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


    public Game(String gameMapName) {
        counter++;
        this.mapName = gameMapName;
        this.gameMap = null;
        this.players = new HashMap<PlayerToken, server_store.Player>();
        this.gamePublicData = new GamePublicData(counter, "Game_" + counter);
        this.turnNumber = 0;
        this.lastAction = null;
    }
}
