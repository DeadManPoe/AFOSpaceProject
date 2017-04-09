package store_actions;

import server_store.Game;
import server_store.StoreAction;

/**
 * Created by giorgiopea on 21/03/17.
 */
public class GameActionAction extends StoreAction {

    public Game game;
    public StoreAction action;

    public GameActionAction(StoreAction action, Game game) {
        super(action.type);
        this.action = action;
        this.game = game;
    }
}
