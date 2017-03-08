package server;

import java.util.logging.Logger;

/**
 * Represents a Logger used to log messages by the server
 * 
 * @author Andrea Sessa
 * @author Giorgio Pea
 */
public class ServerLogger {
	private static final Logger LOGGER = Logger.getLogger(MainServer.class
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
