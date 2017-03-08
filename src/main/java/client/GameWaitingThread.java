package client;

/**
 * Represents a thread that waits until the game joined by the client is ready
 * to start
 * 
 * @author Andrea Sessa
 * @author Giorgio Pea
 * @version 1.0
 */
public class GameWaitingThread implements Runnable {
	private GuiInteractionManager guiClient;
	private String mapName;

	public GameWaitingThread(GuiInteractionManager client, String mapName) {
		this.guiClient = client;
		this.mapName = mapName;
	}

	/**
	 * Waits until the game is started or ended
	 */
	@Override
	public void run() {
		while (!guiClient.getClient().isGameStarted()
				&& !guiClient.getClient().isGameEnded()) {
			synchronized (guiClient.getClient()) {
				try {
					guiClient.getClient().wait();
				} catch (InterruptedException e) {
					System.err.println(e.getMessage());
				}
			}
		}
		guiClient.initGame(mapName);
	}
}
