package server_store_actions;

import server.Game;
import server_store.StoreAction;

/**
 * Created by giorgiopea on 05/05/17.
 */
public class GameStartableGameAction extends StoreAction {
    private final Game game;
    private final boolean isStartable;

    public GameStartableGameAction(Game game, boolean isStartable) {
        super("@GAME_STARTABLE_GAME");
        this.game = game;
        this.isStartable = isStartable;
    }

    public Game getGame() {
        return game;
    }

    public boolean isStartable() {
        return isStartable;
    }
}
