package client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JWindow;

/**
 * Represents the panel that is displayed when the game ends, with a different
 * image for each situation
 * 
 * @author Andrea Sessa
 * @author Giorgio Pea
 */
public class GUIFinalWindow extends JWindow {
	private static final long serialVersionUID = 1L;

	public GUIFinalWindow(Frame f, String image, final boolean exitOnClick) {
		super(f);

		JLabel background = new JLabel();
		background.setIcon(new ImageIcon(image));

		JPanel pane = new JPanel(new FlowLayout());
		pane.setPreferredSize(new Dimension(800, 500));
		pane.setBackground(Color.BLACK);

		pane.add(background);

		getContentPane().add(pane, BorderLayout.CENTER);
		pack();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

		setLocation(screenSize.width / 2 - 400, screenSize.height / 2 - 250);
		addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				setVisible(false);
				dispose();
				if (exitOnClick)
					System.exit(-1);
			}
		});
		setVisible(true);
	}
}
