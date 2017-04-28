package client;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 * Starts and display the GUI
 * @author andrea
 *
 */
public class MainGUI {
	public static void main(String[] args) {
		Runnable r = new Runnable() {
			
			public void run() {


				GuiManager guiManager = GuiManager.getInstance();
				guiManager.initGuiComponents();
				JFrame frame = guiManager.getFrame();

				// Ensures JVM closes after frame(s) closed and
				// all non-daemon threads are finished
				frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				frame.setLocationByPlatform(true);

				// ensures the frame is the minimum size it needs to be
				// in order display the components within it
				frame.pack();
				frame.setExtendedState(frame.getExtendedState()
						| JFrame.MAXIMIZED_BOTH);
				frame.setVisible(true);

			}
		};
		// Swing GUIs should be created and updated on the EDT
		SwingUtilities.invokeLater(r);

	}
}
