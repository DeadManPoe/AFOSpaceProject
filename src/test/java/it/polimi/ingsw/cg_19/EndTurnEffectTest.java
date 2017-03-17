/*
package it.polimi.ingsw.cg_19;

import static org.junit.Assert.*;

import org.jgrapht.UndirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import org.junit.Test;

import common.Coordinate;
import common.EndTurnAction;
import common.PSClientNotification;
import common.RRClientNotification;
import common.Sector;
import common.SectorType;
import effects.EndTurnEffect;

*/
/**
 * Some tests for the EndTurnEffect
 * 
 * @author Andrea Sessa
 * @author Giorgio Pea
 *//*

public class EndTurnEffectTest {

	*/
/**
	 * Test for the executeEffect test It checks that in "small" game with two
	 * safe sector connected to each other the effect of the end turn is
	 * executed correctly
	 *//*

	@Test
	public void testExecuteEffect() {
		RRClientNotification clientNotification = new RRClientNotification();
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

		EndTurnAction action = new EndTurnAction();
		EndTurnEffect effect = new EndTurnEffect(action);

		assertTrue(effect.executeEffect(game, clientNotification,
				psNotification));
	}

	*/
/**
	 * Test for the getAction method It checks that given an EndTurnEffect
	 * initialized with a null action, the getAction method returns null
	 *//*

	@Test
	public void testEndTurnEffect_1() {
		EndTurnEffect effect = new EndTurnEffect();
		assertEquals(null, effect.getAction());
	}

	*/
/**
	 * Test for the getAction method It checks that given an EndTurnEffect
	 * initialized with a not null action, the getAction method returns it
	 * correctly
	 *//*

	@Test
	public void testEndTurnEffect_2() {
		EndTurnAction action = new EndTurnAction();
		EndTurnEffect effect = new EndTurnEffect(action);
		assertEquals(action, effect.getAction());
	}

}
*/
