package server;

import server_store.ServerStore;
import store_actions.GameTurnTimeoutExpiredAction;

import java.util.TimerTask;


/**
 * A thread that notifies an associated {@link Game}.
 */
public class TurnTimeout extends TimerTask {

    private final ServerStore store = ServerStore.getInstance();
    private final Integer gameId;

    public TurnTimeout(int gameId) {
        this.gameId = gameId;
    }

    @Override
    public void run() {
        store.dispatchAction(new GameTurnTimeoutExpiredAction(gameId));
    }
}
