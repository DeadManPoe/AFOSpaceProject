package client_store_effects;

import client.ClientState;
import server_store.Effect;
import server_store.State;
import server_store.StoreAction;

/**
 * Created by giorgiopea on 09/04/17.
 */
public class ClientStartGameEffect implements Effect {
    @Override
    public void apply(StoreAction action, State state) {
        ClientState castedState = (ClientState) state;
        castedState.gamePollingTimer.cancel();
    }
}
