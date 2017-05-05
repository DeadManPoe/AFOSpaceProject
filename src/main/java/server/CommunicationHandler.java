package server;

import server_store.ServerState;
import server_store.ServerStore;
import server_store.StoreAction;
import server_store_actions.*;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Manages the network communication between the client and the server by spawning threads
 * as necessary.
 */
public class CommunicationHandler implements Observer {

    private final ServerStore serverStore;
    private final ExecutorService reqRespThreadPool;
    private final ExecutorService pubSubThreadPool;

    private static final CommunicationHandler instance = new CommunicationHandler();

    public static CommunicationHandler getInstance(){
        return instance;
    }

    private CommunicationHandler() {
        this.serverStore = ServerStore.getInstance();
        this.reqRespThreadPool = Executors.newCachedThreadPool();
        this.pubSubThreadPool = Executors.newCachedThreadPool();
        this.serverStore.observeState(this);
    }
    /**
     * Inits listening on the wire on the given port and start spawning threads to manage
     * incoming requests.
     *
     * @throws IOException Networking problem.
     */
    public void init() throws IOException {
        ServerState serverState = this.serverStore.getState();
        ServerSocket serverSocket = new ServerSocket(serverState.getTcpPort());
        Socket socket;
        while(serverState.isServerListening()){
            socket = serverSocket.accept();
            ReqRespHandler reqRespHandler = new ReqRespHandler(socket);
            this.serverStore.dispatchAction(new CommunicationAddReqRespHandlerAction(reqRespHandler));
            this.reqRespThreadPool.submit(reqRespHandler);
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        StoreAction lastAction = (StoreAction) arg;
        if(lastAction.type.equals("@COMMUNICATION_ADD_PUBSUB_HANDLER")){
            CommunicationAddPubSubHandlerAction castedAction = (CommunicationAddPubSubHandlerAction) lastAction;
            this.pubSubThreadPool.submit(castedAction.getPubSubHandler());
        }
    }
}
