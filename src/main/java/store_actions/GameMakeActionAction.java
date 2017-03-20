package store_actions;

import common.Action;
import common.PlayerToken;
import server_store.StoreAction;

import java.util.UUID;

/**
 * Created by giorgiopea on 12/03/17.
 */
public class GameMakeActionAction extends StoreAction {

    private GameMakeActionActionPayload payload;

    public GameMakeActionAction(Integer gameId, PlayerToken playerToken, UUID uuid, Action action) {
        this.type = "@GAME_MAKE_ACTION";
        this.payload = new GameMakeActionActionPayload(gameId,playerToken,uuid,action);
    }

    public GameMakeActionActionPayload getPayload() {
        return payload;
    }
}
