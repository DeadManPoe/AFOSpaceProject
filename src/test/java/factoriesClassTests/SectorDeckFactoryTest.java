package factoriesClassTests;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import decks.SectorDeck;
import factories.SectorDeckFactory;

/**
 * Tests for ObjectDeckFactory class
 * 
 * @author Andrea Sessa
 * @author Giorgio Pea
 * @see Deck, SectorDeck
 */
public class SectorDeckFactoryTest {
	// A deck with sector cards
	private SectorDeck deck = (SectorDeck) new SectorDeckFactory().makeDeck();

	/**
	 * Test for the make method
	 */
	@Test
	public void makeTest() {
		/*
		 * Testing if the method produces a deck with the right number of cards
		 * and a discard deck with the right number of cards
		 */
		assertEquals(25, deck.getContent().size());
		assertEquals(0, deck.getDiscardDeck().getCards().size());
	}
}
