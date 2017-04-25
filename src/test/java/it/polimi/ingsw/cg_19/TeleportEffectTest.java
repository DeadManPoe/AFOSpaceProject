/*
package it.polimi.ingsw.cg_19;

import static org.junit.Assert.*;
import org.jgrapht.UndirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import org.junit.Test;
import common.Coordinate;
import common.PSClientNotification;
import common.RRClientNotification;
import common.Sector;
import common.SectorType;
import common.TeleportObjectCard;
import effects.TeleportObjCardEffect;

*/
/**
 * Some tests for the teleport effect associated with the teleport object card
 * 
 * @author Andrea Sessa
 * @author Giorgio Pea
 * @see TeleportObjCardEffect
 *//*

public class TeleportEffectTest {

	*/
/**
	 * Checks that the player is correctly moved to its starting sector
	 *//*

	@Test
	public void testExecuteEffect() {
		RRClientNotification stubNotification = new RRClientNotification();
		PSClientNotification psNotification = new PSClientNotification();

		Sector s1 = new Sector(new Coordinate('A', 1), SectorType.HUMAN);
		Sector s2 = new Sector(new Coordinate('A', 2), SectorType.SAFE);
		Sector s3 = new Sector(new Coordinate('A', 3), SectorType.ALIEN);

		Player player1 = new Player(PlayerType.HUMAN, "");

		// Both player located in s2
		player1.setSector(s2);
		s2.addPlayer(player1);

		// Create a simple graph containing three sectors s1<->s2<->s3
		UndirectedGraph<Sector, DefaultEdge> graph = new SimpleGraph<Sector, DefaultEdge>(
				DefaultEdge.class);
		graph.addVertex(s1);
		graph.addVertex(s2);
		graph.addVertex(s3);
		graph.addEdge(s1, s2);
		graph.addEdge(s2, s3);

		// Creates a new map from graph
		GameMap map = new GameMap(graph, 0, 0, 0, 0, "");

		Game game = new Game(map);

		// Creates the TeleportEffect
		TeleportObjCardEffect e1 = new TeleportObjCardEffect(
				new TeleportObjectCard());

		game.addPlayer(player1);
		// Executes the effect
		e1.executeEffect(game, stubNotification, psNotification);

		assertTrue(s2.getPlayers().isEmpty());
		assertFalse(s1.getPlayers().isEmpty());
		assertTrue(s1.getPlayers().contains(player1));
	}

}
*/
