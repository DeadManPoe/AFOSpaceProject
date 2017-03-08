package client;

import java.io.IOException;

/**
 * Represents a factory of remote data exchanges between the client and the
 * server. These data exchanges use a socket based communication
 * 
 * @see RemoteDataExchangeFactory
 * @author Andrea Sessa
 * @author Giorgio Pea
 */
public class SocketRemoteDataExchangeFactory extends RemoteDataExchangeFactory {

	/**
	 * Constructs a factory of remote data exchanges between the client and the
	 * server using a socket based communication. This factory is constructed
	 * from the client it refers to
	 * 
	 * @param client
	 *            the client the factory refers to
	 */
	public SocketRemoteDataExchangeFactory(Client client) {
		super(client);
	}

	/**
	 * @see RemoteDataExchangeFactory#make
	 */
	@Override
	public RemoteDataExchange make() throws IOException {
		return new SocketRemoteDataExchange(client);
	}

}
