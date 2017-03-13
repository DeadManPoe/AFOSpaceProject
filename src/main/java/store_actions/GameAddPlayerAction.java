package store_actions;

import server_store.GameIdPlayerName;
import sts.Action;

/**
 * Created by giorgiopea on 13/03/17.
 */
public class GameAddPlayerAction extends Action {
    public GameIdPlayerName payload;

    public GameAddPlayerAction(String playerName, Integer gameId) {
        super("@GAME_ADD_PLAYER");
        this.payload = new GameIdPlayerName(gameId, playerName);
    }

}
