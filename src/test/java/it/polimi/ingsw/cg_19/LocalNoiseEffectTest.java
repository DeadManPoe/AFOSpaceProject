package it.polimi.ingsw.cg_19;

import static org.junit.Assert.*;

import org.junit.Test;

import common.Coordinate;
import common.LocalNoiseSectorCard;
import common.PSClientNotification;
import common.RRClientNotification;
import common.Sector;
import common.SectorType;
import effects.LocalNoiseSectorCardEffect;

/**
 * Some tests for the LocalNoiseEffect class
 * 
 * @author Andrea Sessa
 * @author Giorgio Pea
 *
 */
public class LocalNoiseEffectTest {

	/**
	 * Test for the executeEffect method
	 */
	@Test
	public void testExecuteEffect() {
		GameMap nullMap = null;
		RRClientNotification n1 = new RRClientNotification();
		PSClientNotification n2 = new PSClientNotification();
		Game g = new Game(nullMap);
		Player p = new Player(PlayerType.ALIEN, "");
		p.setSector(new Sector(new Coordinate('a', 1), SectorType.SAFE));
		g.addPlayer(p);
		LocalNoiseSectorCardEffect effect = new LocalNoiseSectorCardEffect(
				new LocalNoiseSectorCard(false));
		assertTrue(effect.executeEffect(g, n1, n2));
	}

	/**
	 * Test for the empty constructor
	 */
	@Test
	public void testLocalNoiseEffect() {
		LocalNoiseSectorCardEffect effect = new LocalNoiseSectorCardEffect();
		assertNull(effect.getCard());
	}

}
