package client;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Timer;
import java.util.logging.Level;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import client_gui.GuiManager;
import common.GamePublicData;
import server.GameStatus;

/**
 * Represents the panel that shows the list of available games
 *
 * @author Andrea Sessa
 * @author Giorgio Pea
 */
public class GUIGameList extends JPanel {
    private static final long serialVersionUID = 1L;

    private JLabel stateMessage = new JLabel("");
    private JButton startButton = new JButton("Start Game");
    private JPanel buttonPanel;
    private String mapName;
    private DefaultTableModel gameList;

    private transient final GuiManager guiManager = GuiManager.getInstance();


    /**
     * Loads the information on the panel that shows the list of available
     * games, and allows the player to join an existing game or to join a new
     * one
     *
     */
    public void Load() {
        stateMessage.setFont(new Font("Arial", Font.BOLD, 22));
        stateMessage.setForeground(Color.WHITE);
        stateMessage.setAlignmentX(CENTER_ALIGNMENT);


        final JTable gameTables = new JTable();
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

        final JButton joinButton = new JButton("Join");
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
                    stateMessage.setText("Waiting for others players...");
                    // A box for the name
                    String name = (String) JOptionPane.showInputDialog(guiManager.getFrame(),
                            "Insert your name for the game: ",
                            "Name insert",
                            JOptionPane.OK_CANCEL_OPTION, null, null, "");
                    if (!("").equals(name)) {
                        int id = (Integer) gameTables.getValueAt(
                                gameTables.getSelectedRow(), 0);
                        String status = ((GameStatus) gameTables
                                .getValueAt(gameTables.getSelectedRow(), 1))
                                .toString();
                        guiManager.joinGame(id, name);
                    } else {
                        JOptionPane.showMessageDialog(guiManager.getFrame(),
                                "Please insert a valid name",
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                } catch (IllegalArgumentException | SecurityException e1) {
                    ClientLogger.getLogger().log(Level.SEVERE, e1.getMessage(),
                            e1);
                }
            }
        });

        JButton newButton = new JButton("New");
        newButton.setAlignmentX(JButton.CENTER_ALIGNMENT);
        buttonPanel.add(newButton);
        newButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Object[] possibilities = {"Galilei", "Fermi", "Galvani"};
                String name = (String) JOptionPane.showInputDialog(guiManager.getFrame(),
                        "Insert your name for the game: ",
                        "Name insert",
                        JOptionPane.OK_CANCEL_OPTION, null, null, "");
                if (!("").equals(name)) {
                    stateMessage.setText("Waiting for other players");
                    mapName = (String) JOptionPane.showInputDialog(
                            guiManager.getFrame(), "Choose the map: ", "Map",
                            JOptionPane.PLAIN_MESSAGE, null, possibilities,
                            "Galilei");

                    guiManager.JoinNewGame(mapName.toUpperCase(), name);
                    startButton.setVisible(true);
                    buttonPanel.setVisible(false);

                } else {
                    JOptionPane.showMessageDialog(guiManager.getFrame(),
                            "Please insert a valid name",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
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
                System.err.println("balla");
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

}
