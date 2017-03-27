package client;

import client_gui.GuiManager;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.rmi.NotBoundException;
import java.util.logging.Level;

import javax.swing.*;

/**
 * Represents the initial window of the GUI in which the user can choose the type of connection
 * @author Andrea Sessa
 *
 */
public class GUInitialWindow extends JPanel {
	private static final long serialVersionUID = 1L;

	private final transient GuiManager guiManager = GuiManager.getInstance();

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

		// Some space
		add(Box.createRigidArea(new Dimension(0, 20)));

		JButton connectButton = new JButton("Connect");
		connectButton.setAlignmentX(JButton.CENTER_ALIGNMENT);
		add(connectButton);
		connectButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				guiManager.connectAndDisplayGames();
			}
		});
	}
}
