package server_store;

import client.*;
import server_store.PubSubHandler;
import sts.Action;
import sts.ActionFactory;
import sts.Store;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by giorgiopea on 11/03/17.
 */
public class CommunicationComponent implements Observer {

    private final Store store;
    private final ActionFactory actionFactory;
    private final int tcp_port;
    private ServerSocket serverSocket;
    private ExecutorService reqRespThreadPool;
    private ExecutorService pubSubThreadPool;
    private static String context = "@COMMUNICATION";


    public CommunicationComponent(int tcp_port) {
        this.store = Store.getInstance();
        this.tcp_port = tcp_port;
        this.actionFactory = ActionFactory.getInstance();
        this.reqRespThreadPool = Executors.newCachedThreadPool();
        this.pubSubThreadPool = Executors.newCachedThreadPool();
    }

    private void setConnection(){
        this.store.dispatchAction(this.actionFactory.getAction(context+"_SET_CONNECTION",this.tcp_port));
    }
    private void initConnection() throws IOException {
        this.serverSocket = new ServerSocket(this.tcp_port);
        Socket socket;
        while(true){
            socket = this.serverSocket.accept();
            store.dispatchAction(this.actionFactory.getAction(context+"_ADD_SOCKET", socket));
            this.reqRespThreadPool.submit(new ReqRespHandler(socket));
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        Action lastAction = (Action) arg;
        Socket socket = (Socket) lastAction.payload;
        if(lastAction.type.equals("@GAMES_ADD_PLAYER_TO_GAME")){
            store.dispatchAction(this.actionFactory.getAction("@COMMUNICATION_MOVE_SOCKET_TO_PUBSUB",socket));
            try {
                this.pubSubThreadPool.submit(new PubSubHandler(socket));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
