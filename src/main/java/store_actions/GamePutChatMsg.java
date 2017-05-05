package store_actions;

import common.PlayerToken;
import server.Game;
import server_store.StoreAction;

/**
 * Created by giorgiopea on 04/05/17.
 *
 */
public class GamePutChatMsg extends StoreAction {
    private final Game game;
    private final PlayerToken playerToken;
    private final String message;
    public GamePutChatMsg(Game game ,String message, PlayerToken playerToken) {
        super("@GAME_PUT_CHAT_MSG");
        this.message = message;
        this.playerToken = playerToken;
        this.game = game;
    }

    public Game getGame() {
        return game;
    }

    public PlayerToken getPlayerToken() {
        return playerToken;
    }

    public String getMessage() {
        return message;
    }
}
