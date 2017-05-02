package common;

import java.io.Serializable;

/**
 * Represents a generic coordinate in the game. In the game a generic coordinate
 * is a pair: character, integer
 *
 */

public class Coordinate implements Serializable {
	// A field used only for serialization purposes
	private static final long serialVersionUID = 1L;
	private final char x;
	private final int y;

	/**
	 * Constructs a coordinate from an x and y values
	 * 
	 * @param x
	 *            first member of the coordinate
	 * @param y
	 *            second member of the coordinate
	 */
	public Coordinate(char x, int y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Gets the x value of the coordinate
	 * 
	 * @return the x value of the coordinate
	 */
	public char getX() {
		return x;
	}

	/**
	 * Gets the y value of the coordinate
	 * 
	 * @return the y value of the coordinate
	 */
	public int getY() {
		return y;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + x;
		result = prime * result + y;
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
		Coordinate other = (Coordinate) obj;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return x + "" + y;
	}

}
