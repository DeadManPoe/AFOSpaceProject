package clientClassTests;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.rmi.RemoteException;
import java.util.ArrayList;

import factories.GalileiGameMapFactory;
import it.polimi.ingsw.cg_19.GameMap;
import it.polimi.ingsw.cg_19.PlayerType;

import org.junit.Test;

import common.Card;
import common.ClientNotification;
import common.Coordinate;
import common.GamePublicData;
import common.PlayerToken;
import common.RRClientNotification;
import common.RemoteMethodCall;
import common.Sector;
import common.SectorType;
import common.SilenceSectorCard;

public class ClientRemoteServicesTests {
	private ClientRemoteServices clientServices;
	private Client client;
	@Test
	public void sendTokenTest() throws RemoteException, IOException {
		client = new Client(new ClientConnection(21323,"sdasd","sddsad"));
		clientServices = new ClientRemoteServices(client);
		PlayerToken token = new PlayerToken(PlayerType.ALIEN,null);
		clientServices.sendToken(token);
		assertEquals(token,client.getToken());
		token = new PlayerToken(PlayerType.HUMAN,null);
		clientServices.sendToken(token);
		assertEquals(token,client.getToken());
	}
	@Test
	public void sendAvailableGamesTest() throws RemoteException, IOException {
		client = new Client(new ClientConnection(21323,"sdasd","sddsad"));
		clientServices = new ClientRemoteServices(client);
		ArrayList<GamePublicData> gamesList = new ArrayList<GamePublicData>();
		gamesList.add(new GamePublicData(1,"game1"));
		clientServices.sendAvailableGames(gamesList);
		assertTrue(client.getAvailableGames().containsAll(gamesList));
		assertEquals(client.getAvailableGames().size(),gamesList.size());
		gamesList.clear();
		clientServices.sendAvailableGames(gamesList);
		assertTrue(client.getAvailableGames().containsAll(gamesList));
		assertEquals(client.getAvailableGames().size(),gamesList.size());
	}
	@Test
	public void allowTurnTest() throws RemoteException, IOException {
		client = new Client(new ClientConnection(21323,"sdasd","sddsad"));
		clientServices = new ClientRemoteServices(client);
		PlayerToken token = new PlayerToken(PlayerType.ALIEN,null);
		clientServices.sendToken(token);
		//clientServices.allowTurn(token);
		assertTrue(client.getIsMyTurn());
		client.setIsMyTurn(false);
		token = new PlayerToken(PlayerType.HUMAN,null);
		//clientServices.allowTurn(token);
		assertFalse(client.getIsMyTurn());
		client.setIsMyTurn(false);
		token = new PlayerToken(PlayerType.ALIEN,null);
		//clientServices.allowTurn(token);
		assertFalse(client.getIsMyTurn());
	}
	@Test
	public void sendNotificationTest() throws RemoteException, IOException {
		client = new Client(new ClientConnection(21323,"sdasd","sddsad"));
		clientServices = new ClientRemoteServices(client);
		ClientNotification clientNotification = new RRClientNotification();
		clientNotification.setMessage("dsadsad");
		Sector sector = new Sector(new Coordinate('a',1),SectorType.HUMAN);
		Card card = new SilenceSectorCard();
		((RRClientNotification) clientNotification).setActionResult(true);;
		((RRClientNotification) clientNotification).addSector(sector);
		((RRClientNotification) clientNotification).addCard(card);
		clientServices.sendNotification(clientNotification);
		assertEquals(client.getCurrentNotification().getMessage(),"dsadsad");
		assertTrue(client.getCurrentNotification().getActionResult());
		assertTrue(client.getCurrentNotification().getDrawnCards().contains(card));
		assertEquals(client.getCurrentNotification().getDrawnCards().size(),1);
		assertTrue(client.getCurrentNotification().getLightedSectors().contains(sector));
		assertEquals(client.getCurrentNotification().getLightedSectors().size(),1);
	}
	@Test
	public void processRemoteInvocationTest() throws RemoteException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, SecurityException{
		client = new Client(new ClientConnection(21323,"sdasd","sddsad"));
		clientServices = new ClientRemoteServices(client);
		PlayerToken token = new PlayerToken(PlayerType.ALIEN,null);
		ArrayList<Object> parameters = new ArrayList<Object>();
		parameters.add(token);
		clientServices.processRemoteInvocation(new RemoteMethodCall("sendToken",parameters));
		assertEquals(client.getToken(),token);
		ArrayList<GamePublicData> gamesList = new ArrayList<GamePublicData>();
		gamesList.add(new GamePublicData(1,"game1"));
		parameters.clear();
		parameters.add(gamesList);
		clientServices.processRemoteInvocation(new RemoteMethodCall("sendAvailableGames",parameters));
		assertEquals(client.getAvailableGames().size(),1);
		assertTrue(client.getAvailableGames().containsAll(gamesList));
	}
	@Test
	public void sendMapTest() throws IOException{
		client = new Client(new ClientConnection(21323,"sdasd","sddsad"));
		clientServices = new ClientRemoteServices(client);
		PlayerToken token = new PlayerToken(PlayerType.ALIEN,null);
		clientServices.sendToken(token);
		GalileiGameMapFactory factory = new GalileiGameMapFactory();
		GameMap map = factory.makeMap();
		clientServices.sendMap("GALILEI", token);
		assertTrue(client.getIsMyTurn());
		assertEquals(client.getGameMap().getGraph().vertexSet(),map.getGraph().vertexSet());
		assertEquals("GALILEI",client.getGameMap().getName());
	}

}
