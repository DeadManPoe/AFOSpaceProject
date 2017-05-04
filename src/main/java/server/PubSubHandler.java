package server;

import common.PlayerToken;
import common.RemoteMethodCall;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Manages a persistent connection with a client in the logic of the publisher/subscriber pattern
 */
public class PubSubHandler extends Thread {

    // The socket associated to the handler
    private final Socket socket;
    // A queue of messages to send to the subscriber
    private final ConcurrentLinkedQueue<RemoteMethodCall> buffer;
    private final PlayerToken playerToken;
    // The object output stream used to perform the remote method call on the
    // subscriber
    private ObjectOutputStream objectOutputStream;
    private boolean runningFlag;

    /**
     * Constructs a subscriber handler from the socket used to perform remote
     * method calls on the subscriber. An empty queue of remote method calls for
     * the handler is automatically created as well.
     *
     * @param socket The socket used to perform remote method calls on the subscriber.
     *
     * @param outputStream The output stream used to perform remote method calls on the subscriber.
     *
     * @param playerToken The token that identifies a player along with the game is playing.
     *
     */
    public PubSubHandler(Socket socket, ObjectOutputStream outputStream, PlayerToken playerToken)  {
        this.socket = socket;
        this.playerToken = playerToken;
        this.buffer = new ConcurrentLinkedQueue<>();
        this.objectOutputStream = outputStream;
        this.runningFlag = true;
    }

    public void setRunningFlag(boolean runningFlag) {
        this.runningFlag = runningFlag;
    }

    /**
     * Performs a remote method call on the subscriber
     *
     * @param remoteMethodCall
     *            The remote method call to be performed on the subscriber
     * @throws IOException Networking problem.
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
        while (this.runningFlag) {
            RemoteMethodCall remoteMethodCall = buffer.poll();
            if (remoteMethodCall != null)
                try {
                    this.perform(remoteMethodCall);
                } catch (IOException e) {
                    this.runningFlag = false;
                    try {
                        this.objectOutputStream.close();
                        this.socket.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    e.printStackTrace();
                }
            else {
                try {
                    // If there are no incoming remote method calls the thread
                    // waits
                    synchronized (buffer) {
                        buffer.wait();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        try {
            this.objectOutputStream.close();
            this.socket.close();
        } catch (IOException e) {
            e.printStackTrace();
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