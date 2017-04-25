/*
package it.polimi.ingsw.cg_19;

import static org.junit.Assert.*;

import org.jgrapht.UndirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import org.junit.Test;

import common.Action;
import common.Coordinate;
import common.DrawObjectCardAction;
import common.Sector;
import common.SectorType;

*/
/**
 * Test for the Game class Not all method are tested because many interact with
 * RMI/Socket
 * 
 * @author Andrea Sessa
 * @author Giorgio Pea
 *//*

public class GameTest {

	*/
/**
	 * Test for the constructor
	 *//*

	@Test
	public void testGame() {
		Game game1 = new Game("GALILEI");
		Game game2 = new Game("FERMI");
		Game game3 = new Game("GALVANI");

		assertEquals(0, game1.getTurnNumber());
		assertEquals(0, game2.getTurnNumber());
		assertEquals(0, game3.getTurnNumber());
	}

	*/
/**
	 * Test for the the method that assign a type(ALIEN/HUMAN) to the player
	 *//*

	@Test
	public void testAssignTypeToPlayer() {
		Game game = new Game("FERMI");
		assertEquals(PlayerType.ALIEN, game.assignTypeToPlayer(1));
		assertEquals(PlayerType.HUMAN, game.assignTypeToPlayer(2));
	}

	*/
/**
	 * Checks that with no player the fromPlayerToToken method returns null
	 *//*

	@Test
	public void testfromPlayerToToken() {
		Game game = new Game("FERMI");
		assertNull(game.fromPlayerToToken(null));
	}

	*/
/**
	 * First test for the checkWinConditions method
	 *//*

	@Test
	public void testcheckWinConditions_1() {
		UndirectedGraph<Sector, DefaultEdge> graph = new SimpleGraph<Sector, DefaultEdge>(
				DefaultEdge.class);
		Sector s1 = new Sector(new Coordinate('A', 1), SectorType.SAFE);
		Sector s2 = new Sector(new Coordinate('A', 2), SectorType.ALIEN);
		Sector s3 = new Sector(new Coordinate('A', 3), SectorType.SAFE);
		Sector s4 = new Sector(new Coordinate('A', 4), SectorType.SAFE);
		Sector s5 = new Sector(new Coordinate('A', 5), SectorType.OPEN_RESCUE);
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

		Game game = new Game(map);

		Player p1 = new Player(PlayerType.HUMAN, "Andrea");
		p1.setPlayerState(PlayerState.ESCAPED);
		game.addPlayer(p1);
		// Human wins because all human player are escaped
		assertTrue(game.checkWinConditions(PlayerType.HUMAN));
		// Alien loose because all human player are escaped
		assertFalse(game.checkWinConditions(PlayerType.ALIEN));
		p1.setPlayerState(PlayerState.DEAD);
		// Alien win because all human player are dead
		assertTrue(game.checkWinConditions(PlayerType.ALIEN));
		game.setTurnNumber(39);
		p1.setPlayerState(PlayerState.ALIVE);
		// Alien wins beacauese the turn is equal to 39
		assertTrue(game.checkWinConditions(PlayerType.ALIEN));
	}

	*/
/**
	 * Second test for the checkWinConditions method
	 *//*

	@Test
	public void testcheckWinConditions_2() {
		*/
/*
		 * Create a starting graph containing four sectors s1,s2,s3,s4then links
		 * s1 and s2, s2 and s3, s3 and s4
		 *//*

		UndirectedGraph<Sector, DefaultEdge> graph = new SimpleGraph<Sector, DefaultEdge>(
				DefaultEdge.class);
		Sector s1 = new Sector(new Coordinate('A', 1), SectorType.SAFE);
		Sector s2 = new Sector(new Coordinate('A', 2), SectorType.ALIEN);
		Sector s3 = new Sector(new Coordinate('A', 3), SectorType.SAFE);
		Sector s4 = new Sector(new Coordinate('A', 4), SectorType.SAFE);
		Sector s5 = new Sector(new Coordinate('A', 5), SectorType.OPEN_RESCUE);
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

		Game game = new Game(map);
		Player p1 = new Player(PlayerType.ALIEN, "Andrea");
		Player p2 = new Player(PlayerType.HUMAN, "Giorgio");
		p2.setPlayerState(PlayerState.DEAD);
		game.addPlayer(p1);
		game.addPlayer(p2);

		// Alien wins because all humans are dead
		assertTrue(game.checkWinConditions(PlayerType.ALIEN));
		// Human loose because all humans are dead
		assertFalse(game.checkWinConditions(PlayerType.HUMAN));
		game.setTurnNumber(39);
		// Alien wins because the turn number = 39
		assertTrue(game.checkWinConditions(PlayerType.ALIEN));
	}

	*/
/**
	 * Checks that the getCurrentPlayer method returns the correct current
	 * player also after changing it
	 *//*

	@Test
	public void testGetCurrentPlayer() {
		// Create a simple graph containing one sector s1
		UndirectedGraph<Sector, DefaultEdge> graph_1 = new SimpleGraph<Sector, DefaultEdge>(
				DefaultEdge.class);
		Sector s1 = new Sector(new Coordinate('A', 1), SectorType.SAFE);
		graph_1.addVertex(s1);

		// Instantiates a new Map map starting from the graph defined above
		GameMap map = new GameMap(graph_1, 0, 0, 0, 0, "");

		Game game = new Game(map);
		Player player = new Player(PlayerType.HUMAN, "");
		game.addPlayer(player);

		assertEquals(player, game.getCurrentPlayer());
	}

	*/
/**
	 * Checks that the turn number is updated correctly
	 *//*

	@Test
	public void testGetSetTurnNumber() {
		// Create a simple graph containing one sector s1
		UndirectedGraph<Sector, DefaultEdge> graph_1 = new SimpleGraph<Sector, DefaultEdge>(
				DefaultEdge.class);
		Sector s1 = new Sector(new Coordinate('A', 1), SectorType.SAFE);
		graph_1.addVertex(s1);

		// Instantiates a new Map map starting from the graph defined above
		GameMap map = new GameMap(graph_1, 0, 0, 0, 0, "");

		Game game = new Game(map);
		game.setTurnNumber(1);
		assertEquals(1, game.getTurnNumber());
	}

	*/
/**
	 * Checks that the lastAction field is updated correctly
	 *//*

	@Test
	public void testGetSetLastAction() {
		// Create a simple graph containing one sector s1
		UndirectedGraph<Sector, DefaultEdge> graph_1 = new SimpleGraph<Sector, DefaultEdge>(
				DefaultEdge.class);
		Sector s1 = new Sector(new Coordinate('A', 1), SectorType.SAFE);
		graph_1.addVertex(s1);

		// Instantiates a new Map map starting from the graph defined above
		GameMap map = new GameMap(graph_1, 0, 0, 0, 0, "");

		Game game = new Game(map);
		Action drawActionFromObject = new DrawObjectCardAction();
		game.setLastAction(drawActionFromObject);
		assertEquals(drawActionFromObject, game.getLastAction());
	}

	*/
/**
	 * Checks that the turn field is updated correctly
	 *//*

	@Test
	public void testSetGetTurn() {
		// Create a simple graph containing one sector s1
		UndirectedGraph<Sector, DefaultEdge> graph_1 = new SimpleGraph<Sector, DefaultEdge>(
				DefaultEdge.class);
		Sector s1 = new Sector(new Coordinate('A', 1), SectorType.SAFE);
		graph_1.addVertex(s1);

		// Instantiates a new Map map starting from the graph defined above
		GameMap map = new GameMap(graph_1, 0, 0, 0, 0, "");

		Game game = new Game(map);
		Turn turn = new HumanTurn(game);
		game.setTurn(turn);
		assertEquals(turn, game.getTurn());
	}
}
*/
