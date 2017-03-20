package store_actions;

import server_store.StoreAction;

/**
 * Created by giorgiopea on 14/03/17.
 */
public class GameStartGameAction extends StoreAction {

    private Integer payload;

    public GameStartGameAction(Integer gameId) {
        this.type = "@GAME_START_GAME";
        this.payload = gameId;
    }

    public Integer getPayload() {
        return payload;
    }
}
