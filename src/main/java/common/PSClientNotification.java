package common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a notification to be delivered to all the subscribers of a topic
 * in the logic of the publisher-subscriber pattern.
 * 
 * @see ClientNotification
 * @author Andrea Sessa
 * @author Giorgio Pea
 * @version 1.0
 */

public class PSClientNotification extends ClientNotification implements
		Serializable {
	private static final long serialVersionUID = 1L;

	private ArrayList<PlayerToken> deadPlayers;
	private ArrayList<PlayerToken> attackedPlayers;
	private boolean humanWins;
	private boolean alienWins;
	private PlayerToken escapedPlayer;


    /**
	 * Constructs a notification o be delivered to all the subscribers of a
	 * topic in the logic of the publisher-subscriber pattern. An empty list of
	 * dead players and attacked players is automatically created as well
	 */
	public PSClientNotification() {
		super("");
		deadPlayers = new ArrayList<PlayerToken>();
		attackedPlayers = new ArrayList<PlayerToken>();
	}

	/**
	 * Gets the boolean value that indicates if the humans have won the game
	 * 
	 * @return the boolean value that indicates if the humans have won the game
	 */
	public boolean getHumanWins() {
		return humanWins;
	}

	/**
	 * Sets the boolean value that indicates if the humans have won the game
	 * 
	 * @param humanWins
	 *            the new boolean value that indicates if the humans have won
	 *            the game
	 */
	public void setHumanWins(boolean humanWins) {
		this.humanWins = humanWins;
	}

	/**
	 * Gets the boolean value that indicates if the aliens have won the game
	 * 
	 * @return the boolean value that indicates if the aliens have won the game
	 */
	public boolean getAlienWins() {
		return alienWins;
	}

	/**
	 * Sets the boolean value that indicates if the aliens have won the game
	 * 
	 * @param alienWins
	 *            the new boolean value that indicates if the aliens have won
	 *            the game
	 */
	public void setAlienWins(boolean alienWins) {
		this.alienWins = alienWins;
	}

	/**
	 * Gets the list of players who have died during the last action
	 * 
	 * @return the list of players who have died during the last action
	 */
	public List<PlayerToken> getDeadPlayers() {
		return deadPlayers;
	}

	/**
	 * Adds a new player to the list of dead players
	 * 
	 * @param deadPlayers
	 *            the player to add to the list
	 * @throws IllegalArgumentException
	 *             if deadPlayer is null
	 */
	public void addDeadPlayers(PlayerToken deadPlayer) {
		if (deadPlayer == null)
			throw new IllegalArgumentException("deadPlayer must not be null");
		this.deadPlayers.add(deadPlayer);
	}

	/**
	 * Gets the list of the players attacked during the last action
	 * 
	 * @return the list of the players attacked during the last action
	 */
	public List<PlayerToken> getAttackedPlayers() {
		return attackedPlayers;
	}

	/**
	 * Adds a new player to the list of the attacked player
	 * 
	 * @param attackedPlayers
	 * @throws IllegalArgumentExceptio
	 *             if attackedPlayer is null
	 */
	public void addAttackedPlayers(PlayerToken attackedPlayer) {
		if (attackedPlayer == null)
			throw new IllegalArgumentException("attackPlayer must not be null");
		this.attackedPlayers.add(attackedPlayer);
	}

	public PlayerToken getEscapedPlayer() {
		return escapedPlayer;
	}

	public void setEscapedPlayer(PlayerToken escapedPlayer) {
		this.escapedPlayer = escapedPlayer;
	}
}
