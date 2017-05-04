package server;

import server_store.ServerStore;
import store_actions.GameTurnTimeoutExpiredAction;

import java.util.TimerTask;

/**
 * Created by giorgiopea on 20/03/17.
 */
public class TurnTimeout extends TimerTask {

    private final ServerStore store = ServerStore.getInstance();
    private final Integer gameId;

    public TurnTimeout(Integer gameId) {
        this.gameId = gameId;
    }

    @Override
    public void run() {
        store.dispatchAction(new GameTurnTimeoutExpiredAction(gameId));
    }
}
