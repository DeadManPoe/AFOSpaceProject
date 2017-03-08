package client;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.rmi.NotBoundException;

/**
 * Represents the entry point of the application in the case the CLI is used
 * 
 * @author Andrea Sessa
 * @author Giorgio Pea
 */
public class MainCLI {

	public static void main(String[] args) throws IOException,
			IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException,
			SecurityException, ClassNotFoundException, NotBoundException,
			InterruptedException {
		ClientConnection connection = new ClientConnection(29999, "localhost",
				"GAME");
		Client client = new Client(connection);
		CliInteractionManager interactionManager = CliInteractionManager
				.init(client);
		interactionManager.connChoice();
		interactionManager.showGames();

		// Wait the start of the game, a no cpu-time-consuming method
		while (!client.isGameStarted() && !client.isGameEnded()) {
			synchronized (client) {
				client.wait();
			}
		}
		if(client.isGameEnded()) {
			System.out.println("The timout is expired!, Your game has been canceled!");
			client.shutdown();
		}
		else
		 interactionManager.showMap();
	}
}
