package decks;

import java.util.Collections;
import java.util.List;

import common.Card;
import common.ObjectCard;

/**
 * Represents a deck containing object cards
 */
public class ObjectDeck extends Deck {

	// The discard deck associated with the object deck
	private final DiscardDeck discardDeck;

	/**
	 * Constructs a deck containing object cards from a set of cards and a
	 * discard deck
	 *
	 * @param content
	 *            the set of cards to insert in the object deck
	 * @param discardDeck
	 *            the discard deck associated with the object deck
	 */
	public ObjectDeck(List<Card> content, DiscardDeck discardDeck) {
		super(content);
		this.discardDeck = discardDeck;
	}

	/**
	 * Gets the discard deck associated with the object deck
	 *
	 * @return The discard deck associated with the object deck
	 */
	public DiscardDeck getDiscardDeck() {
		return discardDeck;
	}

	/**
	 * Refills the object deck with all the cards of its associated discard deck
	 *
	 */
	public void refill() {
		/*
		 * The refill is performed if and only if there are no cards in the
		 * object deck and there's at least one card in its discard deck
		 */
		if (this.content.isEmpty() && !this.discardDeck.getCards().isEmpty()) {
			List<Card> tmpList = this.discardDeck.getCards();
			Collections.shuffle(tmpList);
			this.content.addAll(tmpList);
			this.discardDeck.removeCards();
		}
	}

	/**
	 * Adds a card to the object deck's associated discard deck
	 * 
	 * @param card
	 *            The card to add to the object deck's associated discard deck
	 */
	public void addToDiscard(ObjectCard card) {
		this.discardDeck.addCard(card);
	}

	@Override
	public Card popCard() {
		this.refill();
		if (this.content.isEmpty()) {
			return null;
		}
		Card card = this.content.get(0);
		this.content.remove(0);
		return (ObjectCard) card;
	}

}
