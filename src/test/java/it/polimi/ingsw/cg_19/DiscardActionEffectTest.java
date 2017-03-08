package it.polimi.ingsw.cg_19;

import static org.junit.Assert.*;

import org.jgrapht.UndirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import org.junit.Test;

import common.AdrenalineObjectCard;
import common.Coordinate;
import common.DiscardAction;
import common.PSClientNotification;
import common.RRClientNotification;
import common.Sector;
import common.SectorType;
import common.SuppressorObjectCard;
import effects.DiscardObjCardEffect;

/**
 * Some tests for the DiscardActionEffect class
 * 
 * @author Andrea Sessa
 * @author Giorgio Pea
 */
public class DiscardActionEffectTest {

	/**
	 * Test for the executeEffect method It simulates the action of discarding
	 * an object card on a "small" game The game has two safe sector connected
	 * to each other
	 */
	@Test
	public void testExecuteEffect() {
		RRClientNotification n1 = new RRClientNotification();
		PSClientNotification n2 = new PSClientNotification();

		DiscardAction action = new DiscardAction(new SuppressorObjectCard());
		DiscardObjCardEffect effect = new DiscardObjCardEffect(action);

		Sector source = new Sector(new Coordinate('A', 1), SectorType.SAFE);
		Sector target = new Sector(new Coordinate('A', 2), SectorType.SAFE);

		Player player1 = new Player(PlayerType.HUMAN, "Andrea");

		/*
		 * Alien in source Human in target
		 */
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
		game.addPlayer(player1);

		assertTrue(effect.executeEffect(game, n1, n2));
	}

	/**
	 * Test for the getAction() method It checks that given an
	 * DiscardActionEffect initialized with a DiscardAction(action) the
	 * getAction returns it correctly
	 */
	@Test
	public void testDiscardActionEffect() {
		DiscardAction action = new DiscardAction(new AdrenalineObjectCard());
		DiscardObjCardEffect effect = new DiscardObjCardEffect(action);
		assertEquals(action, effect.getAction());
	}

}
