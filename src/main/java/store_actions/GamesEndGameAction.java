package store_actions;

import server_store.StoreAction;

/**
 * Created by giorgiopea on 20/03/17.
 */
public class GamesEndGameAction extends StoreAction {

    Integer payload;

    public GamesEndGameAction(Integer gameId) {
        super("@GAMES_END_GAME");
        this.payload = gameId;
    }

    public Integer getPayload() {
        return payload;
    }
}
