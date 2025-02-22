package com.iqpuzzlersolver.gui;

import java.awt.*;
import javax.swing.*;

public class LoadingPanel extends JPanel {
    private MusicPlayer musicPlayer;
    private JButton stopButton;
    private Runnable stopCallback;

    public LoadingPanel(Runnable stopCallback) {
        this.stopCallback = stopCallback;
        setLayout(new BorderLayout());
        JLabel loadingLabel = new JLabel("Pwocessing, pwease wait... UwU", SwingConstants.CENTER);
        loadingLabel.setFont(new Font("Arial", Font.BOLD, 24));
        add(loadingLabel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout());
        JProgressBar progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        bottomPanel.add(progressBar);

        // Add stop button
        stopButton = new JButton("Stop");
        stopButton.addActionListener(e -> {
            if (stopCallback != null) {
                stopCallback.run();
            }
            stopMusic();
        });
        bottomPanel.add(stopButton);

        add(bottomPanel, BorderLayout.SOUTH);
        
        // Start the brainrot
        musicPlayer = new MusicPlayer();
        musicPlayer.playMusic("src/com/iqpuzzlersolver/gui/music.wav"); // name of music file
    }
    
    public void stopMusic() {
        if (musicPlayer != null) {
            musicPlayer.stopMusic();
        }
    }
}
