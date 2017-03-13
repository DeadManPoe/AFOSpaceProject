package server_store;

import sts.Action;

/**
 * Created by giorgiopea on 13/03/17.
 */
public class StartGameAction extends Action {
    public Integer payload;
    public StartGameAction(Integer gameId) {
        super("@GAME_START_GAME");
        this.payload = gameId;
    }
}
