package store_actions;

import server_store.Game;
import server_store.StoreAction;

/**
 * Created by giorgiopea on 14/03/17.
 */
public class GamesAddGameAction extends StoreAction {

    private Game payload;

    public GamesAddGameAction(Game game) {
        this.type = "@GAMES_ADD_GAME";
        this.payload = game;
    }

    public Game getPayload() {
        return payload;
    }
}
