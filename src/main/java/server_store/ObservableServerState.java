package server_store;

import sts.Action;

import java.util.Observable;

/**
 * Created by giorgiopea on 11/03/17.
 */
public class ObservableServerState extends Observable {

    private ServerState serverState;

    public ObservableServerState(ServerState serverState) {
        this.serverState = serverState;
    }

    public ServerState getServerState() {
        return serverState;
    }

    public void setServerState(ServerState serverState, Action lastAction) {
        this.serverState = serverState;
        this.setChanged();
        this.notifyObservers(lastAction);
    }
}
