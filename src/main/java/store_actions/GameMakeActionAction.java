package store_actions;

import common.Action;
import common.PlayerToken;
import server_store.GameActionPlayerHandler;

/**
 * Created by giorgiopea on 12/03/17.
 */
public class GameMakeActionAction extends sts.Action {

    public GameActionPlayerHandler payload;

    public GameMakeActionAction(Integer gameId, Action action, PlayerToken playerToken ) {
        super("@GAME_MAKE_ACTION");
        this.payload = new GameActionPlayerHandler(gameId,playerToken, action);
    }
}
