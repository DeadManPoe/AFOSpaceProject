package server_store;

import common.Action;
import common.PlayerToken;

/**
 * Created by giorgiopea on 13/03/17.
 */
public class GameActionPlayerHandler {
    public Integer gameId;
    public PlayerToken playerToken;
    public Action action;

    public GameActionPlayerHandler(Integer gameId, PlayerToken playerToken, Action action, ) {
        this.gameId = gameId;
        this.playerToken = playerToken;
        this.action = action;
    }
}
