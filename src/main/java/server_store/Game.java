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

/**
 * Created by giorgiopea on 13/03/17.
 */
public class Game {
    public final static long TURN_TIMEOUT = 10 * 60 * 1000;
    // Communication related stuff
    public static int counter = 0;

    public List<Player> players;
    public ObjectDeck objectDeck;
    public RescueDeck rescueDeck;
    public SectorDeck sectorDeck;
    public Player currentPlayer;
    public int turnNumber;
    public GamePublicData gamePublicData;
    public String mapName;
    public GameMap gameMap;
    public Action lastAction;


    public Game(String gameMapName) {
        counter++;
        this.mapName = gameMapName;
        this.gameMap = null;
        this.players = new ArrayList<Player>();
        this.gamePublicData = new GamePublicData(counter, "Game_" + counter);
        this.turnNumber = 0;
        this.players = new ArrayList<Player>();
        this.lastAction = null;
    }
}
