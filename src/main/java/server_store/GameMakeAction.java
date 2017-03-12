package server_store;

import common.Action;
import it.polimi.ingsw.cg_19.Game;

/**
 * Created by giorgiopea on 12/03/17.
 */
public class GameMakeAction {
    public Game game;
    public Action action;

    public GameMakeAction(Game game, Action action) {
        this.game = game;
        this.action = action;
    }
}
