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
import common.SilenceSectorCard;
import common.UseSectorCardAction;
import effects.UseSectorCardEffect;

/**
 * Some tests for the UseSectorCardActionEffect
 * 
 * @author Andrea Sessa
 * @author Giorgio Pea
 */
public class UseSectorCardActionEffectTest {

	/**
	 * Checks that the executeEffect method is executed correctly
	 */
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

		SilenceSectorCard card = new SilenceSectorCard();
		UseSectorCardAction action = new UseSectorCardAction(card);
		UseSectorCardEffect effect = new UseSectorCardEffect(action);
		assertTrue(effect.executeEffect(game, clientNotification,
				psNotification));
	}

	/**
	 * Checks that the getAction returns the action passed to the constructor
	 */
	@Test
	public void testUseSectorCardActionEffect_1() {
		UseSectorCardEffect effect = new UseSectorCardEffect();
		assertEquals(null, effect.getAction());
	}

	/**
	 * Checks that the getAction returns the action passed to the constructor
	 */
	@Test
	public void testUseSectorCardActionEffect_2() {
		UseSectorCardAction action = new UseSectorCardAction(null);
		UseSectorCardEffect effect = new UseSectorCardEffect(action);
		assertEquals(action, effect.getAction());
	}

}
