package server_store;

import common.PlayerToken;
import common.RemoteMethodCall;
import server.ServerLogger;
import sts.Action;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;

/**
 * Created by giorgiopea on 10/03/17.
 *
 */
public class PubSubHandler extends Thread implements Observer {

    // The socket associated to the handler
    private Socket socket;
    // A queue of messages to send to the subscriber
    private ConcurrentLinkedQueue<RemoteMethodCall> buffer;
    private PlayerToken playerToken;
    // The object output stream used to perform the remote method call on the
    // subscriber
    private ObjectOutputStream objectOutputStream;

    /**
     * Constructs a subscriber handler from the socket used to perform remote
     * method calls on the subscriber. An empty queue of remote method calls for
     * the handler is automatically created as well.
     *
     * @param socket
     *            the socket used perform remote method calls on the subscriber
     * @throws IOException
     */
    public PubSubHandler(Socket socket, PlayerToken playerToken) throws IOException {
        this.playerToken = playerToken;
        this.socket = socket;
        this.buffer = new ConcurrentLinkedQueue<RemoteMethodCall>();
        this.objectOutputStream = new ObjectOutputStream(this.socket.getOutputStream());
        this.objectOutputStream.flush();
    }

    /**
     * Performs a remote method call on the subscriber
     *
     * @param remoteMethodCall
     *            the remote method call to be performed on the subscriber
     * @throws IOException
     */
    private void perform(RemoteMethodCall remoteMethodCall) throws IOException {
        this.objectOutputStream.writeObject(remoteMethodCall);
        this.objectOutputStream.flush();
    }




    /**
     * Runs the thread defined in this class. The thread waits until the
     * handler's associated queue of remote method calls has a remote method
     * call, then wakes up and performs the remote method call.
     */
    public void run() {
        while (true) {
            RemoteMethodCall remotheMethodCall = buffer.poll();
            if (remotheMethodCall != null)
                try {
                    this.perform(remotheMethodCall);
                } catch (IOException e1) {
                    ServerLogger
                            .getLogger()
                            .log(Level.SEVERE,
                                    "Could not perform action | SocketSubscriberHandler",
                                    e1);
                }
            else {
                try {
                    // If there are no incoming remote method calls the thread
                    // waits
                    synchronized (buffer) {
                        buffer.wait();
                    }
                } catch (InterruptedException e) {
                    ServerLogger.getLogger().log(Level.SEVERE,
                            "Thread interrupted | SocketSubscriberHandler", e);
                }
            }
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        Action lastAction = (Action) arg;
        String prefix = lastAction.type.substring(0, lastAction.type.indexOf("_"));
        if(lastAction.type.equals("@GAME")){

        }
    }
}