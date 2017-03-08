package client;

import java.rmi.RemoteException;

/**
 * Represents a factory of data exchanges between the client and the server.
 * These data exchanges use a rmi based communication
 * 
 * @see RemoteDataExchangeFactory
 * @author Andrea Sessa
 * @author Giorgio Pea
 */
public class RmiRemoteDataExchangeFactory extends RemoteDataExchangeFactory {

	/**
	 * Constructs a factory of remote data exchanges between the client and the
	 * server using a rmi based communication. This factory is constructed from
	 * the client it refers to
	 * 
	 * @param client
	 *            the client the factory refers to
	 */
	public RmiRemoteDataExchangeFactory(Client client) {
		super(client);
	}

	/**
	 * @see RemoteDataExchangeFactory#make
	 */
	@Override
	public RemoteDataExchange make() throws RemoteException {
		return new RmiRemoteDataExchange(client);
	}

}
