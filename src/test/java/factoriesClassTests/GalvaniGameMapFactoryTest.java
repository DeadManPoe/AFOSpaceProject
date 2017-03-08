package factoriesClassTests;

import static org.junit.Assert.*;

import org.junit.Test;

import factories.GalvaniGameMapFactory;

/**
 * Tests for the GalvaniMapFactory class
 * 
 * @author Andrea Sessa
 * @author Giorgio Pea
 */
public class GalvaniGameMapFactoryTest {

	/**
	 * Test for the execution of makeMap
	 */
	@Test
	public void testMake() {
		GalvaniGameMapFactory factory = new GalvaniGameMapFactory();
		assertEquals("GALVANI", factory.makeMap().getName());
	}

}
