package client_store_actions;

import common.GamePublicData;
import server_store.StoreAction;

import java.util.ArrayList;

/**
 * Created by giorgiopea on 25/03/17.
 */
public class ClientSetAvailableGamesAction extends StoreAction {

    public ArrayList<GamePublicData> availableGames;

    public ClientSetAvailableGamesAction(ArrayList<GamePublicData> availableGames) {
        this.type = "@CLIENT_SET_AVAILABLE_GAMES";
        this.availableGames = availableGames;
    }
}