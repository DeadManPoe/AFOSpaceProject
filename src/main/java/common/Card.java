package common;

import java.io.Serializable;

/**
 * Represents a card in the game This class is empty because is only used for
 * polymorphism
 *
 */
public class Card implements Serializable {
	// A field automatically created for serialization purposes
	private static final long serialVersionUID = 1L;

	@Override
	public int hashCode() {
		return 0;
	}

	@Override
	public boolean equals(Object obj) {
		return this.getClass().equals(obj.getClass());
	}
}
