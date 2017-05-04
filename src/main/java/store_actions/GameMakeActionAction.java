package store_actions;

import common.Action;
import common.PlayerToken;
import server_store.StoreAction;

import java.util.UUID;

/**
 * Created by giorgiopea on 12/03/17.
 */
public class GameMakeActionAction extends StoreAction {

    private final PlayerToken playerToken;
    private final StoreAction action;

    public GameMakeActionAction(PlayerToken playerToken, StoreAction action) {
        super("@GAME_MAKE_ACTION");
        this.playerToken = playerToken;
        this.action = action;
    }

    public PlayerToken getPlayerToken() {
        return playerToken;
    }

    public StoreAction getAction() {
        return action;
    }
}
