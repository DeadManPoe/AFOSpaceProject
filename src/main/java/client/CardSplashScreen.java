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

import common.Card;


public class CardSplashScreen extends JWindow {
	private static final long serialVersionUID = 1L;

	public CardSplashScreen(Card card1, Card card2, Frame f) throws ClassNotFoundException
    {
        super(f);
        ResourceMapper mapper = new ResourceMapper();
        
        JLabel cardLbl1 = new JLabel();
        cardLbl1.setIcon(new ImageIcon(mapper.getCardImage(card1)));
        JLabel cardLbl2 = new JLabel(); 
        if(card2 != null) cardLbl2.setIcon(new ImageIcon(mapper.getCardImage(card2)));
        
        JPanel pane = new JPanel(new FlowLayout());
        pane.setPreferredSize(new Dimension(400,192));
        pane.setBackground(Color.BLACK);
        
        cardLbl1.setAlignmentX(CENTER_ALIGNMENT);
        cardLbl2.setAlignmentX(CENTER_ALIGNMENT);
        cardLbl1.setAlignmentY(CENTER_ALIGNMENT);
        cardLbl2.setAlignmentY(CENTER_ALIGNMENT);
        
        pane.add(cardLbl1);
        pane.add(cardLbl2);
        
        
        getContentPane().add(pane, BorderLayout.CENTER);
        pack();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        
        setLocation(screenSize.width/2 - 200, screenSize.height/2 - 170);
        addMouseListener(new MouseAdapter()
            {
                public void mousePressed(MouseEvent e)
                {
                    setVisible(false);
                    dispose();
                }
            });
        setVisible(true);
    }
}
