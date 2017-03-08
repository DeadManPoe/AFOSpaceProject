package client;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.rmi.NotBoundException;
import java.util.logging.Level;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Represents the initial window of the GUI in which the user can choose the type of connection
 * @author Andrea Sessa
 *
 */
public class GUInitialWindow extends JPanel {
	private static final long serialVersionUID = 1L;

	private transient GuiInteractionManager gui;
	JComboBox<String> connectionMethodComboBox;

	public GUInitialWindow(GuiInteractionManager gui) {
		this.gui = gui;
		String[] method = { "Socket", "RMI" };
		connectionMethodComboBox = new JComboBox<String>(method);
		connectionMethodComboBox.setMaximumSize(connectionMethodComboBox
				.getPreferredSize());
		connectionMethodComboBox.setAlignmentX(JComboBox.CENTER_ALIGNMENT);
	}

	/**
	 * Load and display the panel
	 */
	public void Load() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setBackground(Color.BLACK);

		JLabel title = new JLabel();
		title.setIcon(new ImageIcon("mainImg.jpg"));
		title.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		add(title);

		add(connectionMethodComboBox);

		// Some space
		add(Box.createRigidArea(new Dimension(0, 20)));

		JButton connectButton = new JButton("Connect!");
		connectButton.setAlignmentX(JButton.CENTER_ALIGNMENT);
		add(connectButton);
		connectButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					gui.ConnectAction((String) connectionMethodComboBox
							.getSelectedItem());
				} catch (IllegalAccessException | IllegalArgumentException
						| InvocationTargetException | NoSuchMethodException
						| SecurityException | ClassNotFoundException
						| IOException | NotBoundException e1) {
					ClientLogger.getLogger().log(Level.SEVERE, e1.getMessage(),
							e1);
				}
			}
		});
	}
}
