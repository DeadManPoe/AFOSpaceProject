package decksClassesTests;

import static org.junit.Assert.*;

import org.junit.Test;

import common.AttackObjectCard;
import common.Coordinate;
import common.ObjectCard;
import common.PrivateDeck;
import common.Sector;
import common.SectorType;

/**
 * Tests for the privateDeckTest class
 * 
 * @author Andrea Sessa
 * @author Giorgio Pea
 * @see Card
 * @see ObjectCard
 */
public class privateDeckTest {

	// A privateDeck variable used in the tests below
	private PrivateDeck privateDeck;

	/**
	 * Test for the addCard and the getContent method
	 */
	@Test
	public void testAddCardGetContent() {
		/*
		 * Testing if addCard changes correctly the content of the discard deck
		 * Testing if getContent returns the correct cards
		 */
		privateDeck = new PrivateDeck();
		ObjectCard card = new AttackObjectCard(new Sector(
				new Coordinate('A', 1), SectorType.SAFE));
		privateDeck.addCard(card);
		assertTrue(privateDeck.getContent().contains(card));
		assertEquals(privateDeck.getContent().size(), 1);
	}

	/**
	 * Test for the getCard method and implicitly test for the constructor
	 */
	public void testGetCard() {
		/*
		 * Testing if getCard return null or the card passed in different
		 * scenarios
		 */
		privateDeck = new PrivateDeck();
		ObjectCard card = new AttackObjectCard(new Sector(
				new Coordinate('A', 1), SectorType.SAFE));
		assertEquals(privateDeck.getCard(card), null);
		privateDeck.addCard(card);
		assertEquals(privateDeck.getCard(card), card);
	}
}
