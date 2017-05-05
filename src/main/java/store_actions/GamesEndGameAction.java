package store_actions;

import server.Game;
import server_store.StoreAction;

/**
 * Created by giorgiopea on 20/03/17.
 */
public class GamesEndGameAction extends StoreAction {

    private final Game game;

    public GamesEndGameAction(Game game) {
        super("@GAMES_END_GAME");
        this.game = game;
    }

    public Game getGame() {
        return game;
    }
}
