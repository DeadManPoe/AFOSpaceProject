package factories;

import it.polimi.ingsw.cg_19.RescueType;

import java.util.ArrayList;
import java.util.List;

import common.Card;
import common.RescueCard;
import decks.Deck;
import decks.ObjectDeck;
import decks.RescueDeck;

/**
 * Represents a factory of rescue card's decks
 * 
 * @see DeckFactory
 * @see ObjectDeck
 * @author Andrea Sessa
 * @author Giorgio Pea
 */
public class RescueDeckFactory extends DeckFactory {
	/**
	 * @see DeckFactory#makeDeck
	 */
	@Override
	public Deck makeDeck() {
		List<Card> deck = new ArrayList<Card>();
		for (int i = 0; i < 3; i++) {
			deck.add(new RescueCard(RescueType.GREEN));
		}
		for (int i = 0; i < 3; i++) {
			deck.add(new RescueCard(RescueType.RED));
		}
		return new RescueDeck(deck);
	}

}
