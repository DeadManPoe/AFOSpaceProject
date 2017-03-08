package decksClassesTests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import common.Card;
import decks.DiscardDeck;
import decks.SectorDeck;
import factories.SectorDeckFactory;

/**
 * Tests for the ObjectDeck class
 * 
 * @author Giorgio Pea
 * @see Deck
 * @see SectorDeck
 * @see SectorDeckFactory
 */
public class SectorDeckTest {
	private SectorDeck deck;

	/**
	 * Test for the constructor and for the getContent and for the
	 * getDiscardDeck methods
	 */
	@Test
	public void testInit() {
		/*
		 * Testing if the constructor of the sector deck fills correctly the
		 * content field and the discardDeck field
		 */
		List<Card> testList = new ArrayList<Card>();
		DiscardDeck testDiscard = new DiscardDeck();
		SectorDeck sectorDeck = new SectorDeck(testList, testDiscard);
		assertEquals(testList, sectorDeck.getContent());
		assertEquals(testDiscard, sectorDeck.getDiscardDeck());
	}

	/**
	 * Test for the popCard method
	 */
	@Test
	public void testPopCard() {
		/*
		 * Testing if the card popped was the first card of the deck, testing if
		 * the size of the deck - after the pop of a card - is correct
		 */
		deck = (SectorDeck) new SectorDeckFactory().makeDeck();
		Card current = deck.getContent().get(0);
		int oldSize = deck.getContent().size();
		Card poppedCard = deck.popCard();
		assertEquals(current, poppedCard);
		assertTrue(deck.getDiscardDeck().getCards().contains(poppedCard));
		assertEquals(deck.getContent().size(), oldSize - 1);

		/*
		 * Testing if the pop of all cards in the deck produces an empty deck
		 */
		deck = (SectorDeck) new SectorDeckFactory().makeDeck();
		List<Card> oldDeck = deck.getContent();
		int j = 0;
		while (j < 25) {
			deck.popCard();
			j++;
		}
		assertTrue(deck.getDiscardDeck().getCards().containsAll(oldDeck));
		assertTrue(deck.getContent().isEmpty());

		/*
		 * Testing if the pop of all cards + 1 in the deck produces an empty
		 * deck and an IndexOutOfBoundsException by returning null
		 */
		deck = (SectorDeck) new SectorDeckFactory().makeDeck();
		oldDeck = deck.getContent();
		int i = 0;
		while (i < 25) {
			deck.popCard();
			i++;
		}
		assertTrue(deck.getDiscardDeck().getCards().containsAll(oldDeck));
		assertTrue(deck.getContent().isEmpty());

	}

	/**
	 * Test for the refill method
	 */
	public void testRefill() {
		/*
		 * Testing if refilling an untouched deck produces the right behavior
		 */
		deck = (SectorDeck) new SectorDeckFactory().makeDeck();
		List<Card> oldDeck = new ArrayList<Card>(deck.getContent());
		deck.refill();
		assertEquals(25, deck.getContent().size());
		assertTrue(deck.getDiscardDeck().getCards().isEmpty());
		assertTrue(deck.getContent().containsAll(oldDeck));

		/*
		 * Testing if refilling a touched but non empty deck produces the right
		 * behavior
		 */
		deck = (SectorDeck) new SectorDeckFactory().makeDeck();
		deck.popCard();
		deck.popCard();
		oldDeck = new ArrayList<Card>(deck.getContent());
		deck.refill();
		assertEquals(23, deck.getContent().size());
		assertEquals(2, deck.getDiscardDeck().getCards().size());
		assertTrue(deck.getContent().containsAll(oldDeck));

		/*
		 * Testing if refilling a touched empty deck produces the right behavior
		 */
		deck = (SectorDeck) new SectorDeckFactory().makeDeck();
		oldDeck = new ArrayList<Card>(deck.getContent());
		for (int i = 0; i < 25; i++) {
			deck.popCard();
		}
		assertTrue(deck.getContent().isEmpty());
		assertEquals(25, deck.getDiscardDeck().getCards().size());
		assertTrue(deck.getDiscardDeck().getCards().containsAll(oldDeck));

	}
}
