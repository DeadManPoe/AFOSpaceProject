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
	// The universally unique identifier of the player
	private final UUID playerId;
	public final Integer gameId;
	public final PlayerType playerType;

	/**
	 * Constructs a token that identifies in an unique way a player. This token
	 * is constructed from the type of the player it refers to, and produces a
	 * universal unique identifier for the player.
	 * 
	 * @param playerType
	 *            the type of the player the token refers to
	 * @see PlayerType
	 */
	public PlayerToken(PlayerType playerType, Integer gameId) {
		this.playerId = UUID.randomUUID();
		this.playerType = playerType;
		this.gameId = gameId;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		PlayerToken that = (PlayerToken) o;

		return playerId.equals(that.playerId);

	}

	@Override
	public int hashCode() {
		return 0;
	}

	@Override
	public String toString() {
		return "PlayerToken{" +
				"playerId=" + playerId +
				", gameId=" + gameId +
				", playerType=" + playerType +
				'}';
	}
}
