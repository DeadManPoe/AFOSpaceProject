package factories;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import common.Card;
import common.GlobalNoiseSectorCard;
import common.LocalNoiseSectorCard;
import common.SilenceSectorCard;
import decks.*;

/**
 * Represents a factory of sector card's decks
 *
 */
public class SectorDeckFactory extends DeckFactory {
	@Override
	public Deck makeDeck() {
		// Deck creation
		List<Card> deck = new ArrayList<Card>();
		for (int i = 0; i < 5; i++) {
			deck.add(new SilenceSectorCard());
		}
		for (int i = 0; i < 4; i++) {
			deck.add(new GlobalNoiseSectorCard(true, null));
		}
		for (int i = 0; i < 6; i++) {
			deck.add(new GlobalNoiseSectorCard(true, null));
		}
		for (int i = 0; i < 4; i++) {
			deck.add(new LocalNoiseSectorCard(true));
		}
		for (int i = 0; i < 6; i++) {
			deck.add(new LocalNoiseSectorCard(true));
		}
		// Deck shuffling
		long seed = System.nanoTime();
		Collections.shuffle(deck, new Random(seed));
		return new SectorDeck(deck, new DiscardDeck());
	}

}
