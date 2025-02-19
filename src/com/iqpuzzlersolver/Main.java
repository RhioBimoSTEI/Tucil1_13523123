package com.iqpuzzlersolver;

import com.iqpuzzlersolver.gui.MainPanel;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        // Launch the GUI on the Event Dispatch Thread.
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                MainPanel mainPanel = new MainPanel();
                mainPanel.setVisible(true);
            }
        });
    }
}
