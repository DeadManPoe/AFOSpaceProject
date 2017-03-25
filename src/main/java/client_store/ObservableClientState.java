package client_store;

import server_store.ServerState;
import server_store.State;
import server_store.StoreAction;

import java.util.Observable;

/**
 * Created by giorgiopea on 24/03/17.
 */
public class ObservableClientState extends Observable {
    private ClientState clientState;

    public ObservableClientState(ClientState serverState) {
        this.clientState = serverState;
    }

    public ClientState getClientState() {
        return clientState;
    }

    public void setClientState(State clientState, StoreAction lastAction) {
        this.clientState = (ClientState) clientState;
        this.setChanged();
        this.notifyObservers(lastAction);
    }

    @Override
    public String toString() {
        return clientState.toString();
    }
}
