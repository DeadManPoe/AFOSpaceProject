package client_store;

import client.PubSubHandler;
import common.*;
import common.GameMap;
import common.Player;
import server_store.State;

import java.io.Serializable;
import java.util.List;

/**
 * Created by giorgiopea on 24/03/17.
 */
public class ClientState extends State implements Serializable {

    private int tcpPort;
    private PubSubHandler currentPubSubHandler;
    private List<GamePublicData> availableGames;
    private Player player;
    private String host;
    private GameMap gameMap;
    private boolean isGameStarted;
    private boolean isMyTurn;
    private boolean startableGame;
    private RRClientNotification currentReqRespNotification;
    private boolean askLights;
    private boolean askAttack;
    private StatefulTimer gamePollingTimer;
    private String lastChatMessage;
    private PSClientNotification currentPubSubNotification;
    private long delayReturnToGameList;
    private boolean connectionActive;
    private SectorCard drawnSectorCard;
    private boolean aliensHaveWon;
    private boolean humansHaveWon;

    public ClientState() {
        this.tcpPort = 29999;
        this.host = "localhost";
        this.delayReturnToGameList = 10000;
    }

    public int getTcpPort() {
        return tcpPort;
    }

    public void setTcpPort(int tcpPort) {
        this.tcpPort = tcpPort;
    }

    public PubSubHandler getCurrentPubSubHandler() {
        return currentPubSubHandler;
    }

    public void setCurrentPubSubHandler(PubSubHandler currentPubSubHandler) {
        this.currentPubSubHandler = currentPubSubHandler;
    }

    public List<GamePublicData> getAvailableGames() {
        return availableGames;
    }

    public void setAvailableGames(List<GamePublicData> availableGames) {
        this.availableGames = availableGames;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public GameMap getGameMap() {
        return gameMap;
    }

    public void setGameMap(GameMap gameMap) {
        this.gameMap = gameMap;
    }

    public boolean isGameStarted() {
        return isGameStarted;
    }

    public void setGameStarted(boolean gameStarted) {
        isGameStarted = gameStarted;
    }

    public boolean isMyTurn() {
        return isMyTurn;
    }

    public void setMyTurn(boolean myTurn) {
        isMyTurn = myTurn;
    }

    public boolean isStartableGame() {
        return startableGame;
    }

    public void setStartableGame(boolean startableGame) {
        this.startableGame = startableGame;
    }

    public RRClientNotification getCurrentReqRespNotification() {
        return currentReqRespNotification;
    }

    public void setCurrentReqRespNotification(RRClientNotification currentReqRespNotification) {
        this.currentReqRespNotification = currentReqRespNotification;
    }

    public boolean isAskLights() {
        return askLights;
    }

    public void setAskLights(boolean askLights) {
        this.askLights = askLights;
    }

    public boolean isAskAttack() {
        return askAttack;
    }

    public void setAskAttack(boolean askAttack) {
        this.askAttack = askAttack;
    }

    public StatefulTimer getGamePollingTimer() {
        return gamePollingTimer;
    }

    public void setGamePollingTimer(StatefulTimer gamePollingTimer) {
        this.gamePollingTimer = gamePollingTimer;
    }

    public String getLastChatMessage() {
        return lastChatMessage;
    }

    public void setLastChatMessage(String lastChatMessage) {
        this.lastChatMessage = lastChatMessage;
    }

    public PSClientNotification getCurrentPubSubNotification() {
        return currentPubSubNotification;
    }

    public void setCurrentPubSubNotification(PSClientNotification currentPubSubNotification) {
        this.currentPubSubNotification = currentPubSubNotification;
    }

    public long getDelayReturnToGameList() {
        return delayReturnToGameList;
    }

    public void setDelayReturnToGameList(long delayReturnToGameList) {
        this.delayReturnToGameList = delayReturnToGameList;
    }

    public boolean isConnectionActive() {
        return connectionActive;
    }

    public void setConnectionActive(boolean connectionActive) {
        this.connectionActive = connectionActive;
    }

    public SectorCard getDrawnSectorCard() {
        return drawnSectorCard;
    }

    public void setDrawnSectorCard(SectorCard drawnSectorCard) {
        this.drawnSectorCard = drawnSectorCard;
    }

    public boolean isAliensHaveWon() {
        return aliensHaveWon;
    }

    public void setAliensHaveWon(boolean aliensHaveWon) {
        this.aliensHaveWon = aliensHaveWon;
    }

    public boolean isHumansHaveWon() {
        return humansHaveWon;
    }

    public void setHumansHaveWon(boolean humansHaveWon) {
        this.humansHaveWon = humansHaveWon;
    }
}
