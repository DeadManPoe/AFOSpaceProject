package decks;

import java.util.Collections;
import java.util.List;
import common.Card;
import common.SectorCard;

/**
 * Represents a deck containing sector cards
 * 
 * @author Andrea Sessa
 * @author Giorgio Pea
 */
public class SectorDeck extends Deck {
	private final DiscardDeck discardDeck;

	/**
	 * Constructs a deck containing sector cards from a set of cards and a
	 * discard deck
	 * 
	 * @param content
	 *            the set of cards to insert in the sector deck
	 * @param discardDeck
	 *            the discard deck associated with the sector deck
	 */
	public SectorDeck(List<Card> content, DiscardDeck discardDeck) {
		super(content);
		this.discardDeck = discardDeck;
	}

	/**
	 * Gets the discard deck associated with the sector deck
	 * 
	 * @return The discard deck associated with the sector deck
	 */
	public DiscardDeck getDiscardDeck() {
		return discardDeck;
	}

	/**
	 * Refills the sector deck with all the cards of its associated discard deck
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
	 * Adds a card to the sector deck's associated discard deck
	 * 
	 * @param card
	 *            the card to add to the sector deck's associated discard deck
	 */
	public void addToDiscard(SectorCard card) {
		this.discardDeck.addCard(card);
	}

	/**
	 * @see Deck#popCard
	 */

	@Override
	public Card popCard() {
		/*
		 * An attempt to pop a card from the sect0r deck when the object deck is
		 * empty produces an IndexOutOfBoudsException that we manage. In this
		 * case when a card is popped, it'is automatically added to the sector
		 * deck's associated discard deck
		 */
		if (this.content.isEmpty()) {
			this.refill();
		}
		Card card = this.content.get(0);
		this.discardDeck.addCard(card);
		this.content.remove(0);
		return (SectorCard) card;

	}

}
