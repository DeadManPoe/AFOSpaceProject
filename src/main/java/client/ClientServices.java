package client;

import common.*;
import factories.FermiGameMapFactory;
import factories.GalileiGameMapFactory;
import factories.GalvaniGameMapFactory;
import factories.GameMapFactory;
import it.polimi.ingsw.cg_19.GameMap;
import it.polimi.ingsw.cg_19.PlayerType;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by giorgiopea on 25/04/17.
 *
 */
public class ClientServices {
    private final Client client;
    private final CommunicationHandler communicationHandler;
    private final ServerMethodsNameProvider serverMethodsNameProvider;
    private GuiManager guiInteractionManager;
    private static ClientServices instance;
    private List<GamePublicData> availableGames;
    private RRClientNotification currentRrNotification;
    private PSClientNotification currentPsNotification;

    public static ClientServices getInstance(){
        if (instance == null){
            instance = new ClientServices();
        }
        return instance;
    }

    private ClientServices(){
        this.client = Client.getInstance();
        this.communicationHandler = CommunicationHandler.getInstance();
        this.serverMethodsNameProvider = ServerMethodsNameProvider.getInstance();
    }
    public void initWithGuiManager(GuiManager guiManager){
        this.guiInteractionManager = guiManager;
    }
    /**
     * Requests to the server the creation of a new game with a given map.
     *
     * @param gameMapName The name of the map of the game to be created.
     * @param playerName  The name of the client in the game.
     */
    public void joinNewGame(String gameMapName, String playerName) {
        ArrayList<Object> parameters = new ArrayList<>();
        parameters.add(gameMapName);
        parameters.add(playerName);
        try {
            RemoteMethodCall methodCall = this.communicationHandler.newComSession(new RemoteMethodCall(this.serverMethodsNameProvider.joinNewGame(), parameters));
            this.processRemoteInvocation(methodCall);;
            this.guiInteractionManager.setConnectionActiveReaction(true);
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        } catch (IOException e1) {
            this.guiInteractionManager.setConnectionActiveReaction(false);
        }
        boolean isActionServerValidated = this.currentRrNotification.getActionResult();
        if (isActionServerValidated){
            this.client.setPlayer(playerName);
            this.setPlayerTokenAndSubscribe(this.currentRrNotification.getPlayerToken());
        }
    }

    /**
     * Requests to the server the join of a game.
     *
     * @param gameId     The id of the game to join.
     * @param playerName The name of the client in the game.
     */
    public void joinGame(int gameId, String playerName) {
        this.client.setPlayer(playerName);
        ArrayList<Object> parameters = new ArrayList<>();
        parameters.add(gameId);
        parameters.add(playerName);
        try {
            RemoteMethodCall methodCall = this.communicationHandler.newComSession(new RemoteMethodCall(this.serverMethodsNameProvider.joinGame(), parameters));
            this.processRemoteInvocation(methodCall);
            this.guiInteractionManager.setConnectionActiveReaction(true);
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        } catch (IOException e1) {
            this.guiInteractionManager.setConnectionActiveReaction(false);
        }
        boolean isActionServerValidated = this.currentRrNotification.getActionResult();
        if (isActionServerValidated){
            this.client.setPlayer(playerName);
            this.setPlayerTokenAndSubscribe(this.currentRrNotification.getPlayerToken());
        }
    }

    /**
     * Publishes a message in the chat window.
     * This method is invoked indirectly using reflection.
     *
     * @param msg The message to be published.
     */
    private void publishChatMsg(String msg) {
        this.guiInteractionManager.publishChatMessage(msg);
    }


    /**
     * Moves the client to the sector at the given coordinates and executes the other logic related to this action.
     * This action is validated and registered by contacting the game server.
     *
     * @param coordinate The coordinates of the sector to move to.
     */
    public void moveToSector(Coordinate coordinate) {
        Sector targetSector = this.client.getGameMap().getSectorByCoords(coordinate);
        ArrayList<Object> parameters = new ArrayList<>();
        Action action = new MoveAction(targetSector);
        parameters.add(action);
        parameters.add(this.client.getPlayer().getPlayerToken());
        try {
            RemoteMethodCall methodCall = this.communicationHandler.newComSession(new RemoteMethodCall("makeAction", parameters));
            this.processRemoteInvocation(methodCall);
            this.guiInteractionManager.setConnectionActiveReaction(true);
            this.guiInteractionManager.displayResponseMsg(this.currentRrNotification.getMessage());
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        } catch (IOException e1) {
            this.guiInteractionManager.setConnectionActiveReaction(false);
        }
        boolean isActionServerValidated = this.currentRrNotification.getActionResult();
        if (isActionServerValidated){
            this.client.move(coordinate);
            this.guiInteractionManager.moveToSectorReaction();
            List<Card> drawnCards = this.currentRrNotification.getDrawnCards();
            if (drawnCards.size() == 1) {
                this.guiInteractionManager.setDrawnSectorObjectCardReaction(null,(SectorCard) drawnCards.get(0));
            } else if (drawnCards.size() == 2) {
                this.client.getPlayer().getPrivateDeck().addCard((ObjectCard) drawnCards.get(1));
                this.guiInteractionManager.setDrawnSectorObjectCardReaction((ObjectCard) drawnCards.get(1),(SectorCard) drawnCards.get(0));
            }
        }

    }

    /**
     * Signals that there's the possibility, by the client, to start the game without waiting for 8 players to join.
     * This method is invoked indirectly using reflection.
     */
    private void signalStartableGame() {
        this.guiInteractionManager.signalStartableGame();
    }

    /**
     * Makes the client use an object card. This action is validated and registered by contacting the game server.
     *
     * @param objectCard The object card to be used
     */
    public void useObjCard(ObjectCard objectCard) {
        if (this.client.getPlayer().getPrivateDeck().getContent().contains(objectCard)) {
            if (objectCard instanceof LightsObjectCard) {
                this.guiInteractionManager.askForSectorToLightReaction();
            } else if (objectCard instanceof AttackObjectCard) {
                this.guiInteractionManager.askForSectorToAttackReaction();
            } else {
                ArrayList<Object> parameters = new ArrayList<>();
                Action action = new UseObjAction(objectCard);
                parameters.add(action);
                parameters.add(this.client.getPlayer().getPlayerToken());
                try {
                    RemoteMethodCall methodCall = this.communicationHandler.newComSession(new RemoteMethodCall(this.serverMethodsNameProvider.makeAction(), parameters));
                    this.processRemoteInvocation(methodCall);
                    this.guiInteractionManager.setConnectionActiveReaction(true);
                    this.guiInteractionManager.displayResponseMsg(this.currentRrNotification.getMessage());
                } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                } catch (IOException e1) {
                    this.guiInteractionManager.setConnectionActiveReaction(false);
                }
                boolean isActionServerValidated = this.currentRrNotification.getActionResult();
                if (isActionServerValidated) {
                    this.client.getPlayer().getPrivateDeck().removeCard(objectCard);
                    this.guiInteractionManager.useObjectCardReaction(objectCard);
                    if (objectCard instanceof TeleportObjectCard) {
                        this.guiInteractionManager.teleportToStartingSectorReaction();
                        this.client.teleport();
                    } else if (objectCard instanceof SuppressorObjectCard) {
                        this.client.getPlayer().setSedated(true);
                    } else if (objectCard instanceof AdrenalineObjectCard) {
                        this.client.getPlayer().setAdrenalined(true);
                    }
                }

            }
        }


    }

    /**
     * Allows the client to act on the game as consequence of a favorable turn switch.
     * This method is invoked indirectly using reflection.
     */
    private void startTurn() {
        this.client.setIsMyTurn(true);
        this.guiInteractionManager.startTurn();
    }

    /**
     * Makes the client indicate a sector to the other players of the game as a consequence of
     * a global noise sector card. This action is validated and registered by contacting the game server.
     *
     * @param coordinate The coordinates of the sector to be indicated
     * @param hasObject  If there's an object card associated with the global noise sector card(irrelevant)
     */
    public void globalNoise(Coordinate coordinate, boolean hasObject) {
        Sector targetSector = this.client.getGameMap().getSectorByCoords(coordinate);
        if (targetSector != null) {
            SectorCard globalNoiseCard = new GlobalNoiseSectorCard(hasObject,
                    targetSector);
            ArrayList<Object> parameters = new ArrayList<>();
            Action action = new UseSectorCardAction(globalNoiseCard);
            parameters.add(action);
            parameters.add(this.client.getPlayer().getPlayerToken());
            try {
                RemoteMethodCall methodCall = this.communicationHandler.newComSession(new RemoteMethodCall(this.serverMethodsNameProvider.makeAction(), parameters));
                this.processRemoteInvocation(methodCall);
                this.guiInteractionManager.setConnectionActiveReaction(true);
                this.guiInteractionManager.displayResponseMsg(this.currentRrNotification.getMessage());
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e1) {
                this.guiInteractionManager.setConnectionActiveReaction(false);
            }
            boolean isActionServerValidated = this.currentRrNotification.getActionResult();
            if (isActionServerValidated){
                this.guiInteractionManager.setDrawnSectorObjectCardReaction(null,null);
            }
        } else {
            throw new IllegalArgumentException(
                    "The sector you have indicated does not exists, please try again");
        }
    }

    /**
     * Makes the client indicate a sector and discover if there are players in the sector itself or in the adjacent ones
     * as a consequence of a lights object card. This action is validated and registered by contacting the game server.
     *
     * @param coordinate The coordinates of the sector for the ligths object card effect
     */
    public void lights(Coordinate coordinate) {
        Sector targetSector = this.client.getGameMap().getSectorByCoords(coordinate);
        if (targetSector != null) {
            ObjectCard lightsCard = new LightsObjectCard(targetSector);
            ArrayList<Object> parameters = new ArrayList<>();
            Action action = new UseObjAction(lightsCard);
            parameters.add(action);
            parameters.add(this.client.getPlayer().getPlayerToken());
            try {
                RemoteMethodCall remoteMethodCall = this.communicationHandler.newComSession(new RemoteMethodCall(this.serverMethodsNameProvider.makeAction(), parameters));
                this.processRemoteInvocation(remoteMethodCall);
                this.guiInteractionManager.setConnectionActiveReaction(true);
                this.guiInteractionManager.displayResponseMsg(this.currentRrNotification.getMessage());
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e1) {
                this.guiInteractionManager.setConnectionActiveReaction(false);
            }
            boolean isActionServerValidated = this.currentRrNotification.getActionResult();
            if (isActionServerValidated){
                this.client.getPlayer().getPrivateDeck().removeCard(lightsCard);
                this.guiInteractionManager.useObjectCardReaction(lightsCard);
            }
        } else {
            throw new IllegalArgumentException(
                    "Undefined sector, please try again");
        }

    }

    /**
     * Makes the client end its turn. This action is validated and registered by contacting the game server.
     */
    public void endTurn() {
        ArrayList<Object> parameters = new ArrayList<>();
        Action action = new EndTurnAction();
        parameters.add(action);
        parameters.add(this.client.getPlayer().getPlayerToken());
        try {
            RemoteMethodCall remoteMethodCall = this.communicationHandler.newComSession(new RemoteMethodCall("makeAction", parameters));
            this.processRemoteInvocation(remoteMethodCall);
            this.guiInteractionManager.setConnectionActiveReaction(true);
            this.guiInteractionManager.displayResponseMsg(this.currentRrNotification.getMessage());
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        } catch (IOException e) {
            this.guiInteractionManager.setConnectionActiveReaction(false);
        }
        boolean isActionServerValidated = this.currentRrNotification.getActionResult();
        if (isActionServerValidated){
            this.client.endTurn();
            this.guiInteractionManager.endTurn();
        }

    }

    private void forceEndTurn(){
        this.guiInteractionManager.displayResponseMsg("You have taken too much you will skip your turn");
        this.client.endTurn();
        this.guiInteractionManager.endTurn();
    }

    /**
     * Makes the client discard a given object card. This action is validated and registered by contacting the game server.
     *
     * @param objectCard The object card to be discarded.
     */
    public void discardCard(ObjectCard objectCard) {
        if (this.client.getPlayer().getPrivateDeck().getContent().contains(objectCard)) {
            ArrayList<Object> parameters = new ArrayList<>();
            Action action = new DiscardAction(objectCard);
            parameters.add(action);
            parameters.add(this.client.getPlayer().getPlayerToken());
            try {
                RemoteMethodCall remoteMethodCall = this.communicationHandler.newComSession(new RemoteMethodCall("makeAction", parameters));
                this.processRemoteInvocation(remoteMethodCall);
                this.guiInteractionManager.setConnectionActiveReaction(true);
                this.guiInteractionManager.displayResponseMsg(this.currentRrNotification.getMessage());
            } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            } catch (IOException e1) {
                this.guiInteractionManager.setConnectionActiveReaction(false);
            }
            boolean isActionServerValidated = this.currentRrNotification.getActionResult();
            if (isActionServerValidated){
                this.client.getPlayer().getPrivateDeck().removeCard(objectCard);
                this.guiInteractionManager.discardObjectCardReaction();
            }
        }
    }

    /**
     * Invokes a method on instances of this class from the given {@link RemoteMethodCall} using reflection.
     *
     * @param remoteClientInvocation A description of what method with what arguments
     *                               need to be invoked on instances of this class
     * @throws IllegalAccessException Reflection related exception
     * @throws IllegalArgumentException Reflection related exception
     * @throws InvocationTargetException Reflection related exception
     * @throws NoSuchMethodException Reflection related exception
     * @throws SecurityException Reflection related exception
     */
    public void processRemoteInvocation(RemoteMethodCall remoteClientInvocation)
            throws IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, SecurityException {
        String methodName = remoteClientInvocation.getMethodName();
        ArrayList<Object> parameters = remoteClientInvocation
                .getMethodParameters();
        Class<?>[] parametersClasses = new Class[parameters.size()];
        for (int i = 0; i < parameters.size(); i++) {
            parametersClasses[i] = parameters.get(i).getClass();
        }
        this.getClass().getDeclaredMethod(methodName, parametersClasses)
                .invoke(this, parameters.toArray());
    }

    /**
     * Sets the current {@link RRClientNotification}
     * @param clientNotification The notification to be set
     */
    private void syncNotification(RRClientNotification clientNotification){
        this.currentRrNotification = clientNotification;
    }

    /**
     * Sets the current {@link PSClientNotification} and does some checks on the status of the game
     * @param psNotification The notification to be set
     */
    public void asyncNotification(PSClientNotification psNotification) {
        this.currentPsNotification = psNotification;
        this.guiInteractionManager.publishChatMessage(psNotification.getMessage());
        if (psNotification.getHumanWins() || psNotification.getAlienWins()) {
            this.guiInteractionManager.setWinnersReaction(psNotification.getHumanWins(),psNotification.getAlienWins());
            this.communicationHandler.getPubSubHandler().setListeningFlag(false);
            this.client.setGameStarted(false);
        }
        if (psNotification.getEscapedPlayer() != null) {
            if (psNotification.getEscapedPlayer().equals(this.client.getPlayer().getPlayerToken())){
                this.guiInteractionManager.setPlayerStateReaction();
                this.client.setGameStarted(false);
            }
        }
        if (psNotification.getDeadPlayers().contains(this.client.getPlayer().getPlayerToken())) {
            this.guiInteractionManager.setPlayerStateReaction();
            this.client.setGameStarted(false);
        } else if (psNotification.getAttackedPlayers().contains(this.client.getPlayer().getPlayerToken())) {
            this.guiInteractionManager.useObjectCardReaction(new DefenseObjectCard());
        }
    }
    public void setMapAndStartGame(String mapName) {
        this.client.setGameStarted(true);
        GameMap gameMap;
        GameMapFactory factory;
        switch (mapName) {
            case "GALILEI":
                factory = new GalileiGameMapFactory();
                break;
            case "FERMI":
                factory = new FermiGameMapFactory();
                break;
            case "GALVANI":
                factory = new GalvaniGameMapFactory();
                break;
            default:
                throw new IllegalArgumentException("The type of map is undefined");
        }
        gameMap = factory.makeMap();
        this.client.setGameMap(gameMap);
        if (this.client.getPlayer().getPlayerToken().getPlayerType().equals(PlayerType.ALIEN)) {
            this.client.getPlayer().setCurrentSector(gameMap.getAlienSector());
        } else {
            this.client.getPlayer().setCurrentSector(gameMap.getHumanSector());
        }
        this.client.setGameStarted(true);
        this.guiInteractionManager.startGameReaction();
    }
    /**
     * Makes the client send a chat message to the other players. This action is validated and registered by contacting the game server.
     *
     * @param message The message to be sent.
     */
    public void sendMessage(String message) {
        ArrayList<Object> parameters = new ArrayList<>();
        parameters.add(message);
        parameters.add(this.client.getPlayer().getPlayerToken());
        try {
            RemoteMethodCall remoteMethodCall = this.communicationHandler.newComSession(
                    new RemoteMethodCall(this.serverMethodsNameProvider.publishChatMsg(), parameters));
            this.processRemoteInvocation(remoteMethodCall);
            this.guiInteractionManager.setConnectionActiveReaction(true);
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        } catch (IOException e) {
            this.guiInteractionManager.setConnectionActiveReaction(false);
        }
    }
    /**
     * Makes the client attack a sector at given coordinates. This action is validated and registered by contacting the game server.
     *
     * @param coordinate The coordinates of the sector to be attacked.
     */
    public void attack(Coordinate coordinate) {
        boolean humanAttack = this.client.getPlayer().getPlayerToken().getPlayerType().equals(PlayerType.HUMAN);
        Sector targetSector = this.client.getGameMap().getSectorByCoords(coordinate);
        ArrayList<Object> parameters = new ArrayList<>();
        AttackObjectCard card;
        if (humanAttack) {
            card = new AttackObjectCard(targetSector);
            Action action = new UseObjAction(card);
            parameters.add(action);
            parameters.add(this.client.getPlayer().getPlayerToken());

        } else {
            Action action = new MoveAttackAction(targetSector);
            parameters.add(action);
            parameters.add(this.client.getPlayer().getPlayerToken());
        }
        try {
            RemoteMethodCall remoteMethodCall = this.communicationHandler.newComSession(
                    new RemoteMethodCall(this.serverMethodsNameProvider.makeAction(), parameters));
            this.processRemoteInvocation(remoteMethodCall);
            this.guiInteractionManager.setConnectionActiveReaction(true);
            this.guiInteractionManager.displayResponseMsg(this.currentRrNotification.getMessage());
        } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        } catch (IOException e1) {
            this.guiInteractionManager.setConnectionActiveReaction(false);
        }
        boolean isActionServerValidated = this.currentRrNotification.getActionResult();
        if (isActionServerValidated){
            this.guiInteractionManager.moveToSectorReaction();
        }
    }
    /**
     * Sets the games that are running of waiting to be run on the game server.
     * This method is executed indirectly using reflection.
     *
     * @param avGames The list of data relative to the games available on the server.
     */
    private void setAvailableGames(ArrayList<GamePublicData> avGames) {
        this.availableGames = avGames;
        this.guiInteractionManager.setAvailableGamesReaction();
    }

    /**
     * Sets the identification token for the client and subscribes the client to asynchronous
     * notification/method calls by the server.
     * This method is invoked indirectly using reflection.
     *
     * @param playerToken The client's identification token.
     */
    private void setPlayerTokenAndSubscribe(PlayerToken playerToken) {
        this.client.getPlayer().setPlayerToken(playerToken);
        ArrayList<Object> parameters = new ArrayList<>();
        parameters.add(playerToken);
        try {
            this.communicationHandler.newComSession(
                    new RemoteMethodCall(this.serverMethodsNameProvider.subscribe(),parameters));
            this.guiInteractionManager.setConnectionActiveReaction(true);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            this.guiInteractionManager.setConnectionActiveReaction(false);
        }

    }
    /**
     * Requests to the game server a list of available games.
     */
    public void getGames() {
        try {
            RemoteMethodCall methodCall = this.communicationHandler.newComSession(new RemoteMethodCall(this.serverMethodsNameProvider.getGames(), new ArrayList<>()));
            this.processRemoteInvocation(methodCall);
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        } catch (IOException e1) {
            this.guiInteractionManager.setConnectionActiveReaction(false);
        }
    }
    /**
     * Requests to the server the start of a created game without waiting for 8 players to join the game.
     */
    public void onDemandGameStart() {
        ArrayList<Object> parameters = new ArrayList<>();
        parameters.add(this.client.getPlayer().getPlayerToken());
        try {
            RemoteMethodCall methodCall = this.communicationHandler.newComSession(
                    new RemoteMethodCall(this.serverMethodsNameProvider.onDemandGameStart(), parameters));
            this.processRemoteInvocation(methodCall);
            this.guiInteractionManager.setConnectionActiveReaction(true);
        } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        } catch (IOException e) {
            this.guiInteractionManager.setConnectionActiveReaction(false);
        }
    }
    public RRClientNotification getCurrentRrNotification(){
        return this.currentRrNotification;
    }
    public PSClientNotification getCurrentPsNotification(){
        return this.currentPsNotification;
    }
    public List<GamePublicData> getAvailableGames(){
        return this.availableGames;
    }
}
