package server_store_actions;

import server.Game;
import server_store.StoreAction;

/**
 * Created by giorgiopea on 20/03/17.
 */
public class GameTurnTimeoutExpiredAction extends StoreAction {

    private final Game game;

    public GameTurnTimeoutExpiredAction(Game game) {
        super("@GAME_TURNTIMEOUT_EXPIRED");
        this.game = game;
    }

    public Game getGame() {
        return game;
    }
}
