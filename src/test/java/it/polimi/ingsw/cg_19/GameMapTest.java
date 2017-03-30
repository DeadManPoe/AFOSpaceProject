package it.polimi.ingsw.cg_19;

import static org.junit.Assert.*;

import org.junit.Test;
import org.jgrapht.*;
import org.jgrapht.graph.*;

import common.Coordinate;
import common.Sector;
import common.SectorType;

/**
 * @author Andrea Sessa
 * @see Map Some tests for the map class
 */
public class GameMapTest {

	/**
	 * Some test for the getter in the class ->getStartingHorizontalCoord
	 * ->getStartingVerticalCoord ->getVerticalLength ->getHorizontalLength
	 */
	@Test
	public void testGetStartingHorizontalCoord() {
		// Create a simple graph containing one sector s1
		UndirectedGraph<Sector, DefaultEdge> graph_1 = new SimpleGraph<Sector, DefaultEdge>(
				DefaultEdge.class);
		Sector s1 = new Sector(new Coordinate('A', 1), SectorType.SAFE);
		graph_1.addVertex(s1);

		// Instantiates a new Map map starting from the graph defined above
		GameMap map = new GameMap(graph_1, 0, 0, 0, 0, "");
		assertEquals(0, map.getStartingHorizontalCoord());
		assertEquals(0, map.getStartingVerticalCoord());
		assertEquals(0, map.getVerticalLength());
		assertEquals(0, map.getHorizontalLength());
	}

	/**
	 * Tests if given correct coordinates it returns the correct sector, and if
	 * given wrong coordinates it return the wrong sector
	 */
	@Test
	public void testGetSectorByCoords() {

		// Create a simple graph containing one sector s1
		UndirectedGraph<Sector, DefaultEdge> graph_1 = new SimpleGraph<Sector, DefaultEdge>(
				DefaultEdge.class);
		Sector s1 = new Sector(new Coordinate('A', 1), SectorType.SAFE);
		graph_1.addVertex(s1);

		// Instantiates a new Map map starting from the graph defined above
		GameMap map = new GameMap(graph_1, 0, 0, 0, 0, "");

		// Checks the return value of getSectorByCoords()
		assertEquals(s1, map.getSectorByCoords(new Coordinate('A', 1)));
		assertNotEquals(s1, map.getSectorByCoords(new Coordinate('A', 2)));
	}

	/**
	 * Test if given a graph of four sectors (s1,s2,s3,s4) the checkAdiacency
	 * function returns the correct boolean value according to the reachness
	 * property of the sectors involved
	 */
	@Test
	public void testCheckAdiacency() {

		/*
		 * Create a starting graph containing four sectors s1,s2,s3,s4then links
		 * s1 and s2, s2 and s3, s3 and s4
		 */
		UndirectedGraph<Sector, DefaultEdge> graph = new SimpleGraph<Sector, DefaultEdge>(
				DefaultEdge.class);
		Sector s1 = new Sector(new Coordinate('A', 1), SectorType.SAFE);
		Sector s2 = new Sector(new Coordinate('A', 2), SectorType.ALIEN);
		Sector s3 = new Sector(new Coordinate('A', 3), SectorType.SAFE);
		Sector s4 = new Sector(new Coordinate('A', 4), SectorType.SAFE);
		Sector s5 = new Sector(new Coordinate('A', 5), SectorType.HUMAN);
		graph.addVertex(s1);
		graph.addVertex(s2);
		graph.addVertex(s3);
		graph.addVertex(s4);
		graph.addVertex(s5);
		graph.addEdge(s1, s2);
		graph.addEdge(s2, s3);
		graph.addEdge(s3, s4);
		graph.addEdge(s1, s5);

		// Create a new Map map starting from the graph defined above
		GameMap map = new GameMap(graph, 0, 0, 0, 0, "");

		assertTrue(map.checkSectorAdiacency(s1,s3,1,0,PlayerType.HUMAN,s1,true));
		// Checks the return value of checkAdiacency
		assertFalse(map.checkSectorAdiacency(s1, s2, 1, 0, PlayerType.HUMAN,
				s1, false));
		assertFalse(map.checkSectorAdiacency(s1, s3, 2, 0, PlayerType.HUMAN,
				s1, true));
		assertTrue(map.checkSectorAdiacency(s1, s2, 2, 0, PlayerType.ALIEN, s1,
				false));
		assertTrue(map.checkSectorAdiacency(s1, s2, 1, 0, PlayerType.ALIEN, s1,
				true));
		assertTrue(map.checkSectorAdiacency(s3, s4, 1, 0, PlayerType.HUMAN, s3,
				false));
		assertFalse(map.checkSectorAdiacency(s1, s5, 1, 0, PlayerType.ALIEN,
				s1, true));
	}

	/**
	 * Test for the existEscape method
	 */
	@Test
	public void testExistEscapes() {
		UndirectedGraph<Sector, DefaultEdge> graph = new SimpleGraph<Sector, DefaultEdge>(
				DefaultEdge.class);
		Sector s1 = new Sector(new Coordinate('A', 1), SectorType.OPEN_RESCUE);
		Sector s2 = new Sector(new Coordinate('A', 2), SectorType.ALIEN);
		Sector s3 = new Sector(new Coordinate('A', 3), SectorType.SAFE);
		Sector s4 = new Sector(new Coordinate('A', 4), SectorType.SAFE);
		Sector s5 = new Sector(new Coordinate('A', 5), SectorType.HUMAN);
		graph.addVertex(s1);
		graph.addVertex(s2);
		graph.addVertex(s3);
		graph.addVertex(s4);
		graph.addVertex(s5);
		graph.addEdge(s1, s2);
		graph.addEdge(s2, s3);
		graph.addEdge(s3, s4);
		graph.addEdge(s1, s5);
		// Create a new Map map starting from the graph defined above
		GameMap map = new GameMap(graph, 0, 0, 0, 0, "");
		assertEquals(true, map.existEscapes());
	}

}
