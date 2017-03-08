package client;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.rmi.NotBoundException;
import java.util.List;
import java.util.TimerTask;
import java.util.logging.Level;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import common.GamePublicData;

/**
 * Represents a task that periodically query the server about the list of
 * available games and updates the gui
 * 
 * @author Andrea Sessa
 * @author Giorgio Pea
 */
public class GamePollingThread extends TimerTask {
	private DefaultTableModel tableModel;
	private GuiInteractionManager gui;

	public GamePollingThread(GuiInteractionManager gui, JTable table) {
		this.gui = gui;
		
		tableModel = new DefaultTableModel() {
			@Override
			public boolean isCellEditable(int row, int column) {
		      return false;//This causes all cells to be not editable
		    }
		};
		table.setModel(tableModel);
		tableModel.addColumn("Game ID");
		tableModel.addColumn("Game Status");
		tableModel.addColumn("#Players");
	}

	/**
	 * Updates the JTable with the list of games
	 */
	@Override
	public void run() {
		try {
			Thread.sleep(500);
		} catch (InterruptedException e1) {
			ClientLogger.getLogger().log(Level.SEVERE, e1.getMessage(), e1);
		}
		for (int i = 0; i < tableModel.getRowCount(); i++) {
			tableModel.removeRow(i);
		}
		Object[][] data;
		try {
			data = this.retrieveGamesData();
			for (int i = 0; i < data.length; i++) {
				tableModel.addRow(data[i]);
			}
		} catch (IllegalAccessException | InvocationTargetException
				| NoSuchMethodException | ClassNotFoundException | IOException
				| NotBoundException e) {
			ClientLogger.getLogger().log(Level.SEVERE, e.getMessage(), e);
		}
	}

	/**
	 * Retrieves the data relative to games on the server and puts those games
	 * in a suitable data structure(a matrix) Col 0 -> GAME ID Col 1 -> GAME
	 * STATUS Col 2 -> PLAYERS NUMBER
	 * 
	 * @return the matrix containing the games informations
	 */
	public Object[][] retrieveGamesData() throws IllegalAccessException,
			InvocationTargetException, NoSuchMethodException,
			ClassNotFoundException, IOException, NotBoundException {
		List<GamePublicData> games = gui.getClient().getGames();
		Object[][] data = new Object[games.size()][3];
		int i = 0;
		for (GamePublicData game : games) {
			data[i][0] = game.getId();
			data[i][1] = game.getStatus();
			data[i][2] = game.getPlayersCount();
			i++;
		}
		return data;
	}
	
	/**
	 * One time updating of the list of games
	 */
	public void updateGames() {
		for (int i = 0; i < tableModel.getRowCount(); i++) {
			tableModel.removeRow(i);
		}
		Object[][] data;
		try {
			data = this.retrieveGamesData();
			for (int i = 0; i < data.length; i++) {
				tableModel.addRow(data[i]);
			}
		} catch (IllegalAccessException | InvocationTargetException
				| NoSuchMethodException | ClassNotFoundException | IOException
				| NotBoundException e) {
			ClientLogger.getLogger().log(Level.SEVERE, e.getMessage(), e);
		}
	}

}
