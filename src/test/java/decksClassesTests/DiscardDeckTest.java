package decksClassesTests;

import static org.junit.Assert.*;

import org.junit.Test;

import common.AttackObjectCard;
import common.Card;
import common.Coordinate;
import common.Sector;
import common.SectorType;
import decks.DiscardDeck;

/**
 * Tests for the DiscardDeck class
 * 
 * @author Andrea Sessa
 * @author Giorgio Pea
 * @see DiscardDeck
 * @see Card
 * @see ObjectCard
 */
public class DiscardDeckTest {

	/*
	 * The ObjectDeck variable that is used for all the tests below
	 */
	private DiscardDeck discardDeck;

	/**
	 * Test for the constructor
	 */
	@Test
	public void testInit() {
		/*
		 * Testing if the constructor of the sector deck fills correctly the
		 * content field
		 */
		discardDeck = new DiscardDeck();
		assertTrue(discardDeck.getCards().isEmpty());
	}

	@Test
	public void testAddCardGetCards() {
		/*
		 * Testing if addCard changes correctly the content of the discard deck
		 * Testing if getCards returns the correct cards
		 */
		discardDeck = new DiscardDeck();
		Card card = new AttackObjectCard(new Sector(new Coordinate('A', 1),
				SectorType.SAFE));
		discardDeck.addCard(card);
		assertTrue(discardDeck.getCards().contains(card));
		assertEquals(discardDeck.getCards().size(), 1);
	}

	/**
	 * Test for the removeCards method
	 */
	@Test
	public void testRemoveCards() {
		/*
		 * Testing if removeCards remove all the cards of the discard deck so
		 * its size is zero
		 */
		discardDeck = new DiscardDeck();
		discardDeck.removeCards();
		assertTrue(discardDeck.getCards().isEmpty());
		Card card = new AttackObjectCard(new Sector(new Coordinate('A', 1),
				SectorType.SAFE));
		discardDeck.addCard(card);
		discardDeck.removeCards();
		assertTrue(discardDeck.getCards().isEmpty());
	}
}
