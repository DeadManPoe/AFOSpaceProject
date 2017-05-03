package factories;

import java.io.File;

import common.GameMap;

/**
 * Represents a factory of Galilei game maps
 * 
 * @see GameMapFactory
 * @author Andrea Sessa
 * @author Giorgio Pea
 */
public class GalileiGameMapFactory extends GameMapFactory {
	/**
	 * @see GameMapFactory#makeMap()
	 */
	@Override
	public GameMap makeMap() {
		return new GameMap(this.makeGraph(new File("Galileo_map.txt")), 97, 1,
				23, 14, "GALILEI");
	}
}
