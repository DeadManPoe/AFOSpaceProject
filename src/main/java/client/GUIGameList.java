package client;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import common.GamePublicData;

/**
 * Represents the panel that shows the list of available games
 *
 */
public class GUIGameList extends JPanel {

    private final JLabel connectionAlert = new JLabel("The connection with the server is not active");
    private final ClientServices clientServices = ClientServices.getInstance();
    private final GuiManager guiManager = GuiManager.getInstance();
    private JLabel stateMessage = new JLabel("");
    private JButton startButton = new JButton("Start Game");
    private final JButton joinButton = new JButton("Join");
    private JPanel buttonPanel;
    private DefaultTableModel gameList;
    private JTable gameTables;

    /**
     * Loads the information on the panel that shows the list of available
     * games, and allows the player to join an existing game or to join a new
     * one
     *
     */
    public void load() {
        add(this.connectionAlert);
        stateMessage.setFont(new Font("Arial", Font.BOLD, 15));
        stateMessage.setForeground(Color.WHITE);
        stateMessage.setAlignmentX(CENTER_ALIGNMENT);

        gameTables = new JTable();
        this.gameList = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;//This causes all cells to be not editable
            }
        };
        gameTables.setModel(this.gameList);
        this.gameList.addColumn("Game ID");
        this.gameList.addColumn("Game status");
        this.gameList.addColumn("Players");


        JScrollPane scroll = new JScrollPane(gameTables);
        gameTables.setFillsViewportHeight(true);

        scroll.setMaximumSize(new Dimension(800, 250));

        add(scroll);

        add(Box.createRigidArea(new Dimension(0, 20)));

        buttonPanel = new JPanel();

        joinButton.setEnabled(false);
        joinButton.setAlignmentX(JButton.CENTER_ALIGNMENT);
        buttonPanel.add(joinButton);

        gameTables.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (gameTables.getSelectedRowCount() == 1) {
                    if (!gameTables.getValueAt(gameTables.getSelectedRow(), 1).equals("CLOSED")) {
                        joinButton.setEnabled(true);
                    }
                }
            }
        });

        joinButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                joinGame();
            }
        });

        final JButton newButton = new JButton("New");
        newButton.setAlignmentX(JButton.CENTER_ALIGNMENT);
        buttonPanel.add(newButton);
        final JButton refreshButton = new JButton("Refresh");
        refreshButton.setAlignmentX(JButton.CENTER_ALIGNMENT);
        buttonPanel.add(refreshButton);
        newButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                newGame();
            }
        });
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clientServices.getGames();
            }
        });

        buttonPanel.setMaximumSize(buttonPanel.getPreferredSize());
        buttonPanel.setBackground(Color.BLACK);
        add(buttonPanel);
        add(Box.createRigidArea(new Dimension(0, 20)));
        add(stateMessage);
        add(Box.createRigidArea(new Dimension(0, 20)));
        add(startButton);
        startButton.setVisible(false);
        startButton.setAlignmentX(JButton.CENTER_ALIGNMENT);

        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clientServices.onDemandGameStart();
            }
        });

    }

    public void setGameListContent(List<GamePublicData> gamePublicDataList) {
        if(this.gameList != null){
            for (int i = 0; i < this.gameList.getRowCount(); i++) {
                this.gameList.removeRow(i);
            }
            for (GamePublicData gameDate : gamePublicDataList) {
                this.gameList.addRow(new Object[]{gameDate.getId(), gameDate.getStatus(), gameDate.getPlayersCount()});
            }
        }
    }

    /**
     * Manages interaction related to the join of a game by the client.
     * This interaction includes asking to the client what name he should have in the game.
     *
     */
    private void joinGame(){
        // A box for the name
        String playerName = (String) JOptionPane.showInputDialog(this.getParent(),
                "Insert your name for the game: ",
                "Name insert",
                JOptionPane.OK_CANCEL_OPTION, null, null, "");
        if (playerName != null){
            if (!("").equals(playerName)) {
                int gameId = (Integer) gameTables.getValueAt(
                        gameTables.getSelectedRow(), 0);
                stateMessage.setText("Waiting for others players...");
                clientServices.joinGame(gameId, playerName);
                buttonPanel.setVisible(false);
            } else {
                JOptionPane.showMessageDialog(guiManager.getFrame(),
                        "Please insert a valid name",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }

    }

    /**
     * Manages interaction related to the creation of a new game by the client.
     * This interaction includes asking to the client what name he should have in the game and what map
     * the game should be based on
     */
    private void newGame(){
        Object[] possibilities = {"Galilei", "Fermi", "Galvani"};
        String playerName = (String) JOptionPane.showInputDialog(this.getParent(),
                "Insert your name for the game: ",
                "Name insert",
                JOptionPane.OK_CANCEL_OPTION, null, null, "");
        if (playerName != null){
            if (!("").equals(playerName)) {
                String mapName = (String) JOptionPane.showInputDialog(
                        this.getParent(), "Choose the map: ", "Map",
                        JOptionPane.PLAIN_MESSAGE, null, possibilities,
                        "Galilei");
                if(mapName != null){
                    stateMessage.setText("Waiting for others players...");
                    this.clientServices.joinNewGame(mapName.toUpperCase(),playerName);
                    this.clientServices.getGames();
                    buttonPanel.setVisible(false);
                }


            } else {
                JOptionPane.showMessageDialog(guiManager.getFrame(),
                        "Please insert a valid name",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void startableGame() {
        this.startButton.setVisible(true);
    }

    public void alertConnectionProblem(boolean isConnectionActive) {
        this.connectionAlert.setVisible(!isConnectionActive);
    }
    public void reset(){
        for (int i = 0; i < this.gameList.getRowCount(); i++) {
            this.gameList.removeRow(i);
        }
        this.joinButton.setEnabled(false);
        stateMessage.setText("");
        this.startButton.setVisible(false);
        this.buttonPanel.setVisible(true);
    }
}