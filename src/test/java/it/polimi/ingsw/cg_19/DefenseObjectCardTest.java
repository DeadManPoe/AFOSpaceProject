package it.polimi.ingsw.cg_19;

import static org.junit.Assert.*;

import org.junit.Test;

import common.DefenseObjectCard;

/**
 * Some tests for the DefenseObjectCard
 * 
 * @author Andrea Sessa
 * @author Giorgio Pea
 */
public class DefenseObjectCardTest {

	@Test
	public void testToString() {
		DefenseObjectCard card = new DefenseObjectCard();
		assertEquals("DefenseObjectCard", card.toString());
	}

}
