package factoriesClassTests;

import static org.junit.Assert.*;

import org.junit.Test;

import factories.GalileiGameMapFactory;

/**
 * Tests for the GalileiGameMapFactory
 * 
 * @see GameMapFactoryTest
 * @author Andrea Sessa
 * @author Giorgio Pea
 */
public class GalileiGameMapFactoryTest {

	/**
	 * Test the execution of the makeMap test
	 */
	@Test
	public void testMake() {
		GalileiGameMapFactory factory = new GalileiGameMapFactory();
		assertEquals("GALILEI", factory.makeMap().getName());
	}

}
