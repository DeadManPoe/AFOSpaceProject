package server_store;

import common.PlayerToken;
import common.RemoteMethodCall;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;

/**
 * Created by giorgiopea on 10/03/17.
 *
 */
public class PubSubHandler extends Thread {

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
     * @param outputStream
     *            the socket used perform remote method calls on the subscriber
     * @param playerToken The token that identifies a player along with the game is playing
     *
     * @throws IOException
     */
    public PubSubHandler(ObjectOutputStream outputStream, PlayerToken playerToken) throws IOException {
        this.playerToken = playerToken;
        this.buffer = new ConcurrentLinkedQueue<>();
        this.objectOutputStream = outputStream;
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
                }
            else {
                try {
                    // If there are no incoming remote method calls the thread
                    // waits
                    synchronized (buffer) {
                        buffer.wait();
                    }
                } catch (InterruptedException e) {

                }
            }
        }
    }

    public PlayerToken getPlayerToken() {
        return playerToken;
    }

    public void queueNotification(RemoteMethodCall remoteMethodCall) {
        buffer.add(remoteMethodCall);
        synchronized (buffer) {
            buffer.notify();
        }
    }
}