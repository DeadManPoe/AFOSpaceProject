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
    private final Integer tcp_port;
    private final ExecutorService reqRespThreadPool;
    private final ExecutorService pubSubThreadPool;


    public CommunicationHandler(int tcp_port) {
        this.tcp_port = tcp_port;
        this.reqRespThreadPool = Executors.newCachedThreadPool();
        this.pubSubThreadPool = Executors.newCachedThreadPool();
    }

    public void init() throws IOException {
        ServerSocket serverSocket = new ServerSocket(this.tcp_port);
        Socket socket;
        while(true){
            socket = serverSocket.accept();
            ReqRespHandler reqRespHandler = new ReqRespHandler(socket);
            this.reqRespThreadPool.submit(reqRespHandler);
        }
    }
}
