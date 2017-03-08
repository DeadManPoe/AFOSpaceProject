package it.polimi.ingsw.cg_19;

import static org.junit.Assert.*;

import org.junit.Test;

import common.AdrenalineObjectCard;
import common.ObjectCard;
import common.PrivateDeck;

/**
 * Some tests for the PrivateDeck class
 * 
 * @author Andrea Sessa
 * @author Giorgio Pea
 */
public class PrivateDeckTest {

	/**
	 * Checks that the getCard method returns the correct card added early
	 */
	@Test
	public void testGetCardObjectCard() {
		PrivateDeck deck = new PrivateDeck();
		ObjectCard card = new AdrenalineObjectCard();
		deck.addCard(card);
		assertEquals(card, deck.getCard(card));
	}

	/**
	 * Checks that the getCard method returns the correct card added early
	 */
	@Test
	public void testGetCardInt() {
		PrivateDeck deck = new PrivateDeck();
		ObjectCard card = new AdrenalineObjectCard();
		deck.addCard(card);
		assertEquals(card, deck.getCard(0));
	}

	/**
	 * Checks that the RemoveCard method correctly removes the card
	 */
	@Test
	public void testRemoveCard() {
		PrivateDeck deck = new PrivateDeck();
		ObjectCard card = new AdrenalineObjectCard();
		deck.addCard(card);
		deck.removeCard(card);
		assertNull(deck.getCard(card));
	}

	/**
	 * Checks that the addCard method correctly add a card to the deck
	 */
	@Test
	public void testAddCard() {
		PrivateDeck deck = new PrivateDeck();
		ObjectCard card = new AdrenalineObjectCard();
		deck.addCard(card);
		assertEquals(card, deck.getCard(card));
	}

}
