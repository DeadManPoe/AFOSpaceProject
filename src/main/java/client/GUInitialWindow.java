package client;

import client_gui.GuiManager;
import client_store.InteractionManager;

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

	private final InteractionManager interactionManager = InteractionManager.getInstance();

	private JLabel connectionProblemLabel;
	/**
	 * Load and display the panel
	 */
	public void load() {
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

		this.connectionProblemLabel = new JLabel();
		this.connectionProblemLabel.setText("A connection problem happened, please try again.");
		this.connectionProblemLabel.setForeground(Color.white);
		this.connectionProblemLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        this.connectionProblemLabel.setVisible(false);
        // Some space
        add(Box.createRigidArea(new Dimension(0, 20)));
        add(this.connectionProblemLabel);
		connectButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
               interactionManager.getGames();
            }
		});
	}
	public void alertConnectionProblem(boolean visibility){
		this.connectionProblemLabel.setVisible(visibility);
	}
}
