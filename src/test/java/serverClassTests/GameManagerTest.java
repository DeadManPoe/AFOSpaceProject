package serverClassTests;

import it.polimi.ingsw.cg_19.PlayerType;

import org.junit.Test;

import common.PlayerToken;

/**
 * Some tests for the GameManager class Not all methods are tested because of
 * RMI/Socket depedency
 * 
 * @author Andrea Sessa
 * @author Giorgio Pea
 */
public class GameManagerTest {
	private GameManager gameManager = GameManager.getInstance();

	@Test
	/**
	 * Test for the getGame method
	 */
	public void testAddGameNoTimer() {
		Game game = new Game("FERMI");
		int gameIndex = game.getPublicData().getId();
		gameManager.addGameNoTimer(game);
		assertEquals(game, gameManager.getGame(gameIndex));
	}

	/**
	 * Checks that a player is correctly added to a game
	 */
	@Test
	public void testAddPlayerToGame() {
		Game game = new Game("FERMI");
		int gameIndex = game.getPublicData().getId();
		gameManager.addGameNoTimer(game);
		PlayerToken playerToken = new PlayerToken(PlayerType.ALIEN,null);
		gameManager.addPlayerToGame(playerToken, gameIndex);
		assertEquals(game, gameManager.getGame(playerToken));
		gameManager.addPlayerToGame(playerToken, 23232);
		assertEquals(null, gameManager.getGame(playerToken));
		playerToken = null;
		gameManager.addPlayerToGame(playerToken, gameIndex);
		assertEquals(game, gameManager.getGame(playerToken));
		gameManager.addPlayerToGame(playerToken, gameIndex);
		gameManager.addPlayerToGame(playerToken, gameIndex);
		assertEquals(game, gameManager.getGame(playerToken));

	}

	/**
	 * Checks that a game is correctly removed
	 */
	@Test
	public void testRemoveGame() {
		Game game = new Game("FERMI");
		int gameIndex = game.getPublicData().getId();
		gameManager.addGameNoTimer(game);
		PlayerToken playerToken = new PlayerToken(PlayerType.ALIEN,null);
		gameManager.addPlayerToGame(playerToken, gameIndex);
		gameManager.removeGame(game);
		assertEquals(null, gameManager.getGame(gameIndex));
		assertEquals(null, gameManager.getGame(playerToken));
	}
}
