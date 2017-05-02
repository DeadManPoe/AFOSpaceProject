package common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a private deck of object cards owned by a player
 *
 */
public class PrivateDeck implements Serializable{
	// The set of object cards contained in the private deck
	private volatile List<ObjectCard> content;

	/**
	 * Constructs a private deck of object cards owned by a player. This private
	 * deck is constructed with no cards.
	 */
	public PrivateDeck() {
		this.content = new ArrayList<ObjectCard>();
	}

	/**
	 * Gets all the cards in the private deck.
	 * 
	 * @return the set of cards contained in the private deck
	 */
	public List<ObjectCard> getContent() {
		return content;
	}

	/**
	 * Checks if a card is in the private deck's set of cards and returns that
	 * card. If the card is not in the the private deck's set of cards then null
	 * is returned.
	 * 
	 * @param card
	 *            The card whose presence the private deck's set of cards is
	 *            checked
	 * @return the card whose presence in the private deck's set of cards has
	 *         been checked with a positive result
	 */
	public ObjectCard getCard(ObjectCard card) {
		if (this.content.indexOf(card) != -1) {
			return card;
		}
		return null;
	}

	/**
	 * Gets a card from the private deck's set of cards that has the given index
	 * 
	 * @param cardIndex
	 *            the index of the card to be retrieved from the private deck's
	 *            set of cards
	 * @return the card from the private deck's set of cards that has the given
	 *         index
	 */
	public ObjectCard getCard(int cardIndex) {
		ObjectCard card = this.content.get(cardIndex);
		return card;
	}

	/**
	 * Removes the given card from the private deck's set of cards
	 * 
	 * @param card
	 *            the card to be removed from the private deck's set of cards
	 */
	public void removeCard(ObjectCard card) {
		this.content.remove(card);
	}

	/**
	 * Adds a card to the private deck's set of cards
	 * 
	 * @param objectCard
	 *            the card to be added to the private deck's set of cards
	 * @return true if the card will not increase the size of the private deck's
	 *         set of cards to 5, false otherwise
	 */
	public boolean addCard(ObjectCard card) {
		/*
		 * The private deck must contain a maximum of three cards
		 */
		if (content.size() <= 4) {
			content.add(card);
			return true;
		}
		return false;
	}

	/**
	 * Gets the size of the private deck's set of cards
	 * 
	 * @return the size of the private deck's set of cards
	 */
	public int getSize() {
		return this.content.size();
	}

}
