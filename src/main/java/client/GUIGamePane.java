package client;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.rmi.NotBoundException;
import java.util.logging.Level;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

import common.ObjectCard;
import it.polimi.ingsw.cg_19.GameMap;

/**
 * Represents the main window in which are the map, log, cards, etc... are
 * displayed
 * 
 * @author Andrea Sessa
 * @author Giorgio Pea
 * @version 1.0
 */
public class GUIGamePane extends JPanel {
	private static final long serialVersionUID = 1L;

	private transient GuiInteractionManager gui;

	private DefaultListModel<String> logModel;
	private JList<String> logPane;
	private JScrollPane logScrollPane;

	private JLabel stateLabel;

	private JPanel inputPane;
	private JPanel rightPanel;
	private JPanel cardsPane;

	private JPanel holdingPanel;
	private GUIMap mapPanel;

	private JButton endTurnButton;
	private JButton msgButton;

	private JTextField chatTextField;

	private JPopupMenu humanUseCardMenu = new JPopupMenu();
	private JPopupMenu humanUseDiscCardMenu = new JPopupMenu();
	private JPopupMenu alienCardMenu = new JPopupMenu();
	private JPopupMenu emptyMenu = new JPopupMenu();
	private JPopupMenu attackMenu = new JPopupMenu();

	private JPopupMenu currentCardMenu = new JPopupMenu();

	private ObjectCard selectedObjectCard;

	public GUIGamePane(final GuiInteractionManager gui) {
		mapPanel = new GUIMap(gui);
		this.gui = gui;
		logModel = new DefaultListModel<String>();
		logPane = new JList<String>(logModel);
		logScrollPane = new JScrollPane();
		logScrollPane.setViewportView(logPane);
		holdingPanel = new JPanel();

		stateLabel = new JLabel();
		stateLabel.setFont(new Font("Arial", Font.BOLD, 25));
		stateLabel.setForeground(Color.LIGHT_GRAY);
		inputPane = new JPanel();
		rightPanel = new JPanel(new GridBagLayout());
		cardsPane = new JPanel(new FlowLayout());

		JMenuItem humanDiscardItem = new JMenuItem("Discard");
		JMenuItem useCardItem = new JMenuItem("Use Card");
		this.humanUseDiscCardMenu.add(humanDiscardItem);
		this.humanUseDiscCardMenu.add(useCardItem);

		JMenuItem humanUseOnlyItem = new JMenuItem("Use card");
		this.humanUseCardMenu.add(humanUseOnlyItem);

		JMenuItem alienDiscardItem = new JMenuItem("Discard");
		alienCardMenu.add(alienDiscardItem);

		endTurnButton = new JButton("End Turn!");
		msgButton = new JButton("Send");

		// Button events definition
		endTurnButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					gui.endTurn();
				} catch (IllegalAccessException | IllegalArgumentException
						| InvocationTargetException | NoSuchMethodException
						| SecurityException | ClassNotFoundException
						| IOException | NotBoundException e1) {
					ClientLogger.getLogger().log(Level.SEVERE, e1.getMessage(),
							e1);
				}
			}
		});

		msgButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					gui.sendGlobalMessage(chatTextField.getText());
				} catch (IllegalAccessException | IllegalArgumentException
						| InvocationTargetException | NoSuchMethodException
						| SecurityException | ClassNotFoundException
						| IOException | NotBoundException e1) {
					ClientLogger.getLogger().log(Level.SEVERE, e1.getMessage(),
							e1);
				}
				chatTextField.setText("");
			}
		});

		// Event definitions
		useCardItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					gui.useObjectCard(selectedObjectCard);
				} catch (IllegalAccessException | IllegalArgumentException
						| InvocationTargetException | NoSuchMethodException
						| SecurityException | ClassNotFoundException
						| IOException | NotBoundException e1) {
					ClientLogger.getLogger().log(Level.SEVERE, e1.getMessage(),
							e1);
				}
			}
		});
		humanUseOnlyItem.addActionListener(useCardItem.getActionListeners()[0]);

		alienDiscardItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					gui.discard(selectedObjectCard);
				} catch (IllegalAccessException | IllegalArgumentException
						| InvocationTargetException | NoSuchMethodException
						| SecurityException | ClassNotFoundException
						| IOException | NotBoundException e1) {
					ClientLogger.getLogger().log(Level.SEVERE, e1.getMessage(),
							e1);
				}
			}
		});
		humanDiscardItem.addActionListener(alienDiscardItem
				.getActionListeners()[0]);

		currentCardMenu = emptyMenu;
	}

	/**
	 * Gets the panel containing the game map
	 * 
	 * @return the panel containing the game map
	 */
	public GUIMap getMapPane() {
		return this.mapPanel;
	}

	/**
	 * Inits the gui for the user displaying the mapName game map
	 */
	public void load(GameMap gameMap) {

		mapPanel.displayGameMap(gameMap);

		GridBagConstraints c = new GridBagConstraints();

		setMaximumSize(getPreferredSize());
		setBackground(Color.BLACK);

		mapPanel.setPreferredSize(new Dimension(800, 588));

		c.anchor = GridBagConstraints.WEST;
		c.gridx = 0;
		c.gridy = 0;
		mapPanel.setVisible(true);
		add(mapPanel, c);

		c.gridy = 1;
		stateLabel.setMaximumSize(stateLabel.getPreferredSize());
		add(stateLabel, c);

		rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
		logScrollPane.setPreferredSize(new Dimension(325, 190));

		holdingPanel.setMaximumSize(new Dimension(325, 200));
		holdingPanel.add(logScrollPane);
		holdingPanel.setBackground(Color.BLACK);
		logScrollPane
				.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		logScrollPane
				.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		logModel.addElement("Welcome to Escape from aliens in outer space");
		rightPanel.add(holdingPanel);

		chatTextField = new JTextField(20);
		chatTextField.setMaximumSize(chatTextField.getPreferredSize());
		msgButton.setMaximumSize(new Dimension(200, 50));
		inputPane.setLayout(new FlowLayout());
		inputPane.add(chatTextField);
		inputPane.add(msgButton);
		inputPane.setMaximumSize(inputPane.getPreferredSize());
		rightPanel.add(inputPane);

		rightPanel.add(Box.createRigidArea(new Dimension(0, 20)));

		endTurnButton.setPreferredSize(new Dimension(150, 20));
		endTurnButton.setMaximumSize(new Dimension(300, 20));
		endTurnButton.setAlignmentX(JButton.CENTER_ALIGNMENT);
		endTurnButton.setEnabled(false);
		endTurnButton.setIcon(new ImageIcon("endBtn.png"));
		rightPanel.add(endTurnButton);

		rightPanel.add(Box.createRigidArea(new Dimension(0, 20)));

		cardsPane.setBackground(Color.BLACK);
		cardsPane.setMaximumSize(new Dimension(350, 600));
		rightPanel.add(cardsPane);

		rightPanel.setBackground(Color.BLACK);
		rightPanel.setMaximumSize(new Dimension(400, 650));
		rightPanel.setPreferredSize(new Dimension(400, 675));

		c = new GridBagConstraints();
		c.gridy = 0;
		c.gridx = 1;
		c.anchor = GridBagConstraints.EAST;
		add(rightPanel, c);

		gui.getFrame().add(this);
	}

	/**
	 * Appends a message to the editor pane
	 */
	public void appendMsg(String msg) {
		this.logModel.addElement(msg);
	}

	/**
	 * Changes the state message to be shown to the player
	 * 
	 * @param msg
	 *            the new state message to be shown to the player
	 */
	public void setStateMessage(String msg) {
		this.stateLabel.setText(msg);
	}

	/**
	 * Displays an object card in the proper panel
	 * 
	 * @param objectCard
	 *            the object card to display in the proper panel
	 */
	public void addCardToPanel(ObjectCard objectCard) {
		ResourceMapper mapper = new ResourceMapper();
		ObjectCardLabel cardLbl = new ObjectCardLabel(objectCard);
		cardLbl.setIcon(new ImageIcon(mapper.getCardImage(objectCard)));
		cardLbl.setMaximumSize(cardLbl.getPreferredSize());
		cardsPane.add(cardLbl);

		cardLbl.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				ObjectCardLabel l = (ObjectCardLabel) e.getSource();
				selectedObjectCard = l.getCard();
				showMenu(l, e);
			}
		});
		this.cardsPane.repaint();
	}

	/**
	 * Removes all the cards from the card panel
	 */
	public void removeAllCardsFromPanel() {
		cardsPane.removeAll();
		cardsPane.repaint();
	}

	/**
	 * Changes the menu displayed when the user clicks on a object card label
	 * 
	 * @param type
	 *            the type of the menu to be displayed
	 */
	public void changeCardMenu(MenuType type) {
		switch (type) {
		case HUMAN_USE_MENU:
			this.currentCardMenu = this.humanUseCardMenu;
			break;
		case HUMAN_USE_DISC_MENU:
			this.currentCardMenu = this.humanUseDiscCardMenu;
			break;
		case ALIEN_CARD_MENU:
			this.currentCardMenu = alienCardMenu;
			break;
		default:
			this.currentCardMenu = emptyMenu;
			break;
		}
	}

	/**
	 * Displays on the screen the given object card label
	 */
	private void showMenu(ObjectCardLabel lbl, MouseEvent e) {
		this.currentCardMenu.show(lbl, e.getX(), e.getY());
	}

	/**
	 * Enables or not the end turn button
	 * 
	 * @param show
	 *            the flag that indicates if the end button should be enabled or
	 *            not
	 */
	public void showEndTurnButton(boolean show) {
		this.endTurnButton.setEnabled(show);
	}
}