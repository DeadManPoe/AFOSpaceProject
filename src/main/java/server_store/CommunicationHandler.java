package server_store;

import store_actions.*;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by giorgiopea on 11/03/17.
 *
 */
public class CommunicationHandler implements Observer {

    private final ServerStore serverStore;
    private final Integer tcp_port;
    private final ExecutorService reqRespThreadPool;
    private final ExecutorService pubSubThreadPool;


    public CommunicationHandler(int tcp_port) {
        this.serverStore = ServerStore.getInstance();
        this.tcp_port = tcp_port;
        this.reqRespThreadPool = Executors.newCachedThreadPool();
        this.pubSubThreadPool = Executors.newCachedThreadPool();
        this.serverStore.observeState(this);
    }

    private void setConnection(){
        this.serverStore.dispatchAction(new CommunicationSetTcpPortAction(this.tcp_port));
    }
    private void initConnection() throws IOException {
        ServerSocket serverSocket = new ServerSocket(this.tcp_port);
        Socket socket;
        while(true){
            socket = serverSocket.accept();
            ReqRespHandler reqRespHandler = new ReqRespHandler(socket);
            this.serverStore.dispatchAction(new CommunicationAddReqRespHandlerAction(reqRespHandler));
            this.reqRespThreadPool.submit(reqRespHandler);
        }
    }
    public void runServer() throws IOException {
        this.setConnection();
        this.initConnection();
    }

    @Override
    public void update(Observable o, Object arg) {
        StoreAction lastAction = (StoreAction) arg;
        if(lastAction.type.equals("@COMMUNICATION_ADD_PUBSUB_HANDLER")){
            CommunicationAddPubSubHandlerAction castedAction = (CommunicationAddPubSubHandlerAction) lastAction;
            this.pubSubThreadPool.submit(castedAction.getPayload());
        }
    }
}
