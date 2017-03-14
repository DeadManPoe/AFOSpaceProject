package it.polimi.ingsw.cg_19;

import static org.junit.Assert.*;
import org.jgrapht.UndirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import org.junit.Test;
import common.Coordinate;
import common.MoveAction;
import common.PSClientNotification;
import common.RRClientNotification;
import common.Sector;
import common.SectorType;
import effects.ActionEffect;
import effects.GameActionMapper;

/**
 * Some test for the MoveActionTest class
 * 
 * @author Andrea Sessa
 * @author Giorgio Pea
 *
 */
public class MoveActionEffectTest {

	/**
	 * Test the executeEffect method with a safe sector as target
	 * 
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	@Test
	public void executeActionTest() throws InstantiationException,
			IllegalAccessException {
		GameActionMapper mapper = new GameActionMapper();

		RRClientNotification stubNotification = new RRClientNotification();
		PSClientNotification psNotification = new PSClientNotification();

		Sector source = new Sector(new Coordinate('A', 1), SectorType.SAFE);
		Sector target = new Sector(new Coordinate('A', 2), SectorType.SAFE);

		Player player1 = new Player(PlayerType.HUMAN, "");
		Player player2 = new Player(PlayerType.ALIEN, "");

		// Both player located in source
		player1.setSector(source);
		source.addPlayer(player1);
		player2.setSector(source);
		source.addPlayer(player2);

		// Create a simple graph containing one sector s1
		UndirectedGraph<Sector, DefaultEdge> graph = new SimpleGraph<Sector, DefaultEdge>(
				DefaultEdge.class);
		graph.addVertex(source);
		graph.addVertex(target);
		graph.addEdge(source, target);

		// Creates a new map from graph
		GameMap map = new GameMap(graph, 0, 0, 0, 0, "");

		Game game = new Game(map);
		game.addPlayer(player1);
		game.addPlayer(player2);

		// Creates the move action
		ActionEffect move1 = mapper.getEffect(new MoveAction(target));
		ActionEffect move2 = mapper.getEffect(new MoveAction(target));

		// Executes the two action
		move1.executeEffect(game, stubNotification, psNotification);
		game.shiftCurrentplayer();
		move2.executeEffect(game, stubNotification, psNotification);

		assertEquals(target, player1.getSector());
		assertEquals(target, player2.getSector());

	}

	/**
	 * Test the executeEffect method with a dangerous sector as target
	 * 
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	@Test
	public void executeActionTest_2() throws InstantiationException,
			IllegalAccessException {
		GameActionMapper mapper = new GameActionMapper();

		RRClientNotification stubNotification = new RRClientNotification();
		PSClientNotification psNotification = new PSClientNotification();

		Sector source = new Sector(new Coordinate('A', 1), SectorType.SAFE);
		Sector target = new Sector(new Coordinate('A', 2), SectorType.DANGEROUS);

		Player player1 = new Player(PlayerType.HUMAN, "");
		Player player2 = new Player(PlayerType.ALIEN, "");

		// Both player located in source
		player1.setSector(source);
		source.addPlayer(player1);
		player2.setSector(source);
		source.addPlayer(player2);

		// Create a simple graph containing one sector s1
		UndirectedGraph<Sector, DefaultEdge> graph = new SimpleGraph<Sector, DefaultEdge>(
				DefaultEdge.class);
		graph.addVertex(source);
		graph.addVertex(target);
		graph.addEdge(source, target);

		// Creates a new map from graph
		GameMap map = new GameMap(graph, 0, 0, 0, 0, "");

		Game game = new Game(map);
		game.addPlayer(player1);
		game.addPlayer(player2);

		// Creates the move action
		ActionEffect move1 = mapper.getEffect(new MoveAction(target));
		ActionEffect move2 = mapper.getEffect(new MoveAction(target));

		// Executes the two action
		move1.executeEffect(game, stubNotification, psNotification);
		game.shiftCurrentplayer();
		move2.executeEffect(game, stubNotification, psNotification);

		assertEquals(target, player1.getSector());
		assertEquals(target, player2.getSector());

	}

	/**
	 * Test the executeEffect method with a rescue sector as target
	 * 
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	@Test
	public void executeActionTest_3() throws InstantiationException,
			IllegalAccessException {
		GameActionMapper mapper = new GameActionMapper();

		RRClientNotification stubNotification = new RRClientNotification();
		PSClientNotification psNotification = new PSClientNotification();

		Sector source = new Sector(new Coordinate('A', 1), SectorType.SAFE);
		Sector target = new Sector(new Coordinate('A', 2), SectorType.OPEN_RESCUE);

		Player player1 = new Player(PlayerType.HUMAN, "");
		Player player2 = new Player(PlayerType.ALIEN, "");

		// Both player located in source
		player1.setSector(source);
		source.addPlayer(player1);
		player2.setSector(source);
		source.addPlayer(player2);

		// Create a simple graph containing one sector s1
		UndirectedGraph<Sector, DefaultEdge> graph = new SimpleGraph<Sector, DefaultEdge>(
				DefaultEdge.class);
		graph.addVertex(source);
		graph.addVertex(target);
		graph.addEdge(source, target);

		// Creates a new map from graph
		GameMap map = new GameMap(graph, 0, 0, 0, 0, "");

		Game game = new Game(map);
		game.addPlayer(player1);
		game.addPlayer(player2);

		// Creates the move action
		ActionEffect move1 = mapper.getEffect(new MoveAction(target));
		ActionEffect move2 = mapper.getEffect(new MoveAction(target));

		// Executes the two action
		move1.executeEffect(game, stubNotification, psNotification);
		game.shiftCurrentplayer();
		move2.executeEffect(game, stubNotification, psNotification);

		assertEquals(target, player1.getSector());
		assertNotEquals(target, player2.getSector());

	}

}
