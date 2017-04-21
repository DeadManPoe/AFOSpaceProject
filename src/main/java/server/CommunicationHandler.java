package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by giorgiopea on 19/04/17.
 */
public class CommunicationHandler {
    private final ExecutorService reqRespThreadPool;
    private final ExecutorService pubSubThreadPool;
    private boolean listeningFlag;

    private static final CommunicationHandler instance = new CommunicationHandler();

    public static CommunicationHandler getInstance(){
        return instance;
    }

    private CommunicationHandler() {
        this.reqRespThreadPool = Executors.newCachedThreadPool();
        this.pubSubThreadPool = Executors.newCachedThreadPool();
        this.listeningFlag = true;
    }

    public void init(int tcpPort) throws IOException {
        ServerSocket serverSocket = new ServerSocket(tcpPort);
        Socket socket;
        while(this.listeningFlag){
            socket = serverSocket.accept();
            ReqRespHandler reqRespHandler = new ReqRespHandler(socket,this);
            this.reqRespThreadPool.submit(reqRespHandler);
        }
    }

    public void setListeningFlag(boolean listeningFlag) {
        this.listeningFlag = listeningFlag;
    }

    public void addPubSubHandler(PubSubHandler pubSubHandler){
        this.pubSubThreadPool.submit(pubSubHandler);
    }
}
