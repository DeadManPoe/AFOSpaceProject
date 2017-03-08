package decks;

import java.util.List;

import common.Card;
import common.RescueCard;

/**
 * Represents a deck containing rescue cards
 * 
 * @author Andrea Sessa
 * @author Giorgio Pea
 */
public class RescueDeck extends Deck {
	/**
	 * Constructs a deck containing rescue cards from a set of rescue cards
	 * 
	 * @param content
	 *            the set of cards to insert in the object deck
	 */
	public RescueDeck(List<Card> content) {
		super(content);
	}

	/**
	 * @see Deck#popCard
	 * @author Andrea Sessa
	 * @author Giorgio Pea
	 */
	@Override
	public Card popCard() {
		if (!this.content.isEmpty()) {
			Card card = this.content.get(0);
			this.content.remove(0);
			return (RescueCard) card;
		}
		return null;

	}
}
