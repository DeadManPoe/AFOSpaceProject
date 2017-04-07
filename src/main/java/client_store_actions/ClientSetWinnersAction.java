package client_store_actions;

import server_store.StoreAction;

/**
 * Created by giorgiopea on 07/04/17.
 */
public class ClientSetWinnersAction extends StoreAction {


    public final boolean aliensHaveWon;
    public final boolean humansHaveWon;

    public ClientSetWinnersAction(boolean aliensHaveWon, boolean humansHaveWon) {
        this.type = "@CLIENT_SET_WINNNERS";
        this.aliensHaveWon = aliensHaveWon;
        this.humansHaveWon = humansHaveWon;
    }
}
