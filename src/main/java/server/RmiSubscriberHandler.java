package server;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;

import common.RemoteMethodCall;
import client.ClientRemoteServicesInterface;

/**
 * Represents a subscriber handler based on rmi in the logic of the pub/sub
 * pattern. It observes a topic for changes and notifies its associated
 * subscriber through a remote method call.
 * 
 * @see SubscriberHandler
 * @author Andrea Sessa
 * @author Giorgio Pea
 */
public class RmiSubscriberHandler implements SubscriberHandler {
	// The services offered by the client to the server using rmi
	private ClientRemoteServicesInterface clientServices;
	// A queue of messages to send to the subscriber
	private ConcurrentLinkedQueue<RemoteMethodCall> buffer;

	/**
	 * Constructs a subscriber handler based on rmi from the services offered by
	 * the subscriber(the client's stub). These services are used to send
	 * messages to the subscriber. An empty queue of messages for the handler is
	 * automatically created as well.
	 * 
	 * @param clientServices
	 *            the services the subscriber offers via rmi to communicate
	 */
	public RmiSubscriberHandler(ClientRemoteServicesInterface clientServices) {
		this.clientServices = clientServices;
		this.buffer = new ConcurrentLinkedQueue<RemoteMethodCall>();
	}

	/**
	 * Performs a remote method call on the subscriber
	 * 
	 * @param remoteMethodCall
	 *            the remote method call to be performed on the subscriber
	 * @throws IOException
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	private void perform(RemoteMethodCall remoteMethodCall) throws IOException,
			IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException {
		// Getting the name of the method
		String methodName = remoteMethodCall.getMethodName();
		// Getting the params the method should be called with
		ArrayList<Object> parameters = remoteMethodCall.getMethodParameters();
		// Getting the types of the params the method should be called with
		Class<?>[] parametersClasses = new Class[parameters.size()];
		for (int i = 0; i < parameters.size(); i++) {
			if (parameters.get(i).getClass().getName().contains("action")
					|| parameters.get(i).getClass().getName()
							.contains("ClientNotification")) {
				parametersClasses[i] = parameters.get(i).getClass()
						.getSuperclass();
			} else {
				parametersClasses[i] = parameters.get(i).getClass();
			}

		}
		// Invoking the method on the subscriber
		clientServices.getClass()
				.getDeclaredMethod(methodName, parametersClasses)
				.invoke(clientServices, parameters.toArray());
	}

	/**
	 * Updates the subscriber handler with a remote method call. The remote
	 * method call is appended to the handler's queue of remote method calls and
	 * is performed on the subscriber
	 * 
	 * @see java.util.Observable#update
	 */
	@Override
	public void update(Observable o, Object arg) {
		this.buffer.add((RemoteMethodCall) arg);
		synchronized (buffer) {
			buffer.notify();
		}

	}

	/**
	 * Runs the thread defined in this class. The thread waits until the
	 * handler's associated queue of remote method calls has a remote method
	 * call, then wakes up and performs the remote method call.
	 */
	public void run() {
		while (true) {
			RemoteMethodCall remoteMethodCall = buffer.poll();
			if (remoteMethodCall != null)
				try {
					this.perform(remoteMethodCall);
				} catch (IOException | IllegalAccessException
						| IllegalArgumentException | InvocationTargetException
						| NoSuchMethodException | SecurityException e1) {
					ServerLogger.getLogger().log(Level.SEVERE,
							"Could not perform action | RmiSubscriberHandler",
							e1);
				}
			else {
				try {
					// Waiting until a remote method call arrives
					synchronized (buffer) {
						buffer.wait();
					}
				} catch (InterruptedException e) {
					ServerLogger.getLogger().log(Level.SEVERE,
							"Thread interrupted | RmiSubscriberHandler", e);
				}
			}
		}
	}

}
