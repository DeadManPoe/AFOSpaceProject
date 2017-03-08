package factoriesClassTests;

import static org.junit.Assert.*;
import org.junit.Test;
import decks.ObjectDeck;
import factories.ObjectDeckFactory;

/**
 * Tests for ObjectDeckFactory class
 * 
 * @author Andrea Sessa
 * @author Giorgio Pea
 * @see Deck
 * @see ObjectDeck
 */
public class ObjectDeckFactoryTest {
	// A deck of object cards
	private ObjectDeck deck = (ObjectDeck) new ObjectDeckFactory().makeDeck();

	/**
	 * Test for the make method
	 */
	@Test
	public void sizeTest() {
		/*
		 * Testing if the method produces a deck with the right number of cards
		 * and a discard deck with the right number of cards
		 */
		assertEquals(12, deck.getContent().size());
		assertEquals(0, deck.getDiscardDeck().getCards().size());
	}

}
