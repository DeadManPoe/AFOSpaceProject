package client_store_effects;

import client.GamePollingThread;
import client_store.ClientState;
import server_store.Effect;
import server_store.State;
import server_store.StoreAction;

/**
 * Created by giorgiopea on 05/04/17.
 */
public class SetAvailableGamesEffect implements Effect {
    @Override
    public void apply(StoreAction action, State state) {
        ClientState castedState = (ClientState) state;
        castedState.gamePollingTimer.scheduleAtFixedRate(new GamePollingThread(),0,castedState.gameListPollingPeriod);
    }
}
