package it.polimi.ingsw.cg_19;

import java.io.Serializable;
import common.PrivateDeck;
import common.Sector;

/**
 * Represents a generic player in the game
 * 
 * @author Andrea Sessa
 * @author Giorgio Pea
 * @see playerType
 * @see playerStatus
 */
public class Player implements Serializable {
	// A field used for serialization purposes
	private static final long serialVersionUID = 1L;
	// Player's speed
	private volatile int speed;
	// Player's type
	private final PlayerType playerType;
	// Player's current sector
	private volatile Sector currentSector;
	// Player's state
	private volatile PlayerState playerState;
	// Player's private deck
	private transient final PrivateDeck privateDeck;
	private volatile boolean isSedated;
	private volatile boolean isAdrenalined;
	private volatile boolean hasMoved;
	private volatile String name;

	/**
	 * Constructs a player from its type and name
	 * 
	 * @see PlayerType
	 * @param playerType
	 *            the player's type
	 * @param playerName
	 *            the player's name
	 */
	public Player(PlayerType playerType, String playerName) {
		if (playerType.toString() == "HUMAN") {
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
		this.name = playerName;
	}

	/**
	 * Gets the player's state
	 * 
	 * @return the player's state
	 */
	public PlayerState getPlayerState() {
		return playerState;
	}

	/**
	 * Sets the player's state
	 * 
	 * @param playerState
	 *            the state to be set for the player
	 */
	public void setPlayerState(PlayerState playerState) {
		this.playerState = playerState;
	}

	/**
	 * Gets the player's speed
	 * 
	 * @return the player's speed
	 */
	public int getSpeed() {
		return this.speed;
	}

	/**
	 * Sets the player's speed
	 * 
	 * @param speed
	 *            The speed to be set for the player
	 */
	public void setSpeed(int speed) {
		this.speed = speed;
	}

	/**
	 * Gets the player's type
	 * 
	 * @return the player's type
	 */
	public PlayerType getPlayerType() {
		return this.playerType;
	}

	/**
	 * Gets the player's current sector
	 * 
	 * @return the player's current sector
	 */
	public Sector getSector() {
		return currentSector;
	}

	/**
	 * Sets the player's current sector
	 * 
	 * @param sector
	 *            the current sector to be set for the player
	 */
	public void setSector(Sector sector) {
		this.currentSector = sector;
	}

	/**
	 * Gets the player's private deck
	 * 
	 * @return the player's private deck
	 */
	public PrivateDeck getPrivateDeck() {
		return privateDeck;
	}

	/**
	 * Gets the flag that indicates if the player is sedated or not
	 * 
	 * @return True if the suppressor effect is active for the player, otherwise
	 *         false
	 */
	public boolean isSedated() {
		return isSedated;
	}

	/**
	 * Sets the isSedated flag for the player
	 * 
	 * @param isSedated
	 *            true if the player has activated the suppressor effect
	 *            otherwise false
	 */
	public void setSedated(boolean isSedated) {
		this.isSedated = isSedated;
	}

	/**
	 * Gets the flag that indicates if the player is adrenalined or not
	 * 
	 * @return true if the adrenaline effect is active for the player, otherwise
	 *         false
	 */
	public boolean isAdrenaline() {
		return isAdrenalined;
	}

	/**
	 * Sets the isAdreanlined flag for the player, changes the player speed
	 * accordingly
	 * 
	 * @param isAdrenaline
	 *            true if the player has activated the adrenaline effect,
	 *            otherwise false
	 */
	public void setAdrenaline(boolean isAdrenaline) {
		if (this.playerType.equals(PlayerType.HUMAN)) {
			if (isAdrenaline) {
				this.speed = 2;
				this.isAdrenalined = true;
			} else {
				this.speed = 1;
				this.isAdrenalined = false;
			}
		}

	}

	/**
	 * Gets the flag that indicates if the player has moved
	 * 
	 * @return true if the player has already move in the game
	 */
	public boolean hasMoved() {
		return this.hasMoved;
	}

	/**
	 * Sets the flag that indicates if the player has moved
	 * 
	 * @param hasMoved
	 *            true if the player has moved, false otherwise
	 */
	public void setHasMoved(boolean hasMoved) {
		this.hasMoved = hasMoved;
	}

	/**
	 * Gets the player's name
	 * 
	 * @return the player's name
	 */
	public String getName() {
		return this.name;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (isAdrenalined ? 1231 : 1237);
		result = prime * result + (isSedated ? 1231 : 1237);
		result = prime * result
				+ ((currentSector == null) ? 0 : currentSector.hashCode());
		result = prime * result
				+ ((playerState == null) ? 0 : playerState.hashCode());
		result = prime * result
				+ ((playerType == null) ? 0 : playerType.hashCode());
		result = prime * result
				+ ((privateDeck == null) ? 0 : privateDeck.hashCode());
		result = prime * result + speed;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Player other = (Player) obj;
		if (isAdrenalined != other.isAdrenalined)
			return false;
		if (isSedated != other.isSedated)
			return false;
		if (currentSector == null) {
			if (other.currentSector != null)
				return false;
		} else if (!currentSector.equals(other.currentSector))
			return false;
		if (playerState != other.playerState)
			return false;
		if (playerType != other.playerType)
			return false;
		if (privateDeck == null) {
			if (other.privateDeck != null)
				return false;
		} else if (!privateDeck.equals(other.privateDeck))
			return false;
		if (speed != other.speed)
			return false;
		return true;
	}

}