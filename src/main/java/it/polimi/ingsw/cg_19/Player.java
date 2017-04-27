package it.polimi.ingsw.cg_19;

import common.PlayerToken;
import common.PrivateDeck;
import common.Sector;

/**
 * Represents a generic player in the game
 *
 * @author Andrea Sessa
 * @author Giorgio Pea
 */
public class Player {
    private int speed;
    private PlayerToken playerToken;
    // Player's current sector
    private Sector currentSector;
    // Player's state
    private PlayerState playerState;
    // Player's public deck
    private PrivateDeck privateDeck;
    private volatile boolean isSedated;
    private volatile boolean isAdrenalined;
    private volatile boolean hasMoved;
    private volatile String name;


    public Player(String name) {
        if (playerToken != null) {
            if (playerToken.getPlayerType() == PlayerType.HUMAN) {
                this.speed = 1;
            } else {
                this.speed = 2;
            }
        }
        this.playerState = PlayerState.ALIVE;
        this.privateDeck = new PrivateDeck();
        this.isAdrenalined = false;
        this.isSedated = false;
        this.currentSector = null;
        this.name = name;
        this.hasMoved = false;
    }

    public void setPlayerToken(PlayerToken playerToken) {
        this.playerToken = playerToken;
    }

    public int getSpeed() {
        return speed;
    }

    public PlayerToken getPlayerToken() {
        return playerToken;
    }

    public Sector getCurrentSector() {
        return currentSector;
    }

    public PlayerState getPlayerState() {
        return playerState;
    }

    public PrivateDeck getPrivateDeck() {
        return privateDeck;
    }

    public boolean isSedated() {
        return isSedated;
    }

    public boolean isAdrenalined() {
        return isAdrenalined;
    }

    public boolean isHasMoved() {
        return hasMoved;
    }

    public String getName() {
        return name;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public void setCurrentSector(Sector currentSector) {
        this.currentSector = currentSector;
    }

    public void setPlayerState(PlayerState playerState) {
        this.playerState = playerState;
    }

    public void setSedated(boolean sedated) {
        isSedated = sedated;
    }

    public void setAdrenalined(boolean adrenalined) {
        isAdrenalined = adrenalined;
    }

    public void setHasMoved(boolean hasMoved) {
        this.hasMoved = hasMoved;
    }

    public void setPrivateDeck(PrivateDeck privateDeck) {
        this.privateDeck = privateDeck;
    }
}