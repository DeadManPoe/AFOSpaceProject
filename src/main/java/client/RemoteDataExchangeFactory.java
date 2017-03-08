package client;

import java.io.IOException;
import java.rmi.RemoteException;

/**
 * Represents a generic factory of data exchanges between the client and the
 * server
 * 
 * @see RemoteDataExchange
 * @author Andrea Sessa
 * @author Giorgio Pea
 */
public abstract class RemoteDataExchangeFactory {
	// The client the factory refers to
	protected Client client;

	/**
	 * Constructs a generic factory of remote data exchanges between the client
	 * and the server. This factory is constructed from the client it refers to
	 * 
	 * @param client
	 *            the client the factory refers to
	 */
	public RemoteDataExchangeFactory(Client client) {
		this.client = client;
	}

	/**
	 * Makes a remote data exchange
	 * 
	 * @see RemoteDataExchange
	 * @return the remote data exchange made
	 * @throws RemoteException
	 *             signals an error in the creation of the remote data exchange
	 * @throws IOException
	 *             signals an error in the creation of the remote data exchange
	 */
	public abstract RemoteDataExchange make() throws RemoteException,
			IOException;
}
