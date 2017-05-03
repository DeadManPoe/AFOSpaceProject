package factories;

import java.io.File;

import common.GameMap;

/**
 * Represents a factory of Fermi game maps
 * 
 * @see GameMapFactory
 * @author Andrea Sessa
 * @author Giorgio Pea
 *
 */
public class FermiGameMapFactory extends GameMapFactory {

	/**
	 * @see GameMapFactory#makeMap()
	 */
	@Override
	public GameMap makeMap() {
		return new GameMap(this.makeGraph(new File("Fermi_map.txt")), 97, 1,
				23, 14, "FERMI");
	}

}
