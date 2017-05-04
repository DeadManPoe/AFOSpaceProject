package common;

import java.io.Serializable;

/**
 * Represents a container of public data concerning a game
 * 
 * @author Andrea Sessa
 * @author Giorgio Pea
 * @version 1.0
 */
public class GamePublicData implements Serializable {
	// A field automatically created for serialization purposes
	private static final long serialVersionUID = 1L;
	// The id of the game
	private final int id;
	// The name of the game
	private final String name;
	// The number of players in the game
	private int numberOfPlayers;
	// Status of the game
	private GameStatus status;

	/**
	 * Constructs a container of public data concerning a game from: the id of
	 * the game , the name of the game, a number of zero players and a status of
	 * "OPEN"
	 * 
	 * @param id
	 *            the id of the game this container refers to
	 * @param name
	 *            the name of the game this container refers to
	 */
	public GamePublicData(int id, String name) {
		this.id = id;
		this.name = name;
		this.numberOfPlayers = 0;
		this.status = GameStatus.OPEN;
	}

	/**
	 * Increases the number of players of the game this container refers to
	 */
	public void addPlayer() {
		this.numberOfPlayers++;
	}

	/**
	 * Gets the id of the game this container refers to
	 * 
	 * @return the id of the game this container refers to
	 */
	public int getId() {
		return id;
	}

	/**
	 * Gets the status of the game this container refers to
	 * 
	 * @return the status of the game this container refers to
	 */
	public GameStatus getStatus() {
		return status;
	}

	/**
	 * Sets the status of the game this container refers to
	 * 
	 * @param status
	 *            the new status of the game this container refers to
	 */
	public void setStatus(GameStatus status) {
		this.status = status;
	}

	public int getPlayersCount() {
		return this.numberOfPlayers;
	}

	@Override
	public String toString() {
		return "[name= " + name + ", numberOfPlayers= " + numberOfPlayers
				+ ", status= " + status.toString() + " ]";
	}

}
