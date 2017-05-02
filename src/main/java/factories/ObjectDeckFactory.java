package factories;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import common.*;
import decks.*;

/**
 * Represents a factory of object card's decks
 *
 */
public class ObjectDeckFactory extends DeckFactory {

	@Override
	public Deck makeDeck() {
		List<Card> deck = new ArrayList<Card>();
		// Object cards insertion
		deck.add(new AttackObjectCard(null));
		deck.add(new AttackObjectCard(null));
		deck.add(new TeleportObjectCard());
		deck.add(new AdrenalineObjectCard());
		deck.add(new AdrenalineObjectCard());
		deck.add(new AdrenalineObjectCard());
		deck.add(new SuppressorObjectCard());
		deck.add(new SuppressorObjectCard());
		deck.add(new SuppressorObjectCard());
		deck.add(new DefenseObjectCard());
		deck.add(new LightsObjectCard(null));
		deck.add(new LightsObjectCard(null));
		// Deck shuffling
		long seed = System.nanoTime();
		Collections.shuffle(deck, new Random(seed));
		return new ObjectDeck(deck, new DiscardDeck());
	}

}
