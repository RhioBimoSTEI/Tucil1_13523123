package com.iqpuzzlersolver.gui;

import com.iqpuzzlersolver.model.Board;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;

public class SolvePanel extends JPanel {
    private Board board;
    private long elapsedTime;

    public SolvePanel(Board board, long elapsedTime) {
        this.board = board;
        this.elapsedTime = elapsedTime;
        setLayout(new BorderLayout());

        // Top label
        JLabel titleLabel = new JLabel("Puzzle Solved", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        add(titleLabel, BorderLayout.NORTH);

        // Center: custom drawing panel for board pieces
        GUIPieces guiPieces = new GUIPieces(board);
        add(guiPieces, BorderLayout.CENTER);

        // Bottom: save button and elapsed time info
        JPanel bottomPanel = new JPanel(new FlowLayout());
        JButton saveButton = new JButton("Save Board");
        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String[] options = {"Text File", "Image"};
                int choice = JOptionPane.showOptionDialog(SolvePanel.this, "Save board as:", "Save Board",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
                if (choice == 0) {
                    saveAsText();
                } else if (choice == 1) {
                    saveAsImage(guiPieces);
                }
            }
        });
        bottomPanel.add(saveButton);

        JLabel timeLabel = new JLabel("Time: " + elapsedTime + " ms");
        bottomPanel.add(timeLabel);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void saveAsText() {
        JFileChooser chooser = new JFileChooser();
        int option = chooser.showSaveDialog(SolvePanel.this);
        if (option == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            try (PrintWriter out = new PrintWriter(file)) {
                // Write board as text by iterating through the grid
                char[][] grid = board.getGrid();
                for (int i = 0; i < board.getRows(); i++) {
                    for (int j = 0; j < board.getCols(); j++) {
                        out.print(grid[i][j]);
                    }
                    out.println();
                }
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(SolvePanel.this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void saveAsImage(JPanel panel) {
        BufferedImage image = new BufferedImage(panel.getWidth(), panel.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = image.createGraphics();
        panel.paint(g2);
        g2.dispose();

        JFileChooser chooser = new JFileChooser();
        int option = chooser.showSaveDialog(SolvePanel.this);
        if (option == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            try {
                ImageIO.write(image, "png", file);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(SolvePanel.this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
