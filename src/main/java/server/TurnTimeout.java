package server;

import server_store.ServerStore;
import server_store_actions.GameTurnTimeoutExpiredAction;

import java.util.TimerTask;


/**
 * A thread that notifies an associated {@link Game}.
 */
public class TurnTimeout extends TimerTask {

    private final ServerStore store = ServerStore.getInstance();
    private final Game game;

    public TurnTimeout(Game game) {
        this.game = game;
    }

    @Override
    public void run() {
        store.dispatchAction(new GameTurnTimeoutExpiredAction(game));
    }
}
