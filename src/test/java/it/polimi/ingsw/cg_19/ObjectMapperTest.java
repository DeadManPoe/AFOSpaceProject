package it.polimi.ingsw.cg_19;

import static org.junit.Assert.*;

import org.junit.Test;

import common.*;
import effects.*;

/**
 * Some tests for the ObjectMapper class
 * 
 * @author Andrea Sessa
 * @author Giorgio Pea
 * @see ObjectCardsMapper
 */
public class ObjectMapperTest {

	/**
	 * Checks the correct binding between ObjectCard and ObjectEffect
	 * 
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	@Test
	public void testGetEffect() throws InstantiationException,
			IllegalAccessException {
		ObjectCardsMapper objectMapper = new ObjectCardsMapper();

		// Assert the correct binding between object and effects
		//assertTrue(objectMapper.getEffect(new TeleportObjectCard()) instanceof TeleportObjCardEffect);
		//assertTrue(objectMapper.getEffect(new LightsObjectCard(null)) instanceof LightObjectCardEffect);
		//assertTrue(objectMapper.getEffect(new AdrenalineObjectCard()) instanceof AdrenalineObjCardEffect);
		//assertTrue(objectMapper.getEffect(new SuppressorObjectCard()) instanceof SuppressorEffect);
		//assertTrue(objectMapper.getEffect(new AttackObjectCard(null)) instanceof AttackObjCardEffect);
		//assertTrue(objectMapper.getEffect(new DefenseObjectCard()) instanceof DefenseObjCardEffect);
	}

}
