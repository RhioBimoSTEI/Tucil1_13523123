package com.iqpuzzlersolver.gui;

import java.awt.*;
import javax.swing.*;

public class LoadingPanel extends JPanel {
    public LoadingPanel() {
        setLayout(new BorderLayout());
        JLabel loadingLabel = new JLabel("Processing, please wait...", SwingConstants.CENTER);
        loadingLabel.setFont(new Font("Arial", Font.BOLD, 24));
        add(loadingLabel, BorderLayout.CENTER);

        JProgressBar progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        add(progressBar, BorderLayout.SOUTH);
    }
}
