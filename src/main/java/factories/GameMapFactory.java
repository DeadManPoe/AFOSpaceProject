package factories;

import it.polimi.ingsw.cg_19.GameMap;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.jgrapht.UndirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import common.Coordinate;
import common.Sector;
import common.SectorType;

/**
 * Represents a factory of generic game maps
 * 
 * @see GameMap
 * @author Andrea Sessa
 * @author Giorgio Pea
 */
public abstract class GameMapFactory {
	/**
	 * Makes the graph representing a game map
	 * 
	 * @param file
	 *            the file that represent contains the map's information
	 * @return The undirected graph representing a map
	 */
	public UndirectedGraph<Sector, DefaultEdge> makeGraph(File file) {
		UndirectedGraph<Sector, DefaultEdge> graph = new SimpleGraph<Sector, DefaultEdge>(
				DefaultEdge.class);
		// Intermediate data-structure from which to build the map graph
		// Sectors are organized in columns, based on their coordinate
		List<List<Sector>> listOfColsOfSectors = new ArrayList<List<Sector>>();
		List<Sector> colOfSectors = new ArrayList<Sector>();
		BufferedReader fileReaderBuffer;
		Coordinate coordinate;
		Sector sector;
		String line;
		char xCoord, firstEdge, secondEdge, thirdEdge;
		int i = 0, j = 0;
		xCoord = '/';

		try {

			// Reading file and adding vertices(sectors) with their coordinates
			// to the map graph using information in file
			fileReaderBuffer = new BufferedReader(new FileReader(file));

			while ((line = fileReaderBuffer.readLine()) != null) {
				// line.charAt(4) identifies the x coordinate of the sector from
				// the file
				// line.charAt(5) identifies the y coordinate of the sector from
				// the file, for simplicity characters are used in
				// the file instead of numbers, and the conversion is made using
				// ASCII table char values
				// e.g: ASCII(a)=97 , 97-96=1
				coordinate = new Coordinate(line.charAt(4),
						(int) (line.charAt(5)) - 96);
				// Column change based on coordinates: if the x coord of the
				// current sector
				// is not equal to xCoord(the xcoord of the previous sector
				// analyzed) then
				// when need to add a column to listOfColsOfSectors
				if (coordinate.getX() != xCoord) {
					// Current to which the sectors belong
					colOfSectors = new ArrayList<Sector>();
					listOfColsOfSectors.add(colOfSectors);
				}
				// current coordinate
				xCoord = coordinate.getX();
				switch (line.charAt(0)) {
				case 'd':
					sector = new Sector(coordinate, SectorType.DANGEROUS);
					colOfSectors.add(sector);
					graph.addVertex(sector);
					break;
				case 's':
					sector = new Sector(coordinate, SectorType.SAFE);
					colOfSectors.add(sector);
					graph.addVertex(sector);
					break;
				case 'r':
					sector = new Sector(coordinate, SectorType.OPEN_RESCUE);
					colOfSectors.add(sector);
					graph.addVertex(sector);
					break;
				case 'h':
					sector = new Sector(coordinate, SectorType.HUMAN);
					colOfSectors.add(sector);
					graph.addVertex(sector);
					break;
				case 'a':
					sector = new Sector(coordinate, SectorType.ALIEN);
					colOfSectors.add(sector);
					graph.addVertex(sector);
					break;
				}
			}
			fileReaderBuffer.close();
		} catch (FileNotFoundException e) {
            e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			// Reading file and adding edges that connects the vertices(sectors)
			// of the map graph using information in file
			// and the listOfColsOfSectors created above
			// line.charAt(1), line.charAt(2), line.charAt(3) identify from the
			// file the 3 possible sectors connected to the current one
			// in terms of indexes of sectors in the sector's list
			// for simplicity characters are used instead of numbers and the
			// conversion is made using ASCII table char values
			// e.g: ASCII(a)=97 , 97-97=0
			fileReaderBuffer = new BufferedReader(new FileReader(file));
			while ((line = fileReaderBuffer.readLine()) != null) {
				// Current column to which the sectors belong
				colOfSectors = listOfColsOfSectors.get(i);
				// Index change in order to accessing the columns and their
				// sectors
				if (colOfSectors.size() <= j) {
					i++;
					j = 0;
				}
				firstEdge = line.charAt(1);
				secondEdge = line.charAt(2);
				thirdEdge = line.charAt(3);
				if (firstEdge == 't') {
					// The current sector has a bottom edge
					graph.addEdge(listOfColsOfSectors.get(i).get(j),
							listOfColsOfSectors.get(i).get(j + 1));
				}
				if (secondEdge != '_') {
					// The current sector has a top right edge
					graph.addEdge(
							listOfColsOfSectors.get(i).get(j),
							listOfColsOfSectors.get(i + 1).get(
									(int) (secondEdge) - 97));
				}
				if (thirdEdge != '_') {
					// The current sector has a bottom right edge
					graph.addEdge(
							listOfColsOfSectors.get(i).get(j),
							listOfColsOfSectors.get(i + 1).get(
									(int) (thirdEdge) - 97));
				}
				j++;
			}
		} catch (FileNotFoundException e) {
            e.printStackTrace();
		} catch (IOException e) {
            e.printStackTrace();
		}
		return graph;
	}

	/**
	 * Makes a generic game map
	 * 
	 * @return a generic game map
	 */
	public abstract GameMap makeMap();
}
