package client;

import client_gui.GuiManager;
import client_store.ClientStore;
import client_store_actions.ClientConnectRetrieveGamesAction;
import client_store_actions.ClientSetAvGamesAction;
import common.GamePublicData;
import server_store.StoreAction;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * Represents the panel that shows the list of available games
 *
 * @author Andrea Sessa
 * @author Giorgio Pea
 */
public class GUIGameList extends JPanel implements Observer {
    
    private DefaultTableModel gameList;
    private JButton startButton;
    private final GuiManager guiManager;

    public GUIGameList() {
        this.guiManager = GuiManager.getInstance();
        //Registers observer
        ClientStore.getInstance().observeState(this);
    }

    /**
     * Loads the information on the panel that shows the list of available
     * games, and allows the player to join an existing game or to join a new
     * one
     *
     */
    public void load() {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBackground(Color.BLACK);
        final JTable gameTables =  new JTable();
        final JLabel stateMessage = new JLabel("");
        this.startButton = new JButton("Start Game");
        final JPanel buttonPanel = new JPanel();
        JScrollPane scrollableGamesPanel = new JScrollPane(gameTables);
        final JButton joinButton = new JButton("Join Game");
        JButton joinNewButton = new JButton("New Game");
        stateMessage.setFont(new Font("Arial", Font.BOLD, 18));
        stateMessage.setForeground(Color.WHITE);
        stateMessage.setAlignmentX(CENTER_ALIGNMENT);

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
        
        gameTables.setFillsViewportHeight(true);
        scrollableGamesPanel.setMaximumSize(new Dimension(800, 250));

        add(scrollableGamesPanel);
        //Some vertical space
        add(Box.createRigidArea(new Dimension(0, 20)));
        
        
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
                try {
                    // A box for the name
                    String name = (String) JOptionPane.showInputDialog(guiManager.getFrame(),
                            "Insert your name for the game: ",
                            "Name insert",
                            JOptionPane.OK_CANCEL_OPTION, null, null, "");
                    if (name != null){
                        if (!("").equals(name)) {
                            int id = (Integer) gameTables.getValueAt(
                                    gameTables.getSelectedRow(), 0);
                            stateMessage.setText("Waiting for others players...");
                            guiManager.joinGame(id, name);
                        } else {
                            JOptionPane.showMessageDialog(guiManager.getFrame(),
                                    "Please insert a valid name",
                                    "Error",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    }

                } catch (IllegalArgumentException | SecurityException e1) {
                    e1.printStackTrace();
                }
            }
        });
        
        joinNewButton.setAlignmentX(JButton.CENTER_ALIGNMENT);
        buttonPanel.add(joinNewButton);
        joinNewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Object[] possibilities = {"Galilei", "Fermi", "Galvani"};
                String name = (String) JOptionPane.showInputDialog(guiManager.getFrame(),
                        "Insert your name for the game: ",
                        "Name insert",
                        JOptionPane.OK_CANCEL_OPTION, null, null, "");
                if (name != null){
                    if (!("").equals(name)) {
                        String mapName = (String) JOptionPane.showInputDialog(
                                guiManager.getFrame(), "Choose the map: ", "Map",
                                JOptionPane.PLAIN_MESSAGE, null, possibilities,
                                "Galilei");
                        if(mapName != null){
                            stateMessage.setText("Waiting for others players...");
                            buttonPanel.setVisible(false);
                            guiManager.joinNewGame(mapName,name);

                        }


                    } else {
                        JOptionPane.showMessageDialog(guiManager.getFrame(),
                                "Please insert a valid name",
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }

            }
        });

        buttonPanel.setMaximumSize(buttonPanel.getPreferredSize());
        buttonPanel.setBackground(Color.BLACK);
        add(buttonPanel);
        //Some vertical space
        add(Box.createRigidArea(new Dimension(0, 20)));
        add(stateMessage);
        //Some vertical space
        add(Box.createRigidArea(new Dimension(0, 20)));
        add(startButton);
        startButton.setVisible(false);
        startButton.setAlignmentX(JButton.CENTER_ALIGNMENT);
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guiManager.onDemandGameStart();
            }
        });
    }

    private void setGameListContent(List<GamePublicData> gamePublicDataList) {
        if(this.gameList != null){
            for (int i = 0; i < this.gameList.getRowCount(); i++) {
                this.gameList.removeRow(i);
            }
            for (GamePublicData gameDate : gamePublicDataList) {
                this.gameList.addRow(new Object[]{gameDate.getId(), gameDate.getStatus(), gameDate.getPlayersCount()});
            }
        }
    }
    private void connectRetrieveGames(StoreAction action){
        ClientConnectRetrieveGamesAction castedAction = (ClientConnectRetrieveGamesAction) action;
        if (castedAction.payload){
            this.load();
            this.setVisible(true);
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        StoreAction action = (StoreAction) arg;
        switch (action.getType()) {
            case "@CLIENT_SET_AV_GAMES":
                ClientSetAvGamesAction castedAction = (ClientSetAvGamesAction) action;
                this.setGameListContent(castedAction.payload);
                break;
            case "@CLIENT_STARTABLE_GAME":
                this.startButton.setVisible(true);
                break;
            case "@CLIENT_CONNECT_RETRIEVE_GAMES":
                this.connectRetrieveGames(action);
                break;
        }
    }
}
