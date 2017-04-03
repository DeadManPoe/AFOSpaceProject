package client;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.rmi.NotBoundException;
import java.util.List;
import java.util.TimerTask;
import java.util.logging.Level;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import client_gui.GuiManager;
import client_store.ClientStore;
import client_store.InteractionManager;
import client_store_actions.ClientSetAvGamesAction;
import common.GamePublicData;

/**
 * Represents a task that periodically query the server about the list of
 * available games and updates the gui
 * 
 * @author Andrea Sessa
 * @author Giorgio Pea
 */
public class GamePollingThread extends TimerTask {


	/**
	 * Updates the JTable with the list of games
	 */
	@Override
	public void run() {
		try {
			InteractionManager.getInstance().getGames();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
