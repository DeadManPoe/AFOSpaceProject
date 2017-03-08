package it.polimi.ingsw.cg_19;

import static org.junit.Assert.*;

import org.jgrapht.UndirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import org.junit.Test;

import common.AdrenalineObjectCard;
import common.Coordinate;
import common.PSClientNotification;
import common.RRClientNotification;
import common.Sector;
import common.SectorType;
import common.SuppressorObjectCard;
import common.UseObjAction;
import effects.UseObjCardEffect;

/**
 * Some tests for the UseObjActionEffect class
 * 
 * @author Andrea Sessa
 * @author Giorgio Pea
 */
public class UseObjActionEffectTest {

	/**
	 * Checks that the executeEffect method is executed correctly
	 */
	@Test
	public void testExecuteEffect() {
		RRClientNotification n1 = new RRClientNotification();
		PSClientNotification n2 = new PSClientNotification();

		UseObjAction action = new UseObjAction(new SuppressorObjectCard());
		UseObjCardEffect effect = new UseObjCardEffect(action);

		Sector source = new Sector(new Coordinate('A', 1), SectorType.SAFE);
		Sector target = new Sector(new Coordinate('A', 2), SectorType.SAFE);

		Player player = new Player(PlayerType.HUMAN, "");

		// Both player located in source
		player.setSector(source);
		source.addPlayer(player);

		// Create a simple graph containing one sector s1
		UndirectedGraph<Sector, DefaultEdge> graph = new SimpleGraph<Sector, DefaultEdge>(
				DefaultEdge.class);
		graph.addVertex(source);
		graph.addVertex(target);
		graph.addEdge(source, target);

		// Creates a new map from graph
		GameMap map = new GameMap(graph, 0, 0, 0, 0, "");

		Game game = new Game(map);
		game.addPlayer(player);

		assertTrue(effect.executeEffect(game, n1, n2));
		player.setHasMoved(true);
		assertFalse(effect.executeEffect(game, n1, n2));
	}

	/**
	 * Tests that the getAction returns the action passed to the constructor
	 */
	@Test
	public void testUseObjActionEffect() {
		UseObjAction action = new UseObjAction(new AdrenalineObjectCard());
		UseObjCardEffect effect = new UseObjCardEffect(action);
		assertEquals(action, effect.getAction());
	}
}
