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

	private transient GuiInteractionManager gui;

	/**
	 * Constructs a panel that shows the list of available games
	 * 
	 * @param gui
	 */
	public GUIGameList(GuiInteractionManager gui) {
		this.gui = gui;
	}

	/**
	 * Loads the information on the panel that shows the list of available
	 * games, and allows the player to join an existing game or to join a new
	 * one
	 * 
	 * @param connMethod
	 *            the type of connection to use during the game (RMI/SOCKET)
	 */
	public void Load(String connMethod) throws IllegalAccessException,
			InvocationTargetException, NoSuchMethodException,
			ClassNotFoundException, IOException, NotBoundException {
		stateMessage.setFont(new Font("Arial", Font.BOLD, 22));
		stateMessage.setForeground(Color.WHITE);
		stateMessage.setAlignmentX(CENTER_ALIGNMENT);

		gui.getClient()
				.buildDataRemoteExchangeFactory(connMethod.toUpperCase());

		JTable gameTables = new JTable();

		GamePollingThread pollingThread = new GamePollingThread(gui, gameTables);
		final Timer timerTask = new Timer();
		pollingThread.updateGames();

		timerTask.scheduleAtFixedRate(pollingThread, 0, 10000);

		JScrollPane scroll = new JScrollPane(gameTables);
		gameTables.setFillsViewportHeight(true);
		
		scroll.setMaximumSize(new Dimension(800, 250));

		add(scroll);

		add(Box.createRigidArea(new Dimension(0, 20)));

		buttonPanel = new JPanel();

		JButton joinButton = new JButton("Join");
		joinButton.setAlignmentX(JButton.CENTER_ALIGNMENT);
		buttonPanel.add(joinButton);

		joinButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					stateMessage.setText("Waiting for others players...");
					// A box for the name
					String name = JOptionPane.showInputDialog(gui.getFrame(),
							"Insert the player name: ",
							JOptionPane.OK_CANCEL_OPTION);
					if (!("").equals(name)) {
						JTable gameTables = (JTable) ((JViewport) ((JScrollPane) ((JButton) e
								.getSource()).getParent().getParent()
								.getComponent(0)).getComponent(0)).getView();
						if (gameTables.getSelectedRowCount() == 1) {
							int id = (Integer) gameTables.getValueAt(
									gameTables.getSelectedRow(), 0);
							String status = ((GameStatus) gameTables
									.getValueAt(gameTables.getSelectedRow(), 1))
									.toString();
							if (!status.equals("CLOSED")) {
								try {
									gui.JoinGame(id, name);
								} catch (IllegalAccessException
										| InvocationTargetException
										| NoSuchMethodException
										| ClassNotFoundException | IOException
										| NotBoundException e1) {
									ClientLogger.getLogger().log(Level.SEVERE,
											e1.getMessage(), e1);
								}
							} else
								stateMessage.setText("");
						} else {
							gui.showMessageBox("No game selected!");
							stateMessage.setText("");
						}
					} else {
						gui.showMessageBox("Not a valid player name!");
					}
				} catch (IllegalArgumentException | SecurityException e1) {
					ClientLogger.getLogger().log(Level.SEVERE, e1.getMessage(),
							e1);
				} catch (InterruptedException e1) {
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
				try {

					Object[] possibilities = { "Galilei", "Fermi", "Galvani" };
					String name = (String) JOptionPane.showInputDialog(
							gui.getFrame(), "Insert the player name: ",
							"Player name", JOptionPane.PLAIN_MESSAGE);
					if (name != null)
						stateMessage.setText("Waiting for other player");
					mapName = (String) JOptionPane.showInputDialog(
							gui.getFrame(), "Choose the map: ", "Map",
							JOptionPane.PLAIN_MESSAGE, null, possibilities,
							"Galilei");
					if (!mapName.equals("")) {
						gui.JoinNewGame(name, mapName.toUpperCase());
						startButton.setVisible(true);
						buttonPanel.setVisible(false);
					} else
						stateMessage.setText("");
				} catch (IllegalArgumentException | SecurityException e1) {
					ClientLogger.getLogger().log(Level.SEVERE, e1.getMessage(),
							e1);
				} catch (RemoteException e2) {
					ClientLogger.getLogger().log(Level.SEVERE, e2.getMessage(),
							e2);
				} catch (IllegalAccessException e2) {
					ClientLogger.getLogger().log(Level.SEVERE, e2.getMessage(),
							e2);
				} catch (InvocationTargetException e2) {
					ClientLogger.getLogger().log(Level.SEVERE, e2.getMessage(),
							e2);
				} catch (NoSuchMethodException e2) {
					ClientLogger.getLogger().log(Level.SEVERE, e2.getMessage(),
							e2);
				} catch (ClassNotFoundException e2) {
					ClientLogger.getLogger().log(Level.SEVERE, e2.getMessage(),
							e2);
				} catch (IOException e2) {
					ClientLogger.getLogger().log(Level.SEVERE, e2.getMessage(),
							e2);
				} catch (NotBoundException e2) {
					ClientLogger.getLogger().log(Level.SEVERE, e2.getMessage(),
							e2);
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
				try {
					gui.forceGameStart();
				} catch (IllegalAccessException | InvocationTargetException
						| NoSuchMethodException | ClassNotFoundException
						| IOException | NotBoundException e1) {
					ClientLogger.getLogger().log(Level.SEVERE, e1.getMessage(),
							e1);
				}
			}
		});

		addComponentListener(new ComponentAdapter() {
			public void componentHidden(ComponentEvent e) {
				timerTask.cancel();
				timerTask.purge();
			}
		});

	}

}
