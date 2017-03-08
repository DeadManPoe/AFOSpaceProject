package it.polimi.ingsw.cg_19;

import static org.junit.Assert.*;

import org.jgrapht.UndirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import org.junit.Test;
import common.*;

/**
 * Some tests for the AlienTurnTest class
 * 
 * @author Andrea Sessa
 * @author Giorgio Pea
 */
public class AlienTurnTest {

	/**
	 * Test for the constructor It checks if the alien turn class is initiated
	 * correctly
	 */
	@Test
	public void testAlienTurn() {
		// Create a simple graph containing one sector s1
		UndirectedGraph<Sector, DefaultEdge> graph_1 = new SimpleGraph<Sector, DefaultEdge>(
				DefaultEdge.class);
		Sector s1 = new Sector(new Coordinate('A', 1), SectorType.SAFE);
		graph_1.addVertex(s1);

		// Instantiates a new Map map starting from the graph defined above
		GameMap map = new GameMap(graph_1, 0, 0, 0, 0, "");

		Game game = new Game(map);

		Turn turn = new HumanTurn(game);

		assertEquals(game, turn.getGame());
		assertEquals(MoveAction.class, turn.getInitialActions().get(0));
	}

	/**
	 * Test for the getNextAction method It checks that given an action and in a
	 * particular game situation the next action is calculated correctly
	 */
	@Test
	public void testGetNextActions() {
		// Create a simple graph containing one sector s1
		UndirectedGraph<Sector, DefaultEdge> graph_1 = new SimpleGraph<Sector, DefaultEdge>(
				DefaultEdge.class);
		Sector s1 = new Sector(new Coordinate('A', 1), SectorType.SAFE);
		graph_1.addVertex(s1);

		// Instantiates a new Map map starting from the graph defined above
		GameMap map = new GameMap(graph_1, 0, 0, 0, 0, "");

		Game game = new Game(map);

		Turn turn = new AlienTurn(game);

		Sector safeSector = new Sector(new Coordinate('A', 1), SectorType.SAFE);
		Sector dangerousSector = new Sector(new Coordinate('B', 1),
				SectorType.DANGEROUS);

		SectorCard sectorCardNoObj = new LocalNoiseSectorCard(false);
		SectorCard sectorCardObj = new LocalNoiseSectorCard(true);

		// A player whit less than 3 object card
		Player playerNoFull = new Player(PlayerType.ALIEN, "");

		// A player that has already 3 object and has drawn the fourth
		Player playerFull = new Player(PlayerType.ALIEN, "");

		playerFull.getPrivateDeck().addCard(new AdrenalineObjectCard());
		playerFull.getPrivateDeck().addCard(new AdrenalineObjectCard());
		playerFull.getPrivateDeck().addCard(new AdrenalineObjectCard());
		playerFull.getPrivateDeck().addCard(new AdrenalineObjectCard());

		game.addPlayer(playerNoFull);

		assertTrue(turn.getNextActions(new MoveAction(dangerousSector))
				.contains(DrawSectorCardAction.class));
		assertTrue(turn.getNextActions(new MoveAction(safeSector)).contains(
				EndTurnAction.class));

		assertTrue(turn.getNextActions(new DrawSectorCardAction()).contains(
				UseSectorCardAction.class));

		assertTrue(turn
				.getNextActions(new UseSectorCardAction(sectorCardNoObj))
				.contains(EndTurnAction.class));

		assertTrue(turn.getNextActions(new UseSectorCardAction(sectorCardObj))
				.contains(EndTurnAction.class));

		// = 3 Object Card, Test the situation in which the player has already
		// three object card
		game.addPlayer(playerFull);
		game.shiftCurrentplayer();

		assertTrue(turn
				.getNextActions(new UseSectorCardAction(sectorCardNoObj))
				.contains(EndTurnAction.class));

		assertTrue(turn.getNextActions(new UseSectorCardAction(sectorCardObj))
				.contains(DiscardAction.class));

		assertTrue(turn.getNextActions(new DiscardAction(null)).contains(
				EndTurnAction.class));

		assertTrue(turn.getNextActions(new MoveAttackAction(safeSector))
				.contains(EndTurnAction.class));
	}
}