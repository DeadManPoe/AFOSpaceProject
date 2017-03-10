package server_store;

import sts.Store;

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
public class CommunicationComponent implements Observer {

    private final ExecutorService reqRespHandlersThreadPool;
    private final ExecutorService pubSubHandlersThreadPool;
    private final Store store;
    private final String context = "COMMUNICATION";

    private static CommunicationComponent instance = new CommunicationComponent();

    public static CommunicationComponent getInstance(){
        return instance;
    }
    private CommunicationComponent() {
        this.store = Store.getInstance();
        this.pubSubHandlersThreadPool = Executors.newCachedThreadPool();
        this.reqRespHandlersThreadPool = Executors.newCachedThreadPool();
    }

    @Override
    public void update(Observable o, Object arg) {
        List<String> changedKeys = (List<String>) arg;
        if(changedKeys.contains("@"+this.context+"_"+CommunicationComponentObservedKeys.REQRESP_SOCKETS.toString())){
            List<Socket> sockets = (List<Socket>) this.store.getState().get("@COMMUNICATION_REQRESPONSE_SOCKETS");
            this.reqRespHandlersThreadPool.submit(new ReqRespHandler(sockets.get(sockets.size()-1)));
        }
        else if(changedKeys.contains("@"+this.context+"_"+CommunicationComponentObservedKeys.PUBSUB_SOCKETS.toString())){
            List<Socket> sockets = (List<Socket>) this.store.getState().get("@COMMUNICATION_PUBSUB_SOCKETS");
            this.pubSubHandlersThreadPool.submit(new ReqRespHandler(sockets.get(sockets.size()-1)));
        }
    }
}
