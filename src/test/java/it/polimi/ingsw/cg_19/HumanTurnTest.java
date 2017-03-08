package it.polimi.ingsw.cg_19;

import static org.junit.Assert.*;

import org.jgrapht.UndirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import org.junit.Test;

import common.*;

/**
 * Some tests for the HumanTurn class
 * 
 * @author Andrea Sessa
 * @author Giorgio Pea
 */
public class HumanTurnTest {

	/**
	 * Test for the constructor of HumanTurn and the getInitialAction It checks
	 * that getInitialAction returns MoveAction and UseObjAction(the initial
	 * moved for a human player) and also checks that the getGame returns the
	 * same reference of the game passed to the constructor
	 */
	@Test
	public void testHumanTurn() {
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
		// At start a human player can only move or use an existing object
		assertEquals(MoveAction.class, turn.getInitialActions().get(0));
		assertEquals(UseObjAction.class, turn.getInitialActions().get(1));

	}

	/**
	 * Test for the getNextAction method It checks that given an action and a
	 * particular game situation it always returns the correct next action for
	 * the human player The test are made in two main different situations the
	 * first with a player with less than 3 object card and the second with a
	 * player that has collected 3 object card and has drawn the fourth
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

		Turn turn = new HumanTurn(game);

		Sector safeSector = new Sector(new Coordinate('A', 1), SectorType.SAFE);
		Sector dangerousSector = new Sector(new Coordinate('B', 1),
				SectorType.DANGEROUS);
		Sector rescueSector = new Sector(new Coordinate('C', 1),
				SectorType.OPEN_RESCUE);

		SectorCard sectorCardNoObj = new LocalNoiseSectorCard(false);
		SectorCard sectorCardObj = new LocalNoiseSectorCard(true);

		// < 3 Object card
		Player playerNoFull = new Player(PlayerType.HUMAN, null);

		// = 3 Object Card
		Player playerFull = new Player(PlayerType.HUMAN, null);

		playerFull.getPrivateDeck().addCard(new AdrenalineObjectCard());
		playerFull.getPrivateDeck().addCard(new AdrenalineObjectCard());
		playerFull.getPrivateDeck().addCard(new AdrenalineObjectCard());
		playerFull.getPrivateDeck().addCard(new AdrenalineObjectCard());

		// Test for a player with less than three cards in its private deck
		game.addPlayer(playerNoFull);

		assertTrue(turn.getNextActions(new MoveAction(dangerousSector))
				.contains(DrawSectorCardAction.class));
		assertTrue(turn.getNextActions(new MoveAction(rescueSector)).contains(
				DrawRescueCardAction.class));
		assertTrue(turn.getNextActions(new MoveAction(safeSector)).contains(
				UseObjAction.class));
		assertTrue(turn.getNextActions(new MoveAction(safeSector)).contains(
				EndTurnAction.class));
		assertTrue(turn.getNextActions(new DrawSectorCardAction()).contains(
				UseSectorCardAction.class));
		assertTrue(turn
				.getNextActions(new UseSectorCardAction(sectorCardNoObj))
				.contains(UseObjAction.class));
		assertTrue(turn
				.getNextActions(new UseSectorCardAction(sectorCardNoObj))
				.contains(EndTurnAction.class));
		assertTrue(turn.getNextActions(new UseSectorCardAction(sectorCardObj))
				.contains(EndTurnAction.class));

		// Test with a player that has collected three object card and has drawn
		// the fourth
		game.addPlayer(playerFull);
		game.shiftCurrentplayer();
		assertTrue(turn.getNextActions(new UseSectorCardAction(sectorCardObj))
				.contains(DiscardAction.class));
		assertTrue(turn.getNextActions(new DiscardAction(null)).contains(
				UseObjAction.class));
		assertTrue(turn.getNextActions(new DiscardAction(null)).contains(
				EndTurnAction.class));

		playerFull.setHasMoved(true);
		assertTrue(turn.getNextActions(new UseObjAction(null)).contains(
				EndTurnAction.class));

		assertTrue(turn.getNextActions(new UseObjAction(null)).contains(
				UseObjAction.class));

		assertTrue(turn.getNextActions(new DrawRescueCardAction()).contains(
				UseObjAction.class));
		assertTrue(turn.getNextActions(new DrawRescueCardAction()).contains(
				EndTurnAction.class));
	}
}
