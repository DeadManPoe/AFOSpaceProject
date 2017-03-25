package client_store_actions;

import common.GamePublicData;
import server_store.StoreAction;

import java.util.ArrayList;

/**
 * Created by giorgiopea on 25/03/17.
 */
public class ClientSetAvGamesAction extends StoreAction {

    public ArrayList<GamePublicData> payload;

    public ClientSetAvGamesAction(ArrayList<GamePublicData> avGames) {
        this.type = "@CLIENT_SET_AV_GAMES";
        this.payload = avGames;
    }
}
