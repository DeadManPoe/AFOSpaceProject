package common;

import it.polimi.ingsw.cg_19.PlayerType;

import java.io.Serializable;
import java.util.UUID;

/**
 * Represents a token that identifies in an unique way a player and stores its
 * type.
 * 
 * @author Andrea Sessa
 * @author Giorgio Pea
 * @version 1.0
 */
public class PlayerToken implements Serializable {
	// A field automatically created for serialization purposes
	private static final long serialVersionUID = 1L;
	// The universally unique identifier of the player
	private final UUID playerId;
	private final PlayerType playerType;
	private final int gameId;

	public int getGameId() {
		return gameId;
	}

	/**
	 * Constructs a token that identifies in an unique way a player. This token
	 * is constructed from the type of the player it refers to, and produces a
	 * universal unique identifier for the player.
	 * 
	 * @param playerType
	 *            the type of the player the token refers to
	 * @see PlayerType
	 */
	public PlayerToken(PlayerType playerType, int gameId) {
		this.playerId = UUID.randomUUID();
		this.playerType = playerType;
		this.gameId = gameId;
	}
	/**
	 * Gets the type of the player the token refers to
	 * 
	 * @return the type of the player the token refers to
	 */
	public PlayerType getPlayerType() {
		return playerType;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((playerId == null) ? 0 : playerId.hashCode());
		result = prime * result
				+ ((playerType == null) ? 0 : playerType.hashCode());
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
		PlayerToken other = (PlayerToken) obj;
		if (playerId == null) {
			if (other.playerId != null)
				return false;
		} else if (!playerId.equals(other.playerId))
			return false;
		if (playerType != other.playerType)
			return false;
		return true;
	}
}
