package store_actions;

import server_store.StoreAction;

/**
 * Created by giorgiopea on 20/03/17.
 */
public class GameTurnTimeoutExpiredAction extends StoreAction {

    private Integer payload;

    public GameTurnTimeoutExpiredAction(Integer gameId) {
        super("@GAME_TURNTIMEOUT_EXPIRED");
        this.payload = gameId;
    }

    public Integer getPayload() {
        return payload;
    }
}
