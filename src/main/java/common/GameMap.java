package common;

import java.util.*;

import org.jgrapht.UndirectedGraph;
import org.jgrapht.alg.NeighborIndex;
import org.jgrapht.graph.DefaultEdge;

import common.Coordinate;
import common.Sector;
import common.SectorType;

/**
 * Represents a generic map in the game Use the JGraphT library to represent the
 * game map structure in terms of an undirected graph
 * 
 * @author Andrea Sessa
 * @author Giorgio Pea
 * @version 1.1
 */
public class GameMap {

	// The map's associated graph
	private UndirectedGraph<Sector, DefaultEdge> graph;
	// A searchable version of the map's associated graph
	private NeighborIndex<Sector, DefaultEdge> searchableGraph;
	// The human starting sector
	private final Sector humanSector;
	// The alien starting sector
	private final Sector alienSector;
	// The horizontal span of the map
	private final int horizontalLength;
	// The vertical span of the map
	private final int verticalLength;
	// The starting horizontal coordinate
	private final int startingHorizontalCoord;
	// The starting vertical coordinate
	private final int startingVerticalCoord;
	// The map's name
	private final String name;
	// The rescue sectors of the map
	private List<Sector> escapes;

	/**
	 * Constructs a generic map in the game from an undirected graph, from
	 * informations about its table like representation and from its name. A
	 * searchable version of the undirected graph given is created and
	 * references to the map's alien sectors and human sectors are saved.
	 * 
	 * @param graph
	 *            the graph associated with the map
	 * @param startingHorizontalCoord
	 *            considering a table like representation of the map, the map's
	 *            starting horizontal coordinate
	 * @param startingVerticalCoord
	 *            considering a table like representation of the map, the map's
	 *            starting vertical coordinate of the map
	 * @param horizontalLength
	 *            considering a table like representation of the map, the map's
	 *            number of columns
	 * @param verticalLength
	 *            considering a table like representation of the map, the map's
	 *            number of rows
	 * @param name
	 *            the map's name
	 */
	public GameMap(UndirectedGraph<Sector, DefaultEdge> graph,
			int startingHorizontalCoord, int startingVerticalCoord,
			int horizontalLength, int verticalLength, String name) {
		this.name = name;
		this.graph = graph;
		this.searchableGraph = new NeighborIndex<Sector, DefaultEdge>(
				this.graph);
		this.startingHorizontalCoord = startingHorizontalCoord;
		this.startingVerticalCoord = startingVerticalCoord;
		this.horizontalLength = horizontalLength;
		this.verticalLength = verticalLength;
		this.humanSector = this.getSectorByType(SectorType.HUMAN);
		this.alienSector = this.getSectorByType(SectorType.ALIEN);
		this.escapes = this.findEscapes();
	}

	/**
	 * Gets the map's name
	 * 
	 * @return the map's name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Considering a table like representation of the map, gets the map's
	 * starting horizontal coordinate
	 * 
	 * @return the map's starting horizontal coordinate
	 */
	public int getStartingHorizontalCoord() {
		return startingHorizontalCoord;
	}

	/**
	 * Considering a table like representation of the map, gets the map's
	 * starting vertical coordinate
	 * 
	 * @return the map's starting vertical coordinate
	 */
	public int getStartingVerticalCoord() {
		return startingVerticalCoord;
	}

	/**
	 * Considering a table like representation of the map, gets the map's number
	 * of rows
	 * 
	 * @return the map's number of rows
	 */
	public int getVerticalLength() {
		return verticalLength;
	}

	/**
	 * Considering a table like representation of the map, gets the map's number
	 * of columns
	 * 
	 * @return the map's number of columns
	 */
	public int getHorizontalLength() {
		return horizontalLength;
	}

	/**
	 * @return A reference to the graph data structure, used for testing
	 *         purposes
	 */
	public UndirectedGraph<Sector, DefaultEdge> getGraph() {
		return this.graph;
	}

	/**
	 * Gets the map's searchable graph
	 * 
	 * @return the map's searchable graph
	 */
	public NeighborIndex<Sector, DefaultEdge> getSearchableGraph() {
		return searchableGraph;
	}

	/**
	 * Gets the map's human sector
	 * 
	 * @return Gets the map's human sector
	 */
	public Sector getHumanSector() {
		return humanSector;
	}

	/**
	 * Gets the map's alien sector
	 * 
	 * @return Gets the map's alien sector
	 */
	public Sector getAlienSector() {
		return alienSector;
	}

	/**
	 * Gets a map's sector from a given coordinate
	 * 
	 * @param coordinate
	 *            the coordinate of the sector to be returned
	 * @return the map's sector whose coordinate is the one specified
	 */
	public Sector getSectorByCoords(Coordinate coordinate) throws NoSuchElementException {
		/*
		 * Get list of sectors in the graph and scans this list in order to find
		 * the correct sector if the sector doesn't exist returns null
		 */
		Set<Sector> sectors = this.graph.vertexSet();
		for (Sector s : sectors) {
			if (s.getCoordinate().equals(coordinate)) {
				return s;
			}
		}
		return null;
	}

	/**
	 * Gets the first map's sector that matches a given sector type
	 * 
	 * @param sectorType
	 *            the type of sector, whose first occurrence in the map we want
	 *            to return
	 * @return the first map's sector that matches the given sector type
	 */
	public Sector getSectorByType(SectorType sectorType) {
		/*
		 * Get list of sectors in the graph and scans this list in order to find
		 * the correct sector if the sector doesn't exist returns null
		 */
		Set<Sector> sectors = this.graph.vertexSet();
		for (Sector s : sectors) {
			if (s.getSectorType() == sectorType) {
				return s;
			}
		}
		return null;
	}

    /**
     * Checks if a given sector is reachable from another given sector within, or exactly with, a certain distance
     * @param sourceSector The starting sector
     * @param targetSector The sector whose adjacency we want to check w.r.t a given starting sector
     * @param maxDistance The distance within which the given target sector must be reachable from the given source sector
     * @param forceMaxMovement A flag that forces the algorythm to check sector reachable with exactly the given distance
     * @return True if the reachability is verified with success, otherwise false
     */
	public boolean checkSectorAdiacency(Sector sourceSector, Sector targetSector,int maxDistance,boolean forceMaxMovement){
        List<List<Sector>> visitedSectors = new ArrayList<>();
        List<Sector> neighbors = this.searchableGraph.neighborListOf(sourceSector);
        visitedSectors.add(neighbors);
        for (int i=1; i<maxDistance; i++){
            visitedSectors.add(new ArrayList<Sector>());
            for (Sector sector : visitedSectors.get(i-1)){
                neighbors = this.searchableGraph.neighborListOf(sector);
                for ( Sector neighbor : neighbors){
                    if (!neighbor.isHasBeenChecked()){
                            visitedSectors.get(i).add(neighbor);
                    }

                }
            }
        }
        for (List<Sector> sectorList : visitedSectors){
            for (Sector sector : sectorList){
                sector.setHasBeenChecked(false);
            }
        }
        if (forceMaxMovement){
            for ( Sector sector : visitedSectors.get(maxDistance-1)){
                if (sector.equals(targetSector)){
                    return true;
                }
            }
        }
        else {
            for (List<Sector> sectorList : visitedSectors){

                for (Sector sector : sectorList){
                    if (sector.equals(targetSector)){
                        return true;
                    }
                }
            }
        }
        return false;
	}

	/**
	 * Checks if two map's sector are adjacent according to a given maximum
	 * distance
	 * 
	 * @param sourceSector
	 *            the first sector
	 * @param targetSector
	 *            the second sector
	 * @param maxLen
	 *            the maximum distance to consider to check if the two given
	 *            sectors are adjacent
	 * @param currLen
	 *            MUST always be 0
	 * @param playerType
	 *            the type of player, true if human, false if alien
	 * 
	 * @param equals
	 *            If True then the function return True iff the distance between
	 *            sourceSector and targetSector equals to maxLen, if False than
	 *            the distance between sourceSector and targetSecto could be
	 *            lower or equals to maxLen
	 * @return true if source and target are adjacent according to player and
	 *         speed
	 */
	public boolean checkSectorAdiacency(Sector sourceSector,
			Sector targetSector, int maxLen, int currLen,
			PlayerType playerType, Sector startingSector, boolean equals) {
		/*
		 * A recursive function. At each level of recursion source contains the
		 * sector from which building the path toward target. If source isn't
		 * crossable by player(p) or if the current path length, depth, is equal
		 * to speed without reaching the target, the current branch of the
		 * recursion ends with false otherwise the recursion proceeds until
		 * source == target and the correct path length(depth) has been reached.
		 */
		if (currLen >= maxLen && !sourceSector.equals(targetSector))
			return false;
		Iterator<Sector> listIterator;
		// If Human
		if (playerType == PlayerType.HUMAN) {
			// Returns false because the currLen of the path is equal to maxLen
			// but source is different from target
			if (sourceSector.equals(targetSector)
					&& sourceSector.getSectorType() != SectorType.ALIEN) {
				if (equals && currLen == maxLen)
					return true;
				if (!equals && currLen <= maxLen)
					return true;
			}

			// if source isn't crossable by Human then this path is not valid
			if (sourceSector.getSectorType() == SectorType.ALIEN)
				return false;

			// Get an iterator to retrieve a list of the neighbors of source
			listIterator = this.searchableGraph.neighborListOf(sourceSector)
					.iterator();
			while (listIterator.hasNext()) {
				// Call recursively checkAdiacency passing as source the
				// neighbors of the current source
				Sector toVisit = listIterator.next();
				if (!startingSector.equals(toVisit)) {

					if (checkSectorAdiacency(toVisit, targetSector, maxLen,
							currLen + 1, playerType, startingSector, equals))
						return true;
				}
			}
		}
		// If Alien
		else {
			// Returns false because the currLen of the path is equal to maxLen
			// but source is different from target
			if (sourceSector.equals(targetSector)
					&& sourceSector.getSectorType() != SectorType.HUMAN) {
				if (equals && currLen == maxLen)
					return true;
				if (!equals && currLen <= maxLen)
					return true;
			}
			// if source isn't crossable by Alien then this path is not valid
			if (sourceSector.getSectorType() == SectorType.HUMAN)
				return false;

			// Get an iterator to retrieve a list of the neighbors of source
			listIterator = this.searchableGraph.neighborListOf(sourceSector)
					.iterator();
			while (listIterator.hasNext()) {
				// Call recursively checkAdiacency passing as source the
				// neighbors of the current source
				Sector toVisit = listIterator.next();
				if (!startingSector.equals(toVisit)) {

					if (checkSectorAdiacency(toVisit, targetSector, maxLen,
							currLen + 1, playerType, startingSector, equals))
						return true;
				}
			}
		}
		return false;
	}

	/**
	 * Find the rescue sector in the map
	 * 
	 * @return A list of the rescue sector of the map
	 */
	private List<Sector> findEscapes() {
		List<Sector> toReturn = new ArrayList<Sector>();
		Set<Sector> sectors = this.graph.vertexSet();
		for (Sector s : sectors) {
			if (s.getSectorType() == SectorType.OPEN_RESCUE
					|| s.getSectorType() == SectorType.CLOSED_RESCUE) {
				toReturn.add(s);

			}
		}
		return toReturn;
	}

	/**
	 * @return True if there is still an escape for the human players
	 */
	public boolean existEscapes() {
		for (Sector s : escapes) {
			if (s.getSectorType() == SectorType.OPEN_RESCUE)
				return true;
		}
		return false;
	}
}
