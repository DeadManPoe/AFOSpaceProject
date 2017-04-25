/*
package it.polimi.ingsw.cg_19;

import static org.junit.Assert.*;
import org.junit.Test;
import common.Coordinate;
import common.Sector;
import common.SectorType;

*/
/**
 * Tests for the Player class
 * 
 * @author Andrea Sessa
 * @author Giorgio Pea
 * @see Player
 *//*

public class PlayerTest {

	// The player variable that is used for all the tests below
	private Player player;

	*/
/**
	 * Test for the constructor and the getters
	 *//*

	@Test
	public void initTest() {
		*/
/*
		 * Testing if the constructor fills correctly all the fields
		 *//*

		player = new Player(PlayerType.ALIEN, "");
		assertFalse(player.isAdrenaline());
		assertFalse(player.isSedated());
		assertNotEquals(player.getPrivateDeck(), null);
		assertTrue(player.getPrivateDeck().getContent().isEmpty());
		assertEquals(player.getPlayerType(), PlayerType.ALIEN);
		assertEquals(player.getPlayerState(), PlayerState.ALIVE);
		assertEquals(player.getSpeed(), 2);
		player = new Player(PlayerType.HUMAN, "");
		assertEquals(player.getPlayerType(), PlayerType.HUMAN);
		assertEquals(player.getSpeed(), 1);
	}

	*/
/**
	 * Test for the setPlayerState method
	 *//*

	@Test
	public void setPlayerStateTest() {
		*/
/*
		 * Testing if the set of the player state ends correctly
		 *//*

		player = new Player(PlayerType.ALIEN, "");
		player.setPlayerState(PlayerState.DEAD);
		assertEquals(PlayerState.DEAD, player.getPlayerState());
	}

	*/
/**
	 * Test for the setPlayerState method
	 *//*

	@Test
	public void setSpeedTest() {
		*/
/*
		 * Testing if the set of the player speed ends correctly
		 *//*

		player = new Player(PlayerType.ALIEN, "");
		player.setSpeed(1);
		assertEquals(1, player.getSpeed());
	}

	*/
/**
	 * Test for the setMySector method
	 *//*

	@Test
	public void setMySectorTest() {
		*/
/*
		 * Testing if the set of the player sector ends correctly
		 *//*

		player = new Player(PlayerType.ALIEN, "");
		Sector sector = new Sector(new Coordinate('A', 1), SectorType.SAFE);
		player.setSector(sector);
		assertEquals(sector, player.getSector());
	}

	*/
/**
	 * Test for the setSedated method
	 *//*

	@Test
	public void setSedatedTest() {
		*/
/*
		 * Testing if the set of the player isSedate flag ends correctly
		 *//*

		player = new Player(PlayerType.ALIEN, "");
		player.setSedated(true);
		assertTrue(player.isSedated());
	}

	*/
/**
	 * Test for the equals() method
	 *//*

	@Test
	public void testEquals() {
		Player p1 = new Player(PlayerType.ALIEN, "");
		Player p2 = new Player(PlayerType.HUMAN, "");

		assertFalse(p1.equals(p2));
		assertTrue(p1.equals(p1));
		assertFalse(p1.equals(null));
		assertFalse(p2.equals(new String("hello")));
	}

	*/
/**
	 * Test for setAdrenaline method
	 *//*

	@Test
	public void TestsetAdrenaline() {
		Player p = new Player(PlayerType.HUMAN, "");
		p.setAdrenaline(true);
		assertTrue(p.isAdrenaline());
	}

	*/
/**
	 * Checks that the getName returns the correctly the name of the player
	 *//*

	@Test
	public void TestgetName() {
		Player p = new Player(PlayerType.HUMAN, "Andrea");
		assertEquals("Andrea", p.getName());
	}
	
}
*/
