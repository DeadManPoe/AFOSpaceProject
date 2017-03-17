/*
package it.polimi.ingsw.cg_19;

import static org.junit.Assert.*;
import org.jgrapht.UndirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import org.junit.Test;
import common.Coordinate;
import common.LightsObjectCard;
import common.PSClientNotification;
import common.RRClientNotification;
import common.Sector;
import common.SectorType;
import effects.LightObjectCardEffect;

*/
/**
 * Some tests for the LightEffect class
 * 
 * @author Andrea Sessa
 * @author Giorgio Pea
 *//*

public class LightsEffectTest {

	*/
/**
	 * Test for the executeEffect method It checks that in a "small" game with
	 * two safe secotr connected to each other the target sector is contained in
	 * the group of lighted sectors
	 *//*

	@Test
	public void testExecuteEffect() {
		RRClientNotification n1 = new RRClientNotification();
		PSClientNotification n2 = new PSClientNotification();

		Sector source = new Sector(new Coordinate('A', 1), SectorType.SAFE);
		Sector target = new Sector(new Coordinate('A', 2), SectorType.SAFE);

		Player player1 = new Player(PlayerType.HUMAN, "Andrea");

		*/
/*
		 * Alien in source Human in target
		 *//*

		player1.setSector(target);
		target.addPlayer(player1);

		// Create a simple graph containing one sector s1
		UndirectedGraph<Sector, DefaultEdge> graph = new SimpleGraph<Sector, DefaultEdge>(
				DefaultEdge.class);
		graph.addVertex(source);
		graph.addVertex(target);
		graph.addEdge(source, target);

		// Creates a new map from graph
		GameMap map = new GameMap(graph, 0, 0, 0, 0, "");

		Game game = new Game(map);

		LightObjectCardEffect effect = new LightObjectCardEffect(
				new LightsObjectCard(source));
		assertTrue(effect.executeEffect(game, n1, n2));
		assertTrue(n1.getLightedSectors().contains(target));
	}

	*/
/**
	 * Test for the getObjectCard method It checks that given an LightEffect
	 * initialized with a not null action the getObjectCard method returns it
	 * correctly
	 *//*

	@Test
	public void testLightsEffectLight() {
		LightsObjectCard card = new LightsObjectCard(new Sector(new Coordinate(
				'A', 1), SectorType.SAFE));
		LightObjectCardEffect effect = new LightObjectCardEffect(card);
		assertEquals(card, effect.getObjectCard());
	}

}
*/
