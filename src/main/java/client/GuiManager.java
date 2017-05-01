package client;

import common.*;
import it.polimi.ingsw.cg_19.PlayerState;
import it.polimi.ingsw.cg_19.PlayerType;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.Timer;

/**
 * Created by giorgiopea on 25/04/17.
 *
 */
public class GuiManager {
    private final long DELAY_RETURN_TO_GAME_LIST = 10000;
    private final Timer gameListRefreshTimer;
    private final ClientServices clientServices;
    private static GuiManager instance = new GuiManager();
    private final Client client;
    private GUInitialWindow guiInitialWindow;
    private GUIGameList guiGameList;
    private GUIGamePane guiGamePane;
    private JFrame mainFrame;

    public static GuiManager getInstance() {
        return instance;
    }

    private GuiManager() {
        this.client = Client.getInstance();
        this.gameListRefreshTimer = new Timer();
        this.clientServices = ClientServices.getInstance();
        this.clientServices.initWithGuiManager(this);
    }

    /**
     * Inits all the gui components of the application
     */
    public void initGuiComponents() {
        this.mainFrame = new JFrame("Escape from aliens in the outer space");
        this.mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.mainFrame.setLocationByPlatform(true);
        this.mainFrame.pack();
        this.mainFrame.setExtendedState(this.mainFrame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
        this.guiInitialWindow = new GUInitialWindow();
        this.guiGameList = new GUIGameList();
        this.guiGamePane = new GUIGamePane();
        this.guiGamePane.setLayout(new GridBagLayout());
        this.guiInitialWindow.load();
        this.mainFrame.setVisible(true);
        mainFrame.getContentPane().add(this.guiInitialWindow);
        this.guiInitialWindow.setVisible(true);
        this.guiGameList.setVisible(false);
    }

    /**
     * Shows on the {@link GUIMap} the new position of the client.
     */
    private void updatePosition() {
        Coordinate newCoordinates = this.client.getPlayer().getCurrentSector().getCoordinate();
        String playerName = /*name*/"";
        this.guiGamePane.getMapPane().delightAllSectors();
        this.guiGamePane.getMapPane().lightSector(newCoordinates, "Y",
                playerName);
    }


    public JFrame getFrame() {
        return this.mainFrame;
    }


    /**
     * Changes the card/map menus as a consequence of the excessive number of object
     * card in the client's private deck, so that the client is forced to discard or use one of those.
     */
    private void manyCardHandler() {
        PlayerType playerType = this.client.getPlayer().getPlayerToken().getPlayerType();
        if (playerType.equals(PlayerType.HUMAN)) {
            this.guiGamePane.changeCardMenu(MenuType.HUMAN_USE_DISC_MENU);
        } else {
            this.guiGamePane.changeCardMenu(MenuType.ALIEN_CARD_MENU);
        }

        this.guiGamePane.setStateMessage("Use or discard an object card");
        this.guiGamePane.getMapPane().changeMapMenu(MenuType.EMPTY);
    }

    /**
     * A reaction to the fact that either humans or aliens or both have won the game.
     * This reaction includes displaying some messages, updating card/map menus and disabling the 'end turn' button.
     */
    public void setWinnersReaction(boolean humansHaveWon, boolean aliensHaveWon) {
        if (humansHaveWon && aliensHaveWon) {
            this.guiGamePane.setStateMessage("Aliens and Humans have won");
        } else if (humansHaveWon) {
            this.guiGamePane.setStateMessage("Humans have won");
        } else {
            this.guiGamePane.setStateMessage("Aliens have won");
        }
        this.guiGamePane.changeCardMenu(MenuType.EMPTY);
        this.guiGamePane.getMapPane().changeMapMenu(MenuType.EMPTY);
        this.guiGamePane.refreshCardPanel(new ArrayList<ObjectCard>());
        this.guiGamePane.showEndTurnButton(false);
        (new Timer()).schedule(new EndGameTimeout(),DELAY_RETURN_TO_GAME_LIST);
    }

    /**
     * A reaction to some changes in the game state of the client.
     * This reaction includes updating card/map menus, refreshing the object cards panel and displaying some messages.
     */
    public void setPlayerStateReaction() {
        PlayerState playerState = this.client.getPlayer().getPlayerState();
        PSClientNotification psClientNotification = this.clientServices.getCurrentPsNotification();
        if (playerState.equals(PlayerState.ESCAPED)) {
            this.guiGamePane.setStateMessage("You've ESCAPED!");
            this.guiGamePane.changeCardMenu(MenuType.EMPTY);
            this.guiGamePane.getMapPane().changeMapMenu(MenuType.EMPTY);
            this.guiGamePane.refreshCardPanel(new ArrayList<ObjectCard>());
            this.guiGamePane.showEndTurnButton(false);
        } else if (playerState.equals(PlayerState.DEAD) && !(psClientNotification.getAlienWins() || psClientNotification.getHumanWins())) {
            this.guiGamePane.setStateMessage("You're DEAD!");
            this.guiGamePane.changeCardMenu(MenuType.EMPTY);
            this.guiGamePane.getMapPane().changeMapMenu(MenuType.EMPTY);
            this.guiGamePane.refreshCardPanel(new ArrayList<ObjectCard>());
            this.guiGamePane.showEndTurnButton(false);
        }
    }

    /**
     * A reaction to publishing a message in the game chat by the client.
     * This reaction includes showing the actual message in the chat panel.
     */
    public void publishChatMessage(String message) {
        this.guiGamePane.appendMsg(message);
    }

    /**
     * A reaction to starting his turn by the client.
     * This reaction includes setting/resetting of cards/map menus.
     */
    public void startTurn() {
        String message;
        PlayerType playerType = this.client.getPlayer().getPlayerToken().getPlayerType();
        if (playerType.equals(PlayerType.ALIEN)) {
            message = /*name*/" now is your turn: move or attack";
            this.guiGamePane.getMapPane().changeMapMenu(MenuType.ALIEN_INITIAL);
        } else {
            message = /*name*/ "now is your turn: move or use an object card";
            this.guiGamePane.getMapPane().changeMapMenu(MenuType.HUMAN_INITIAL);
            this.guiGamePane.changeCardMenu(MenuType.HUMAN_USE_MENU);
        }
        this.guiGamePane.setStateMessage(message);
    }

    /**
     * A reaction to ending his turn by the client.
     * This reaction includes setting/resetting cards/map menus and disabling the end turn button.
     */
    public void endTurn() {
        this.guiGamePane.getMapPane().changeMapMenu(MenuType.EMPTY);
        this.guiGamePane.changeCardMenu(MenuType.EMPTY);
        this.guiGamePane.showEndTurnButton(false);
    }

    public void displayResponseMsg(String msg) {
        this.guiGamePane.setStateMessage(msg);
    }

    /**
     * A reaction to discarding object card by the client.
     * This reaction includes refreshing the object card panel.
     */
    public void discardObjectCardReaction() {
        this.guiGamePane.refreshCardPanel(this.client.getPlayer().getPrivateDeck().getContent());
    }

    /**
     * A reaction to using a teleport object card by the client.
     * This reaction includes moving the client to his original sector.
     */
    public void teleportToStartingSectorReaction() {
        this.guiGamePane.getMapPane().delightAllSectors();
        this.guiGamePane.getMapPane().lightSector(this.client.getPlayer().getCurrentSector().getCoordinate(), "Y", ""/*name*/);
    }

    /**
     * A reaction to using an attack object card by the client.
     * This reaction includes asking to the client what sector he wants to attack and
     * setting/resetting cards/map menus.
     */
    public void askForSectorToAttackReaction() {
        this.guiGamePane.setStateMessage("Indicate the sector to attack");
        this.guiGamePane.getMapPane().changeMapMenu(MenuType.ATTACK_MENU);
    }

    /**
     * A reaction to using a lights sector card by the client. This
     * reaction includes asking to the client what sector he wants to light and includes setting/resetting
     * cards/map menus.
     */
    public void askForSectorToLightReaction() {
        this.guiGamePane.setStateMessage("Indicate the sector to light");
        this.guiGamePane.getMapPane().changeMapMenu(MenuType.LIGHT_MENU);
    }

    /**
     * A reaction to using an object card by the client.
     * This reaction includes updating the object cards shown in the card panel and includes displaying various messages.
     */
    public void useObjectCardReaction(ObjectCard objectCard) {
        if (objectCard instanceof DefenseObjectCard) {
            this.guiGamePane.setStateMessage("You have succesfully defended from an attack");
        }
        if (objectCard instanceof LightsObjectCard) {
            for (Sector sector : this.clientServices.getCurrentRrNotification().getLightedSectors()) {
                this.guiGamePane.getMapPane().lightSector(sector.getCoordinate(), "A", "Here there's an alien");
            }
        }
        this.guiGamePane.refreshCardPanel(this.client.getPlayer().getPrivateDeck().getContent());
    }

    /**
     * A reaction to moving to a sector by the client. This reaction includes
     * updating gui components so that the new position of the client is shown on the map, and
     * includes setting/resetting map/cards menus
     */
    public void moveToSectorReaction() {
        this.updatePosition();
        this.guiGamePane.showEndTurnButton(true);
        this.guiGamePane.getMapPane().changeMapMenu(MenuType.EMPTY);
    }

    /**
     * A reaction to object/sector cards by the client. This reaction includes
     * setting/resetting map/card menus and verifying if there are too many object cards and
     * so one must be discarded
     *
     * @param action The action that has triggered this reaction.
     */
    public void setDrawnSectorObjectCardReaction(ObjectCard drawnObjectCard, SectorCard drawnSectorCard) {
        CardSplashScreen cardSplashScreen = new CardSplashScreen(this.mainFrame);
        cardSplashScreen.showCards(drawnSectorCard, drawnObjectCard);
        PrivateDeck clientPrivateDeck = this.client.getPlayer().getPrivateDeck();
        //The action has not been validated by the server so only a message is shown

        if (drawnSectorCard instanceof GlobalNoiseSectorCard) {
            this.guiGamePane
                    .setStateMessage("Select a sector for the global noise card");
            this.guiGamePane.getMapPane().changeMapMenu(
                    MenuType.NOISE_MENU);
            this.guiGamePane.showEndTurnButton(false);
        } else if (drawnSectorCard instanceof LocalNoiseSectorCard) {
            this.guiGamePane.setStateMessage("You will make noise in your sector");
            if (clientPrivateDeck.getSize() > 3) {
                this.manyCardHandler();
            } else {
                this.guiGamePane.showEndTurnButton(true);
            }
        } else if (drawnSectorCard instanceof SilenceSectorCard) {
            this.guiGamePane.setStateMessage("You will make no noise");
            if (clientPrivateDeck.getSize() > 3) {
                this.manyCardHandler();
            } else {
                this.guiGamePane.showEndTurnButton(true);
            }
        } else {
            this.guiGamePane.setStateMessage(this.clientServices.getCurrentRrNotification().getMessage());
            this.guiGamePane.getMapPane().changeMapMenu(MenuType.EMPTY);
            if (clientPrivateDeck.getSize() > 3) {
                this.manyCardHandler();
            } else {
                this.guiGamePane.showEndTurnButton(true);
            }
        }
        if (drawnObjectCard != null) {
            this.guiGamePane.addCardToPanel(drawnObjectCard);
        }

    }

    /**
     * Switches to the {@link client.GUIGamePane} view from the {@link client.GUIGameList} and
     * acts on this view in response to the starting of the game
     */
    public void startGameReaction() {
        String characterInfoMsg;
        this.gameListRefreshTimer.cancel();
        if (this.client.getPlayer().getPlayerToken().getPlayerType().equals(PlayerType.ALIEN)) {
            characterInfoMsg = /*name*/"" + " | ALIEN";
            this.guiGamePane.setSectorMenu(MenuType.ALIEN_INITIAL);
        } else {
            characterInfoMsg = /*name*/"" + " | HUMAN";
            this.guiGamePane.setSectorMenu(MenuType.HUMAN_INITIAL);
        }
        this.guiGameList.reset();
        this.mainFrame.remove(this.guiGameList);
        this.guiGamePane.load(this.client.getGameMap());
        this.mainFrame.add(this.guiGamePane);
        this.guiGamePane.setVisible(true);
        this.guiGamePane.setInfoMsg(characterInfoMsg);
        this.guiGamePane.getMapPane().lightSector(
                this.client.getPlayer().getCurrentSector().getCoordinate(), "Y", /*name*/"");
        this.mainFrame.validate();
        this.mainFrame.repaint();
    }

    /**
     * Makes the different gui components of the app signal the absence of a connection
     * with the server.
     */
    public void setConnectionActiveReaction(boolean isConnectionActive) {
        this.guiInitialWindow.alertConnectionProblem(isConnectionActive);
        this.guiGameList.alertConnectionProblem(isConnectionActive);
        this.guiGamePane.alertConnectionProblem(isConnectionActive);
    }

    /**
     * Switches to the {@link client.GUIGameList} view from the {@link client.GUInitialWindow}.
     */
    public void setAvailableGamesReaction() {
        if (!this.guiGameList.isVisible()) {
            //this.gameListRefreshTimer.scheduleAtFixedRate(new GamePollingThread(),1000,this.GAME_LIST_REFRESH_RATE);
            this.mainFrame.remove(this.guiInitialWindow);
            this.guiGameList
                   .setLayout(new BoxLayout(this.guiGameList, BoxLayout.Y_AXIS));
            this.guiGameList.setBackground(Color.BLACK);
            this.guiGameList.load();
            mainFrame.getContentPane().add(this.guiGameList);
            this.guiGameList.setVisible(true);
            this.mainFrame.validate();
            this.mainFrame.repaint();
        }
        else {
            this.guiGameList.setGameListContent(this.clientServices.getAvailableGames());
        }




    }
    public void signalStartableGame(){
        this.guiGameList.startableGame();
    }

    public void returnToGameList() {
        this.mainFrame.remove(this.guiGamePane);
        this.mainFrame.add(this.guiGameList);
        this.guiGameList.setVisible(true);
        this.mainFrame.validate();
        this.mainFrame.repaint();
    }
}
