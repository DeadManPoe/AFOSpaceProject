package decksClassesTests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import common.AttackObjectCard;
import common.Card;
import common.Coordinate;
import common.ObjectCard;
import common.Sector;
import common.SectorType;
import decks.DiscardDeck;
import decks.ObjectDeck;
import factories.ObjectDeckFactory;

/**
 * Tests for the ObjectDeck class
 * 
 * @author Andrea Sessa
 * @author Giorgio Pea
 * @see Deck
 * @see ObjectDeck
 * @see ObjectDeckFactory
 * @see DiscardDeck
 * @see DiscardDeckTest
 */
public class ObjectDeckTest {

	/*
	 * The ObjectDeck variable that is used for all the test below
	 */
	private ObjectDeck deck;

	/**
	 * Test for the constructor and getters
	 */
	@Test
	public void testInit() {
		/*
		 * Testing if the constructor of the sector deck fills correctly the
		 * content field and the discardDeck field
		 */
		List<Card> testList = new ArrayList<Card>();
		DiscardDeck testDiscard = new DiscardDeck();
		ObjectDeck objectDeck = new ObjectDeck(testList, testDiscard);
		assertEquals(testList, objectDeck.getContent());
		assertEquals(testDiscard, objectDeck.getDiscardDeck());
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
		deck = (ObjectDeck) new ObjectDeckFactory().makeDeck();
		Card current = deck.getContent().get(0);
		int oldSize = deck.getContent().size();
		Card poppedCard = deck.popCard();
		assertEquals(current, poppedCard);
		assertEquals(deck.getContent().size(), oldSize - 1);
		/*
		 * Testing if the pop of all cards in the deck produces an empty deck
		 */
		deck = (ObjectDeck) new ObjectDeckFactory().makeDeck();
		int j = 0;
		while (j < 12) {
			deck.popCard();
			j++;
		}
		assertTrue(deck.getContent().isEmpty());
		/*
		 * Testing if the pop of all cards + 1 in the deck produces an empty
		 * deck and an IndexOutOfBoundsException by returning null
		 */
		deck = (ObjectDeck) new ObjectDeckFactory().makeDeck();
		int i = 0;
		while (i < 12) {
			deck.popCard();
			i++;
		}
		assertTrue(deck.getContent().isEmpty());
	}

	/**
	 * Test for the refill method
	 */
	@Test
	public void testRefill() {
		/*
		 * Testing if refilling a non empty deck using no cards produces the
		 * right behavior
		 */
		deck = (ObjectDeck) new ObjectDeckFactory().makeDeck();
		List<Card> oldDeck = new ArrayList<Card>(deck.getContent());
		deck.refill();
		assertEquals(12, deck.getContent().size());
		assertEquals(0, deck.getDiscardDeck().getCards().size());
		assertTrue(deck.getContent().containsAll(oldDeck));
		/*
		 * Testing if refilling an empty deck using all the cards it had before
		 * produces the right behavior
		 */
		deck = (ObjectDeck) new ObjectDeckFactory().makeDeck();
		int j = 0;
		while (j < 12) {
			deck.addToDiscard((ObjectCard) deck.popCard());
			j++;
		}
		List<Card> oldDiscard = new ArrayList<Card>(deck.getDiscardDeck()
				.getCards());
		deck.refill();
		assertEquals(12, deck.getContent().size());
		assertTrue(deck.getContent().containsAll(oldDiscard));

		/*
		 * Testing if refilling an empty deck using no cards produces the right
		 * behavior
		 */
		deck = (ObjectDeck) new ObjectDeckFactory().makeDeck();
		j = 0;
		while (j < 12) {
			deck.popCard();
			j++;
		}
		deck.refill();
		assertTrue(deck.getContent().isEmpty());

		/*
		 * Testing if refilling an empty deck using some of the cards it had
		 * before produces the right behavior
		 */
		deck = (ObjectDeck) new ObjectDeckFactory().makeDeck();
		j = 0;
		while (j < 4) {
			deck.addToDiscard((ObjectCard) deck.popCard());
			j++;
		}
		j = 0;
		while (j < 8) {
			deck.popCard();
			j++;
		}
		List<Card> discardDeck = new ArrayList<Card>(deck.getDiscardDeck()
				.getCards());
		deck.refill();
		assertTrue(deck.getContent().containsAll(discardDeck));
		assertEquals(deck.getDiscardDeck().getCards().size(), 0);
		assertEquals(deck.getContent().size(), 4);
	}

	/**
	 * Test for the addToDiscard method
	 */
	@Test
	public void testAddToDiscard() {
		/*
		 * Testing if adding a card to discard produces the right behavior
		 */
		deck = (ObjectDeck) new ObjectDeckFactory().makeDeck();
		ObjectCard card = new AttackObjectCard(new Sector(
				new Coordinate('A', 1), SectorType.SAFE));
		deck.addToDiscard(card);
		assertTrue(deck.getDiscardDeck().getCards().contains(card));
		assertEquals(deck.getDiscardDeck().getCards().size(), 1);
	}
}
