package client_gui;

import client.*;
import client_store.ClientStore;
import client_store.InteractionManager;
import client_store_actions.*;
import common.*;
import it.polimi.ingsw.cg_19.PlayerType;
import server_store.Player;
import server_store.StoreAction;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.List;
import java.util.Timer;

/**
 * Created by giorgiopea on 27/03/17.
 */
public class GuiManager implements Observer {

    private static GuiManager instance = new GuiManager();
    private final ClientStore clientStore = ClientStore.getInstance();
    private GUInitialWindow guiInitialWindow;
    private GUIGameList guiGameList;
    private GUIGamePane guiGamePane;
    private JFrame mainFrame;

    public static GuiManager getInstance() {
        return instance;
    }

    private GuiManager() {
        ClientStore.getInstance().observeState(this);
    }

    public void initGuiComponents() {
        this.mainFrame = new JFrame("Escape from aliens in the outer space");
        this.guiInitialWindow = new GUInitialWindow();
        this.guiGameList = new GUIGameList();
        this.guiGameList
                .setLayout(new BoxLayout(this.guiGameList, BoxLayout.Y_AXIS));
        this.guiGameList.setBackground(Color.BLACK);
        this.guiGamePane = new GUIGamePane();
        this.guiGamePane.setLayout(new GridBagLayout());
        this.guiInitialWindow.load();
        mainFrame.getContentPane().add(this.guiInitialWindow);
        this.guiInitialWindow.setVisible(true);
    }

    private void updateGuiState() {
        boolean isMyTurn = ClientStore.getInstance().getState().isMyTurn;
        PlayerType playerType = ClientStore.getInstance().getState().player.playerType;
        if (isMyTurn) {
            this.guiGamePane.setStateMessage("It's your turn!");
            if (playerType.equals(PlayerType.HUMAN)) {
                this.guiGamePane.changeCardMenu(MenuType.HUMAN_USE_MENU);
                this.guiGamePane.getMapPane().changeMapMenu(
                        MenuType.HUMAN_INITIAL);
            } else {
                this.guiGamePane.changeCardMenu(MenuType.EMPTY);
                this.guiGamePane.getMapPane().changeMapMenu(
                        MenuType.ALIEN_INITIAL);
            }
        } else {
            this.guiGamePane.setStateMessage("Waiting your turn!");
            this.guiGamePane.changeCardMenu(MenuType.EMPTY);
            this.guiGamePane.getMapPane().changeMapMenu(MenuType.EMPTY);
        }
    }

    public void connectAndDisplayGames() {
        try {
            this.guiInitialWindow.alertConnectionProblem(false);
            interactionManager.getGames();
            this.guiInitialWindow.setVisible(false);
            this.guiGameList
                    .setLayout(new BoxLayout(this.guiGameList, BoxLayout.Y_AXIS));
            this.guiGameList.setBackground(Color.BLACK);
            this.guiGameList.load();
            mainFrame.getContentPane().add(this.guiGameList);
            this.guiGameList.setVisible(true);
            ClientStore.getInstance().getState().gamePollingTimer = new Timer();
            ClientStore.getInstance().getState().gamePollingTimer.scheduleAtFixedRate(new GamePollingThread(), 0, 10000);
        } catch (IOException | ClassNotFoundException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
            this.guiInitialWindow.alertConnectionProblem(true);
        }

    }

    public void forwardMethod(String methodName, List<Object> parameters) {
        try {
            this.interactionManager.executeMethod(methodName, parameters);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }


    private void updatePosition() {
        Coordinate newCoordinates = this.clientStore.getState().player.currentSector.getCoordinate();
        String playerName = this.clientStore.getState().player.name;
        this.guiGamePane.getMapPane().delightAllSectors();
        this.guiGamePane.getMapPane().lightSector(newCoordinates, "Y",
                playerName);
    }

    private void updateCardsPanel() {
        PrivateDeck privateDeck = ClientStore.getInstance().getState().player.privateDeck;
        for (ObjectCard card : privateDeck.getContent()) {
            this.guiGamePane.addCardToPanel(card);
        }
        this.guiGamePane.repaint();
    }

    public void attack(Coordinate coords) {
        this.interactionManager.attack(coords);
        boolean hasAttacked = ClientStore.getInstance().getState().askAttack;
        if (!hasAttacked){
            this.updatePosition();
            this.guiGamePane.removeAllCardsFromPanel();
            this.updateCardsPanel();
            try {
                this.handleAction();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            this.guiGamePane.setStateMessage(ClientStore.getInstance().getState().currentReqRespNotification.getMessage());
        }
        else {
            this.guiGamePane.setStateMessage("You cannot attack in that sector!");
        }

    }

    public void move(Coordinate coordinate) {
        this.interactionManager.move(coordinate);
        boolean hasMoved = ClientStore.getInstance().getState().player.hasMoved;

        if (hasMoved) {
            this.updatePosition();
            this.guiGamePane.setStateMessage("You have moved to sector "+ClientStore.getInstance().getState().player.currentSector.getCoordinate());
            try {
                this.handleAction();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            this.guiGamePane.setStateMessage("You cannot move in that sector!");
        }
    }

    public JFrame getFrame() {
        return this.mainFrame;
    }


    private void handleAction() throws ClassNotFoundException {
        Card firstCard, secondCard = null;
        boolean isAskingLights = ClientStore.getInstance().getState().askLights;
        boolean isAskingAttack = ClientStore.getInstance().getState().askAttack;
        boolean hasMoved = ClientStore.getInstance().getState().player.hasMoved;
        PlayerType playerType = ClientStore.getInstance().getState().player.playerType;
        RRClientNotification currentReqRespNotification = ClientStore.getInstance().getState().currentReqRespNotification;
        PrivateDeck clientPrivateDeck = ClientStore.getInstance().getState().player.privateDeck;

        List<Card> drawedCards = currentReqRespNotification.getDrawnCards();

        if (isAskingLights) {
            this.guiGamePane
                    .setStateMessage("Select a sector for the light card");
            this.guiGamePane.getMapPane().changeMapMenu(
                    MenuType.LIGHT_MENU);
        } else if (isAskingAttack) {
            this.guiGamePane
                    .setStateMessage("Select a sector fot the attack card");
            this.guiGamePane.getMapPane().changeMapMenu(
                    MenuType.ATTACK_MENU);
        } else if (!drawedCards.isEmpty()) {
            firstCard = drawedCards.get(0);
            if (drawedCards.size() > 1) {
                secondCard = drawedCards.get(1);
                clientPrivateDeck.addCard(
                        (ObjectCard) secondCard);
                this.guiGamePane
                        .addCardToPanel((ObjectCard) secondCard);
            }

            CardSplashScreen splash = new CardSplashScreen(firstCard,
                    secondCard, mainFrame);

            if (firstCard instanceof GlobalNoiseSectorCard) {
                this.guiGamePane
                        .setStateMessage("Select a sector for the global noise card");
                this.guiGamePane.getMapPane().changeMapMenu(
                        MenuType.NOISE_MENU);
                this.guiGamePane.changeCardMenu(MenuType.EMPTY);
                // I've drawn a object card
            }
            else {
                if (firstCard instanceof SilenceSectorCard){
                    this.guiGamePane
                            .setStateMessage("You will not reveal any clue about your position");
                    this.guiGamePane.getMapPane().changeMapMenu(MenuType.EMPTY);
                }
                if (firstCard instanceof LocalNoiseSectorCard){
                    this.guiGamePane.setStateMessage("You have revealed your position");
                    this.guiGamePane.getMapPane().changeMapMenu(MenuType.EMPTY);
                }
                if (clientPrivateDeck.getSize() > 3){
                    this.manyCardHandler(playerType);
                }
                else {
                    this.guiGamePane.showEndTurnButton(true);
                }

            }
        } else {
            if (clientPrivateDeck.getSize() > 3) {
                this.manyCardHandler(playerType);
            } else if (playerType.equals(PlayerType.HUMAN)) {
                if (hasMoved) {
                    this.guiGamePane.showEndTurnButton(true);
                    this.guiGamePane.getMapPane().changeMapMenu(
                            MenuType.EMPTY);
                    this.guiGamePane.changeCardMenu(MenuType.HUMAN_USE_MENU);
                } else {
                    this.guiGamePane.getMapPane().changeMapMenu(
                            MenuType.HUMAN_INITIAL);
                    this.guiGamePane.changeCardMenu(MenuType.HUMAN_USE_MENU);
                }

            } else {
                if (hasMoved) {
                    this.guiGamePane.showEndTurnButton(true);
                    this.guiGamePane.getMapPane().changeMapMenu(
                            MenuType.EMPTY);
                } else {
                    this.guiGamePane.getMapPane().changeMapMenu(
                            MenuType.ALIEN_INITIAL);
                }
            }
        }
    }

    private void manyCardHandler(PlayerType playerType) {
        if (playerType.equals(PlayerType.HUMAN)) {
            this.guiGamePane.changeCardMenu(MenuType.HUMAN_USE_DISC_MENU);
        } else {
            this.guiGamePane.changeCardMenu(MenuType.ALIEN_CARD_MENU);
        }

        this.guiGamePane.setStateMessage("Use or discard an object card");
        this.guiGamePane.getMapPane().changeMapMenu(MenuType.EMPTY);
    }

    private void showMessageBox(String message) {
        JOptionPane.showMessageDialog(mainFrame, message);
    }


    private void processPSNotification(PSClientNotification notification) {
        PlayerToken playerToken = ClientStore.getInstance().getState().player.playerToken;
        if (notification.getEscapedPlayer() != null) {
            if (notification.getEscapedPlayer().equals(playerToken)) {
                this.guiGamePane.setStateMessage("You're ESCAPED!");
                this.guiGamePane.changeCardMenu(MenuType.EMPTY);
                this.guiGamePane.getMapPane().changeMapMenu(MenuType.EMPTY);
                this.guiGamePane.showEndTurnButton(false);
            }
        }

        if (notification.getAlienWins()) {
            if (notification.getHumanWins()) {
                GUIFinalWindow finalWindow = new GUIFinalWindow(this.mainFrame,
                        "HumanAlienW.png", true);
            } else {
                GUIFinalWindow finalWindow = new GUIFinalWindow(this.mainFrame,
                        "AlienW.png", true);
            }
        } else if (notification.getHumanWins()) {
            GUIFinalWindow finalWindow;
            if (notification.getAlienWins()) {
                finalWindow = new GUIFinalWindow(this.mainFrame,
                        "HumanAlienW.png", true);
            } else {
                finalWindow = new GUIFinalWindow(this.mainFrame, "HumanW.png",
                        true);
            }
        }
        if (notification.getDeadPlayers().contains(playerToken)) {
            GUIFinalWindow deadWindow = new GUIFinalWindow(this.mainFrame,
                    "dead.jpg", false);
            this.guiGamePane.setStateMessage("You're DEAD!");
            this.guiGamePane.changeCardMenu(MenuType.EMPTY);
            this.guiGamePane.getMapPane().changeMapMenu(MenuType.EMPTY);
            this.guiGamePane.showEndTurnButton(false);
        } else if (notification.getAttackedPlayers()
                .contains(playerToken)) {
            this.showMessageBox("You've used your Defence Object card, You're still alive!");
            ObjectCard toRemove = null;
            for (ObjectCard c : ClientStore.getInstance().getState().player.privateDeck.getContent()) {
                if (c instanceof DefenseObjectCard) {
                    toRemove = c;
                    break;
                }
            }
            ClientStore.getInstance().getState().player.privateDeck.getContent().remove(toRemove);
            this.guiGamePane.removeAllCardsFromPanel();
            this.updateCardsPanel();
            this.guiGamePane.setStateMessage("You're safe!");
        }
    }

    private void endGame(boolean humansHaveWon, boolean aliensHaveWon) {
        //Go to final window
    }

    public void joinNewGame(String mapName, String playerName) {
        interactionManager.joinNewGame(mapName, playerName);
    }

    /**
     * Produces a noise effect to the given coordinates
     */
    public void noise(Coordinate coords) {
        this.interactionManager.globalNoise(coords, true);
        this.guiGamePane.setStateMessage("You've made noise on sector "
                + coords.getX() + "" + coords.getY());
        try {
            this.handleAction();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    /**
     * Updates the map panel after using a light object card It displays the
     * lighted sector and evidence the player in that sector
     *
     * @param sectors The list of sectord to light
     */
    private void updateLightedSector(List<Sector> sectors) {
        String nameList = "";
        for (Sector s : sectors) {
            nameList = "";
            for (server_store.Player p : s.getPlayers()) {
                nameList += " " + p.name;
            }
            this.guiGamePane.getMapPane().lightSector(s.getCoordinate(), "",
                    nameList);
        }
    }

    /**
     * Produces a light effect to the given coordinates
     */
    public void lights(Coordinate coords) {
        this.interactionManager.lights(coords);
        this.updateLightedSector(ClientStore.getInstance().getState().currentReqRespNotification
                .getLightedSectors());
        try {
            this.handleAction();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        this.guiGamePane.removeAllCardsFromPanel();
        this.updateCardsPanel();
        this.guiGamePane.setStateMessage(ClientStore.getInstance().getState().currentReqRespNotification.getMessage());
    }

    /**
     * Allows the client to use an object card
     */
    public void useObjectCard(ObjectCard card) {
        int index = ClientStore.getInstance().getState().player.privateDeck.getContent().indexOf(card);
        try {
            this.interactionManager.useObjCard(index + 1);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        this.updatePosition();
        this.guiGamePane.removeAllCardsFromPanel();
        this.updateCardsPanel();
        try {
            this.handleAction();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        if (!ClientStore.getInstance().getState().currentReqRespNotification.getActionResult())
            this.guiGamePane.setStateMessage("You cannot use this card!");
        else {
            if (!(card instanceof LightsObjectCard) && !(card instanceof AttackObjectCard))
                this.guiGamePane.setStateMessage("You've used an object card!");
        }
    }

    /**
     * Allows the client to discard an object card
     */
    public void discard(ObjectCard card) {
        int index = ClientStore.getInstance().getState().player.privateDeck.getContent().indexOf(card);
        this.interactionManager.discardCard(index + 1);
        this.guiGamePane.removeAllCardsFromPanel();
        this.updateCardsPanel();
        try {
            this.handleAction();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        this.guiGamePane.setStateMessage("You've discarded an object card");
    }

    /**
     * Allows the user to terminate its turn
     */
    public void endTurn() {
        this.interactionManager.endTurn();
        this.updateGuiState();
        this.guiGamePane.showEndTurnButton(false);
    }

    @Override
    public void update(Observable o, Object arg) {
        StoreAction action = (StoreAction) arg;
        switch (action.getType()) {
            case "@CLIENT_SET_AVAILABLE_GAMES": {
                this.setAvailableGamesReaction(action);
                break;
            }
            case "@CLIENT_SET_CONNECTION_ACTIVE": {
                this.setConnectionActiveReaction(action);
                break;
            }
            case "@CLIENT_START_GAME":
                this.startGameReaction();
                break;
            case "@CLIENT_MOVE_TO_SECTOR":
                this.moveToSectorReaction(action);
                break;
            case "@CLIENT_PUBLISH_MSG": {
                ClientSetCurrentMessage castedAction = (ClientSetCurrentMessage) action;
                this.guiGamePane.appendMsg(castedAction.payload);
                break;
            }
            case "@CLIENT_ALLOW_TURN":
                updateGuiState();
                break;
            case "@CLIENT_SET_CURRENT_PUBSUB_NOTIFICATION": {
                ClientSetCurrentPubSubNotificationAction castedAction = (ClientSetCurrentPubSubNotificationAction) action;
                this.guiGamePane.appendMsg(castedAction.payload.getMessage());
                this.processPSNotification(castedAction.payload);
                break;
            }
            case "@CLIENT_SET_CURRENT_REQRESP_NOTIFICATION":
                //ClientSetCurrentReqRespNotificationAction castedAction = (ClientSetCurrentReqRespNotificationAction) action;
                //this.guiGamePane.setStateMessage(castedAction.payload.getMessage());
                break;
            case "@CLIENT_DENY_TURN":
                this.updateGuiState();
                break;
            case "@CLIENT_STARTABLE_GAME":
                this.guiGameList.startableGame();
                break;
        }
    }

    private void moveToSectorReaction(StoreAction action) {
        ClientMoveAction castedAction = (ClientMoveAction) action;
        Player player = this.clientStore.getState().player;
        this.guiGamePane.setStateMessage(this.clientStore.getState().currentReqRespNotification.getMessage());
        if (player.hasMoved){
            this.updatePosition();
            this.nextState();
        }
    }

    /**
     * Switches to the {@link client.GUIGamePane} view from the {@link client.GUIGameList} and
     * acts on this view in response to the starting of the game
     */
    private void startGameReaction() {
        Player player = this.clientStore.getState().player;
        String characterInfoMsg = "";
        if (player.playerToken.playerType.equals(PlayerType.ALIEN)){
            characterInfoMsg = player.name + " | ALIEN";
            this.guiGamePane.setSectorMenu(MenuType.ALIEN_INITIAL);
        }
        else {
            characterInfoMsg = player.name + " | HUMAN";
            this.guiGamePane.setSectorMenu(MenuType.HUMAN_INITIAL);
        }
        this.mainFrame.remove(this.guiGameList);
        this.guiGamePane.load("dasd");
        this.mainFrame.add(this.guiGamePane);
        this.guiGamePane.setVisible(true);
        this.guiGamePane.setInfoMsg(characterInfoMsg);
        this.guiGamePane.getMapPane().lightSector(
                player.currentSector.getCoordinate(), "Y", player.name);
        this.mainFrame.validate();
        this.mainFrame.repaint();
    }

    /**
     * Make the different gui components of the app signal the absence of a connection
     * with the server
     * @param action The {@link server_store.StoreAction} that has caused this behavior
     */
    private void setConnectionActiveReaction(StoreAction action) {
        ClientSetConnectionActiveAction castedAction = (ClientSetConnectionActiveAction) action;
        this.guiInitialWindow.alertConnectionProblem(castedAction.isConnectionActive);
        this.guiGameList.alertConnectionProblem(castedAction.isConnectionActive);
        this.guiGamePane.alertConnectionProblem(castedAction.isConnectionActive);
    }

    /**
     * Switches to the {@link client.GUIGameList} view from the {@link client.GUInitialWindow}
     * @param action The {@link server_store.StoreAction} that has caused this behavior
     */
    private void setAvailableGamesReaction(StoreAction action) {
        ClientSetAvailableGamesAction castedAction = (ClientSetAvailableGamesAction) action;
        this.mainFrame.remove(this.guiInitialWindow);
        this.guiGameList.load();
        this.guiGameList.setGameListContent(castedAction.availableGames);
        mainFrame.getContentPane().add(this.guiGameList);
        this.guiGameList.setVisible(true);
        this.mainFrame.validate();
        this.mainFrame.repaint();
    }

}
