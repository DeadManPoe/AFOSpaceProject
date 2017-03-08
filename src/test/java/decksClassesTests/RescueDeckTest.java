package decksClassesTests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import factories.RescueDeckFactory;

import org.junit.Test;

import common.Card;
import decks.RescueDeck;

public class RescueDeckTest {
	/**
	 * Tests for the ObjectDeck class
	 * 
	 * @author Giorgio Pea
	 * @see Deck
	 * @see RescueDeck
	 * @see RescueDeckFactory
	 */
	private RescueDeck deck;

	/**
	 * Test for the constructor and test for the getContent method
	 */
	@Test
	public void testInit() {
		/*
		 * Testing if the constructor of the sector deck fills correctly the
		 * content field and the discardDeck field
		 */
		List<Card> testList = new ArrayList<Card>();
		RescueDeck rescueDeck = new RescueDeck(testList);
		assertEquals(testList, rescueDeck.getContent());
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
		deck = (RescueDeck) new RescueDeckFactory().makeDeck();
		Card current = deck.getContent().get(0);
		int oldSize = deck.getContent().size();
		Card poppedCard = deck.popCard();
		assertEquals(current, poppedCard);
		assertEquals(deck.getContent().size(), oldSize - 1);

		/*
		 * Testing if the pop of all cards in the deck produces an empty deck
		 */
		deck = (RescueDeck) new RescueDeckFactory().makeDeck();
		int j = 0;
		while (j < 6) {
			deck.popCard();
			j++;
		}
		assertTrue(deck.getContent().isEmpty());

		/*
		 * Testing if the pop of all cards + 1 in the deck produces an empty
		 * deck and an IndexOutOfBoundsException by returning null
		 */
		deck = (RescueDeck) new RescueDeckFactory().makeDeck();
		int i = 0;
		while (i < 6) {
			deck.popCard();
			i++;
		}
		assertTrue(deck.getContent().isEmpty());

	}
}
