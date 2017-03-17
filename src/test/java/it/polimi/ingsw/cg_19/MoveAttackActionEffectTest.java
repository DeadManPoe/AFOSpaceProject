/*
package it.polimi.ingsw.cg_19;

import static org.junit.Assert.*;

import org.jgrapht.UndirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import org.junit.Test;
import common.Action;
import common.Coordinate;
import common.MoveAttackAction;
import common.PSClientNotification;
import common.RRClientNotification;
import common.Sector;
import common.SectorType;
import effects.ActionEffect;
import effects.GameActionMapper;
import effects.MoveAttackActionEffect;

*/
/**
 * Some tests for the MoveAttackActionEffect class
 * 
 * @author Andrea Sessa
 * @author Giorgio Pea
 *//*

public class MoveAttackActionEffectTest {

	*/
/**
	 * Test for executeEffect with target sector a safe sector
	 * 
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 *//*

	@Test
	public void testExecuteEffect() throws InstantiationException,
			IllegalAccessException {
		GameActionMapper mapper = new GameActionMapper();

		RRClientNotification clientNotification = new RRClientNotification();
		PSClientNotification psNotification = new PSClientNotification();

		Sector source = new Sector(new Coordinate('A', 1), SectorType.SAFE);
		Sector target = new Sector(new Coordinate('A', 2), SectorType.SAFE);

		Player player1 = new Player(PlayerType.HUMAN, "");
		Player player2 = new Player(PlayerType.ALIEN, "");

		*/
/*
		 * Alien in source Human in target
		 *//*

		player1.setSector(target);
		target.addPlayer(player1);
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
		ActionEffect move1 = mapper.getEffect(new MoveAttackAction(target));
		ActionEffect move2 = mapper.getEffect(new MoveAttackAction(target));

		// Executes the two action
		game.shiftCurrentplayer();
		assertTrue(move1
				.executeEffect(game, clientNotification, psNotification));
		assertNull(player1.getSector());
		assertTrue(source.getPlayers().isEmpty());
		game.shiftCurrentplayer();
		assertFalse(move2.executeEffect(game, clientNotification,
				psNotification));
	}

	*/
/**
	 * Test the executeEffect with a target sector as a dangerous sector
	 * 
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 *//*

	@Test
	public void testExecuteEffect_2() throws InstantiationException,
			IllegalAccessException {
		GameActionMapper mapper = new GameActionMapper();

		RRClientNotification clientNotification = new RRClientNotification();
		PSClientNotification psNotification = new PSClientNotification();

		Sector source = new Sector(new Coordinate('A', 1), SectorType.SAFE);
		Sector target = new Sector(new Coordinate('A', 2), SectorType.DANGEROUS);

		Player player1 = new Player(PlayerType.HUMAN, "");
		Player player2 = new Player(PlayerType.ALIEN, "");

		*/
/*
		 * Alien in source Human in target
		 *//*

		player1.setSector(target);
		target.addPlayer(player1);
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
		ActionEffect move1 = mapper.getEffect(new MoveAttackAction(target));
		ActionEffect move2 = mapper.getEffect(new MoveAttackAction(target));

		// Executes the two action
		game.shiftCurrentplayer();
		assertTrue(move1
				.executeEffect(game, clientNotification, psNotification));
		assertNull(player1.getSector());
		assertTrue(source.getPlayers().isEmpty());
		game.shiftCurrentplayer();
		assertFalse(move2.executeEffect(game, clientNotification,
				psNotification));
	}

	*/
/**
	 * Test the executeEffect with the target sector as a Rescue sector
	 * 
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 *//*

	@Test
	public void testExecuteEffect_3() throws InstantiationException,
			IllegalAccessException {
		GameActionMapper mapper = new GameActionMapper();

		RRClientNotification clientNotification = new RRClientNotification();
		PSClientNotification psNotification = new PSClientNotification();

		Sector source = new Sector(new Coordinate('A', 1), SectorType.SAFE);
		Sector target = new Sector(new Coordinate('A', 2),
				SectorType.OPEN_RESCUE);

		Player player1 = new Player(PlayerType.HUMAN, "");
		Player player2 = new Player(PlayerType.ALIEN, "");

		*/
/*
		 * Alien in source Human in target
		 *//*

		player1.setSector(target);
		target.addPlayer(player1);
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
		ActionEffect move1 = mapper.getEffect(new MoveAttackAction(target));
		ActionEffect move2 = mapper.getEffect(new MoveAttackAction(target));

		// Executes the two action
		game.shiftCurrentplayer();
		assertTrue(move1
				.executeEffect(game, clientNotification, psNotification));
		assertNull(player1.getSector());
		assertTrue(source.getPlayers().isEmpty());
		game.shiftCurrentplayer();
		assertFalse(move2.executeEffect(game, clientNotification,
				psNotification));
	}

	*/
/**
	 * Checks that the setAction field updates the action field correctly
	 *//*

	@Test
	public void testSetAction() {
		Action action = new MoveAttackAction(null);
		MoveAttackActionEffect m = new MoveAttackActionEffect(null);
		m.setAction(action);
		assertEquals(action, m.getAction());
	}

}
*/
