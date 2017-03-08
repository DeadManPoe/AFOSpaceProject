package factoriesClassTests;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import factories.FermiGameMapFactory;

/**
 * Some tests for the FermiGameMap factory
 * 
 * @author Andrea Sessa
 * @author Giorgio Pea
 */
public class FermiGameMapFactoryTest {

	/**
	 * Test the execution of the makeMap method
	 */
	@Test
	public void testMake() {
		FermiGameMapFactory factory = new FermiGameMapFactory();
		assertEquals("FERMI", factory.makeMap().getName());
	}

}
