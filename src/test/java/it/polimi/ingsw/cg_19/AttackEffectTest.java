package it.polimi.ingsw.cg_19;

import static org.junit.Assert.*;

import org.jgrapht.UndirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import org.junit.Test;
import common.AttackObjectCard;
import common.Coordinate;
import common.PSClientNotification;
import common.RRClientNotification;
import common.Sector;
import common.SectorType;
import effects.AttackObjCardEffect;

/**
 * Some tests for the AttackEffect class
 * 
 * @author Andrea Sessa
 * @author Giorgio Pea
 *
 */
public class AttackEffectTest {

	/**
	 * Test for the executeEffect method It simulates the use of an attack
	 * object card on a "small" game The game has two safe sector connected to
	 * each other
	 */
	@Test
	public void testExecuteEffect() {
		RRClientNotification n1 = new RRClientNotification();
		PSClientNotification n2 = new PSClientNotification();

		Sector source = new Sector(new Coordinate('A', 1), SectorType.SAFE);
		Sector target = new Sector(new Coordinate('A', 2), SectorType.SAFE);

		AttackObjectCard card = new AttackObjectCard(target);
		AttackObjCardEffect effect = new AttackObjCardEffect(card);

		Player player1 = new Player(PlayerType.HUMAN, "");

		// Both player located in source
		player1.setSector(source);
		source.addPlayer(player1);

		// Create a simple graph containing one sector s1
		UndirectedGraph<Sector, DefaultEdge> graph = new SimpleGraph<Sector, DefaultEdge>(
				DefaultEdge.class);
		graph.addVertex(source);
		graph.addVertex(target);
		graph.addEdge(source, target);

		GameMap map = new GameMap(graph, 0, 0, 0, 0, "");

		Game game = new Game(map);
		game.addPlayer(player1);

		assertTrue(effect.executeEffect(game, n1, n2));
		// Checks that the player has been correctly moved from its starting
		// sector
		assertTrue(source.getPlayers().isEmpty());
	}

	/**
	 * Test for the getObjectCard() method It checks that given an AttackEffect
	 * initialized with a given object card(card) the getObjectCard returns it
	 * correctly
	 */
	@Test
	public void testAttackEffectAttackObjectCard() {
		AttackObjectCard card = new AttackObjectCard(new Sector(new Coordinate(
				'A', 1), SectorType.SAFE));
		AttackObjCardEffect effect = new AttackObjCardEffect(card);
		assertEquals(card, effect.getObjectCard());
	}

}
