package store_actions;

import server.Game;
import server_store.StoreAction;

/**
 * Created by giorgiopea on 21/03/17.
 *
 */
public class GameActionAction extends StoreAction {

    private final Game game;
    private final StoreAction action;

    public GameActionAction(StoreAction action, Game game) {
        super(action.type);
        this.action = action;
        this.game = game;
    }

    public Game getGame() {
        return game;
    }

    public StoreAction getAction() {
        return action;
    }
}
