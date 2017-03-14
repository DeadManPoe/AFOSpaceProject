package server_store;

import common.PlayerToken;
import common.PrivateDeck;
import common.Sector;
import it.polimi.ingsw.cg_19.PlayerState;
import it.polimi.ingsw.cg_19.PlayerType;

/**
 * Created by giorgiopea on 13/03/17.
 */
public class Player {
    public int speed;
    public PlayerToken playerToken;
    // Player's type
    public PlayerType playerType;
    // Player's current sector
    public Sector currentSector;
    // Player's state
    public PlayerState playerState;
    // Player's public deck
    public PrivateDeck privateDeck;
    public volatile boolean isSedated;
    public volatile boolean isAdrenalined;
    public volatile boolean hasMoved;
    public volatile String name;

    public Player(PlayerType playerType, String name, PlayerToken playerToken) {
        if (playerType == PlayerType.HUMAN) {
            this.speed = 1;
        } else {
            this.speed = 2;
        }
        this.playerType = playerType;
        this.playerState = PlayerState.ALIVE;
        this.privateDeck = new PrivateDeck();
        this.isAdrenalined = false;
        this.isSedated = false;
        this.currentSector = null;
        this.name = name;
        this.hasMoved = false;
        this.playerToken = playerToken;

    }
}
