package store_actions;

import common.PlayerToken;
import server_store.StoreAction;

/**
 * Created by giorgiopea on 04/05/17.
 *
 */
public class GamePutChatMsg extends StoreAction {
    private final PlayerToken playerToken;
    private final String message;
    public GamePutChatMsg(String message, PlayerToken playerToken) {
        super("@GAME_PUT_CHAT_MSG");
        this.message = message;
        this.playerToken = playerToken;
    }

    public PlayerToken getPlayerToken() {
        return playerToken;
    }

    public String getMessage() {
        return message;
    }
}
