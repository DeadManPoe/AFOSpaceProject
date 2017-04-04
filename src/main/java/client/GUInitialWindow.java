package client;

import client_gui.GuiManager;
import client_store.ClientStore;
import client_store_actions.ClientSetConnectionStatusAction;
import server_store.StoreAction;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

/**
 * Represents the initial window of the GUI in which the user can choose the type of connection
 * @author Andrea Sessa
 *
 */
public class GUInitialWindow extends JPanel implements Observer {

	private final GuiManager guiManager = GuiManager.getInstance();

	private JLabel connectionProblemLabel;


    public GUInitialWindow() {
        this.connectionProblemLabel = new JLabel();
        //Subscribes to state changes
        ClientStore.getInstance().observeState(this);
    }

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

		// Some vertical space
		add(Box.createRigidArea(new Dimension(0, 20)));

		JButton connectButton = new JButton("Connect");
		connectButton.setAlignmentX(JButton.CENTER_ALIGNMENT);
		add(connectButton);

		this.connectionProblemLabel.setText("A connection problem happened, please try again.");
		this.connectionProblemLabel.setForeground(Color.white);
		this.connectionProblemLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        this.connectionProblemLabel.setVisible(false);
        // Some vertical space space
        add(Box.createRigidArea(new Dimension(0, 20)));
        add(this.connectionProblemLabel);
		connectButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
                guiManager.connectAndDisplayGames();
			}
		});
	}

    @Override
    public void update(Observable o, Object arg) {
        StoreAction action = (StoreAction) arg;
        if (action.getType().equals("@CLIENT_SET_CONNECTION_STATUS")){
            ClientSetConnectionStatusAction castedAction = (ClientSetConnectionStatusAction) action;
            boolean isConnectionProblemPanelShown = this.connectionProblemLabel.isVisible();
            if (!isConnectionProblemPanelShown && castedAction.payload){
                this.connectionProblemLabel.setVisible(true);
            }
            else if (isConnectionProblemPanelShown && !castedAction.payload){
                this.connectionProblemLabel.setVisible(false);
            }
        }
    }
}
