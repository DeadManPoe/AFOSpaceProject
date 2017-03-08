package factoriesClassTests;

import static org.junit.Assert.*;

import org.junit.Test;

import decks.RescueDeck;
import factories.RescueDeckFactory;

/**
 * Tests for RescueDeckFactory class
 * 
 * @author Andrea Sessa
 * @author Giorgio Pea
 * @see Deck, RescueDeck
 */
public class RescueDeckFactoryTest {
	// A deck of rescue cards
	private RescueDeck deck = (RescueDeck) new RescueDeckFactory().makeDeck();

	/**
	 * Test for the make method
	 */
	@Test
	public void makeTest() {
		/*
		 * Testing if the method produces a deck with the right number of cards
		 */
		assertEquals(6, deck.getContent().size());
	}

}
