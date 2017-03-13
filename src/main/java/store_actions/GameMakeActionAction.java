package store_actions;

import common.Action;
import common.PlayerToken;
import it.polimi.ingsw.cg_19.Game;
import server_store.GameActionPlayer;

/**
 * Created by giorgiopea on 12/03/17.
 */
public class GameMakeActionAction extends sts.Action {

    public GameActionPlayer payload;

    public GameMakeActionAction(Integer gameId, Action action, PlayerToken playerToken ) {
        super("@GAME_MAKE_ACTION");
        this.payload = new GameActionPlayer(gameId,playerToken, action);
    }
}
