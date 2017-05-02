package it.polimi.ingsw.cg_19;

import common.Coordinate;
import common.Sector;
import common.SectorType;
import org.jgrapht.UndirectedGraph;
import org.jgrapht.alg.NeighborIndex;
import org.jgrapht.graph.DefaultEdge;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Represents a generic map in the game.
 *
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
     * information about its table like representation and from its name. A
     * searchable version of the undirected graph given is created and
     * references to the map's alien sectors and human sectors are saved.
     *
     * @param graph                   The graph associated with the map.
     * @param startingHorizontalCoord Considering a table like representation of the map, the map's
     *                                starting horizontal coordinate.
     * @param startingVerticalCoord   Considering a table like representation of the map, the map's
     *                                starting vertical coordinate of the map
     * @param horizontalLength        Considering a table like representation of the map, the map's
     *                                number of columns
     * @param verticalLength          Considering a table like representation of the map, the map's
     *                                number of rows
     * @param name                    The map's name
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


    public String getName() {
        return name;
    }


    public int getStartingHorizontalCoord() {
        return startingHorizontalCoord;
    }


    public int getStartingVerticalCoord() {
        return startingVerticalCoord;
    }


    public int getVerticalLength() {
        return verticalLength;
    }


    public int getHorizontalLength() {
        return horizontalLength;
    }


    public UndirectedGraph<Sector, DefaultEdge> getGraph() {
        return this.graph;
    }


    public NeighborIndex<Sector, DefaultEdge> getSearchableGraph() {
        return searchableGraph;
    }


    public Sector getHumanSector() {
        return humanSector;
    }


    public Sector getAlienSector() {
        return alienSector;
    }


    public Sector getSectorByCoords(Coordinate coordinate) {
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
     * Checks if a given sector is reachable from another given sector within, or exactly with, a certain distance.
     *
     * @param sourceSector     The starting sector.
     * @param targetSector     The sector whose adjacency we want to check w.r.t a given starting sector.
     * @param maxDistance      The distance within which the given target sector must be reachable from the given source sector.
     * @param forceMaxMovement A flag that forces the algorythm to check sector reachable with exactly the given distance.
     * @return True if the reachability is verified with success, otherwise false
     */
    public boolean checkSectorAdiacency(Sector sourceSector, Sector targetSector, int maxDistance, boolean forceMaxMovement) {
        List<List<Sector>> visitedSectors = new ArrayList<>();
        List<Sector> neighbors = this.searchableGraph.neighborListOf(sourceSector);
        visitedSectors.add(neighbors);
        for (int i = 1; i < maxDistance; i++) {
            visitedSectors.add(new ArrayList<Sector>());
            for (Sector sector : visitedSectors.get(i - 1)) {
                neighbors = this.searchableGraph.neighborListOf(sector);
                for (Sector neighbor : neighbors) {
                    if (!neighbor.isHasBeenChecked()) {
                        visitedSectors.get(i).add(neighbor);
                    }

                }
            }
        }
        for (List<Sector> sectorList : visitedSectors) {
            for (Sector sector : sectorList) {
                sector.setHasBeenChecked(false);
            }
        }
        if (forceMaxMovement) {
            for (Sector sector : visitedSectors.get(maxDistance - 1)) {
                if (sector.equals(targetSector)) {
                    return true;
                }
            }
        } else {
            for (List<Sector> sectorList : visitedSectors) {

                for (Sector sector : sectorList) {
                    if (sector.equals(targetSector)) {
                        return true;
                    }
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
