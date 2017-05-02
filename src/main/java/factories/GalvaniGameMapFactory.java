package factories;

import java.io.File;

import it.polimi.ingsw.cg_19.GameMap;

/**
 * Represents a factory of Galvani game maps
 *
 */
public class GalvaniGameMapFactory extends GameMapFactory {

	@Override
	public GameMap makeMap() {
		return new GameMap(this.makeGraph(new File("Galvani_map.txt")), 97, 1,
				23, 14, "GALVANI");
	}

}
