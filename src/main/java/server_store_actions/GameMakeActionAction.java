package server_store_actions;

import common.PlayerToken;
import server.Game;
import server_store.StoreAction;

/**
 * Created by giorgiopea on 12/03/17.
 */
public class GameMakeActionAction extends StoreAction {

    private final PlayerToken playerToken;
    private final StoreAction action;
    private final Game game;

    public GameMakeActionAction(Game game, PlayerToken playerToken, StoreAction action) {
        super("@GAME_MAKE_ACTION");
        this.playerToken = playerToken;
        this.action = action;
        this.game = game;
    }

    public PlayerToken getPlayerToken() {
        return playerToken;
    }

    public StoreAction getAction() {
        return action;
    }

    public Game getGame() {
        return game;
    }
}
