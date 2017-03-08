package decks;

import java.util.ArrayList;
import java.util.List;

import common.Card;

/**
 * Represents a deck containing discarded cards
 * 
 * @author Andrea Sessa
 * @author Giorgio Pea
 */
public class DiscardDeck {
	// The set of cards contained in the discard deck
	private final List<Card> content;

	/**
	 * Constructs an empty discard deck
	 */
	public DiscardDeck() {
		this.content = new ArrayList<Card>();
	}

	/**
	 * Adds a card to the discard deck
	 * 
	 * @param card
	 *            the card to be added to the discard deck
	 */
	public void addCard(Card card) {
		this.content.add(card);
	}

	/**
	 * Gets all the cards of the discard deck
	 * 
	 * @return all the cards of the discard deck
	 * 
	 */
	public List<Card> getCards() {
		return new ArrayList<Card>(this.content);
	}

	/**
	 * Removes all the cards of the discard deck
	 */
	public void removeCards() {
		this.content.removeAll(this.content);
	}
}
