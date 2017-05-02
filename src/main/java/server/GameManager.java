package server;

import it.polimi.ingsw.cg_19.Game;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Represents a manager of all the games running on the server, it also offers
 * services to build new games and destroy old ones.
 */
public class GameManager {
    private final List<Game> games;
    // The only game manager instance (singleton pattern)
    private static final GameManager instance = new GameManager();

    public static GameManager getInstance() {
        return instance;
    }

    private GameManager() {
        this.games = new ArrayList<>();
    }

    public Game getGame(int gameId) throws NoSuchElementException {
        for (Game game : this.games) {
            if (game.getId() == gameId) {
                return game;
            }
        }
        throw new NoSuchElementException("No game matches with the given id");
    }

    public void addGame(Game game) {
        this.games.add(game);
    }

    public List<Game> getGames() {
        return this.games;
    }

    public void endGame(Game game) {
        this.games.remove(game);
    }
}
