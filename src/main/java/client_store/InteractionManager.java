package client_store;

import client_store_actions.ClientSetGameMapAction;
import client_store_actions.ClientStartGameAction;

/**
 * Created by giorgiopea on 25/03/17.
 */
public class InteractionManager {

    private static final InteractionManager instance = new InteractionManager();
    private final ClientStore clientStore;

    public static InteractionManager getInstance() {
        return instance;
    }

    private InteractionManager(){
        clientStore = ClientStore.getInstance();
    }


    public void setMapAndStartGame(String mapName){
        clientStore.dispatchAction(new ClientSetGameMapAction(mapName));
        clientStore.dispatchAction(new ClientStartGameAction());
    }

}
