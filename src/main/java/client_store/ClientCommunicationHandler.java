package client_store;

import server_store.ReqRespHandler;
import server_store.ServerStore;
import server_store.StoreAction;
import store_actions.CommunicationAddPubSubHandlerAction;
import store_actions.CommunicationAddReqRespHandlerAction;
import store_actions.CommunicationSetTcpPortAction;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by giorgiopea on 24/03/17.
 */
public class ClientCommunicationHandler implements Observer {
    private final ClientStore clientStore;
    private final Integer tcp_port;
    private final ExecutorService pubSubThreadPool;


    public ClientCommunicationHandler(int tcp_port) {
        this.tcp_port = tcp_port;
        this.clientStore = ClientStore.getInstance();
        this.pubSubThreadPool = Executors.newCachedThreadPool();
    }

    private void setConnection(){
        this.clientStore.dispatchAction(new CommunicationSetTcpPortAction(this.tcp_port));
    }
    public void runServer() throws IOException {
        this.setConnection();
        this.initConnection();
    }

    @Override
    public void update(Observable o, Object arg) {
        StoreAction lastAction = (StoreAction) arg;
        if(lastAction.getType().equals("@COMMUNICATION_ADD_PUBSUB_HANDLER")){
            CommunicationAddPubSubHandlerAction castedAction = (CommunicationAddPubSubHandlerAction) lastAction;
            this.pubSubThreadPool.submit(castedAction.getPayload());
        }
    }
}
