package client_store_actions;

import server_store.StoreAction;

/**
 * Created by giorgiopea on 25/03/17.
 *
 */
public class ClientEndTurnAction extends StoreAction {

    public final boolean isActionServerValidated;

    public ClientEndTurnAction(boolean isActionServerValidated) {
        super("@CLIENT_END_TURN");
        this.isActionServerValidated = isActionServerValidated;
    }
}
