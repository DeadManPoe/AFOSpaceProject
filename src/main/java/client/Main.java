package client;

import javax.swing.*;

/**
 * Created by giorgiopea on 09/04/17.
 */
public class Main {

    public static void main(String[] args){
        Runnable r = new Runnable() {
            public void run() {
                GuiManager.getInstance().initGuiComponents();
            }
        };
        // Swing GUIs should be created and updated on the EDT
        SwingUtilities.invokeLater(r);
    }
}
