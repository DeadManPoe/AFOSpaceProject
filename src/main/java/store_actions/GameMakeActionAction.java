package store_actions;

import common.Action;
import common.PlayerToken;
import server_store.StoreAction;

import java.util.UUID;

/**
 * Created by giorgiopea on 12/03/17.
 */
public class GameMakeActionAction extends StoreAction {

    public GameMakeActionActionPayload payload;

    public GameMakeActionAction(PlayerToken playerToken, UUID uuid, StoreAction action) {
        super("@GAME_MAKE_ACTION");
        this.payload = new GameMakeActionActionPayload(playerToken,uuid,action);
    }
}
