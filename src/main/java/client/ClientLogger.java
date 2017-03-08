package client;

import java.util.logging.Logger;

/**
 * Represents a Logger used to log messages on the client
 * 
 * @author Andrea Sessa
 * @author Giorgio Pea
 */
public class ClientLogger {
	private static final Logger LOGGER = Logger.getLogger(Client.class
			.getName());

	/**
	 * Gets the static instance of the logger
	 * 
	 * @return the static instance of the logger
	 */
	public static Logger getLogger() {
		return LOGGER;
	}
}