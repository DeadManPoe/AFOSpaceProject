package server_store_actions;

import server.Game;
import server_store.StoreAction;

/**
 * Created by giorgiopea on 14/03/17.
 */
public class GameStartGameAction extends StoreAction {

    private final Game game;

    public GameStartGameAction(Game game) {
        super("@GAME_START_GAME");
        this.game = game;
    }

    public Game getGame() {
        return game;
    }
}
