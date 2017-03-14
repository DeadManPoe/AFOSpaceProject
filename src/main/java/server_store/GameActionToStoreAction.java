package server_store;

/**
 * Created by giorgiopea on 13/03/17.
 */
public class GameActionToStoreAction {

    private static GameActionToStoreAction instance = new GameActionToStoreAction();

    public static GameActionToStoreAction getInstance(){
        return instance;
    }

    private GameActionToStoreAction(){

    }
}
