package store_actions;

import server_store.StoreAction;

/**
 * Created by giorgiopea on 20/03/17.
 */
public class GameEndGameAction extends StoreAction {

    Integer payload;

    public GameEndGameAction(Integer gameId) {
        this.type = "@GAME_END_GAME";
        this.payload = gameId;
    }

    public Integer getPayload() {
        return payload;
    }
}
