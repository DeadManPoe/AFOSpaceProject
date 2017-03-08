package it.polimi.ingsw.cg_19;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import common.AdrenalineObjectCard;
import common.Card;
import common.Coordinate;
import common.ObjectCard;
import common.RRClientNotification;
import common.Sector;
import common.SectorType;

/**
 * Some tests for the RRClientNotification class
 * 
 * @author Andrea Sessa
 * @author Giorgio Pea
 */
public class RRClientNotificationTest {

	/**
	 * Tests that the constructor initializes the class correctly
	 */
	@Test
	public void testRRClientNotification() {
		ArrayList<Card> drawedCards = new ArrayList<Card>();
		ArrayList<Sector> lightedSectors = new ArrayList<Sector>();

		RRClientNotification rr = new RRClientNotification(true, drawedCards,
				lightedSectors);
		assertTrue(rr.getActionResult());
		assertEquals(drawedCards, rr.getDrawnCards());
		assertEquals(lightedSectors, rr.getLightedSectors());

	}

	/**
	 * Test the the set action result correctly updates the actionResult field
	 */
	@Test
	public void testSetActionResult() {
		ArrayList<Card> drawedCards = new ArrayList<Card>();
		ArrayList<Sector> lightedSectors = new ArrayList<Sector>();
		RRClientNotification rr = new RRClientNotification(true, drawedCards,
				lightedSectors);
		rr.setActionResult(false);
		assertFalse(rr.getActionResult());
	}

	/**
	 * Checks that the given card is correctly added to the list of drawedCards
	 */
	@Test
	public void testAddCard() {
		ArrayList<Card> drawedCards = new ArrayList<Card>();
		ArrayList<Sector> lightedSectors = new ArrayList<Sector>();
		ObjectCard card = new AdrenalineObjectCard();
		drawedCards.add(card);

		RRClientNotification rr = new RRClientNotification(true, drawedCards,
				lightedSectors);
		assertEquals(card, rr.getDrawnCards().get(0));
	}

	/**
	 * Checks that the given sector is correctly added to the list of lighted
	 * sectors
	 */
	@Test
	public void testAddSector() {
		ArrayList<Card> drawedCards = new ArrayList<Card>();
		ArrayList<Sector> lightedSectors = new ArrayList<Sector>();
		Sector sector = new Sector(new Coordinate('A', 1), SectorType.SAFE);
		lightedSectors.add(sector);
		RRClientNotification rr = new RRClientNotification(true, drawedCards,
				lightedSectors);
		assertEquals(sector, rr.getLightedSectors().get(0));
	}

}
