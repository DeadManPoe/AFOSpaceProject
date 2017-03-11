package server_store;

import contracts.StateChangedNotification;
import sts.ActionFactory;
import sts.Store;

import java.io.IOException;
import java.net.Socket;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by giorgiopea on 10/03/17.
 *
 */
public class GamesComponent implements Observer {

    private final ExecutorService reqRespHandlersThreadPool;
    private final ExecutorService pubSubHandlersThreadPool;
    private final Store store;
    private final String context = "@GAMES";

    private static GamesComponent instance = new GamesComponent();

    public static GamesComponent getInstance(){
        return instance;
    }
    private GamesComponent() {
        this.store = Store.getInstance();
        this.pubSubHandlersThreadPool = Executors.newCachedThreadPool();
        this.reqRespHandlersThreadPool = Executors.newCachedThreadPool();
    }

    @Override
    public void update(Observable o, Object arg) {
        StateChangedNotification stateChangedNotification = (StateChangedNotification) arg;
        if(stateChangedNotification.lastAction.type.equals(this.context+"_ADD_PLAYER_TO_GAME")){
            List<Socket> sockets = (List<Socket>) this.store.getState().get("@COMMUNICATION_REQRESPONSE_SOCKETS");
            Socket socket = sockets.get(sockets.size()-1);
            try {
                this.pubSubHandlersThreadPool.submit(new PubSubHandler(socket));
                this.store.dispatchAction(ActionFactory.getInstance().getAction("@COMMUNICATION_MOVE_SOCKET_TO_SUPSUB",socket));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if(stateChangedNotification.lastAction.type.equals("@COMMUNICATION_ADD_SOCKET")){
            List<Socket> sockets = (List<Socket>) this.store.getState().get("@COMMUNICATION_ADD_SOCKET");
            this.reqRespHandlersThreadPool.submit(new ReqRespHandler(sockets.get(sockets.size()-1)));
        }
    }
}
