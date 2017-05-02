package factories;

import decks.Deck;

/**
 * Represents a factory of game's decks
 *
 */
public abstract class DeckFactory {
	/**
	 * Makes a game's deck
	 * 
	 * @return the deck made
	 */
	public abstract Deck makeDeck();
}
