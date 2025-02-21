package com.iqpuzzlersolver.gui;

import com.iqpuzzlersolver.model.Board;
import com.iqpuzzlersolver.solver.DefaultSolver;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;
import javax.swing.*;

public class SolvePanel extends JPanel {
    private Board board;
    private long elapsedTime;

    public SolvePanel(Board board, long elapsedTime) {
        this.board = board;
        this.elapsedTime = elapsedTime;
        setLayout(new BorderLayout());

        // Top label
        JLabel titleLabel = new JLabel("Puzzle Solved!!", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        add(titleLabel, BorderLayout.NORTH);

        // Center Panel
        GUIPieces guiPieces = new GUIPieces(board);
        add(guiPieces, BorderLayout.CENTER);

        // Bottom Panel
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
        
        JButton backButton = new JButton("Back");
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Return to main panel.
                JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(SolvePanel.this);
                frame.dispose();
                new MainPanel().setVisible(true);
                DefaultSolver.Placing_steps = 0;
            }
        });
        bottomPanel.add(backButton);

        JLabel timeLabel = new JLabel("Time: " + elapsedTime + " ms");
        JLabel iterateLabel = new JLabel("Steps: " + DefaultSolver.Placing_steps + " steps");
        bottomPanel.add(timeLabel);
        bottomPanel.add(iterateLabel);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void saveAsText() {
        JFileChooser chooser = new JFileChooser();
        int option = chooser.showSaveDialog(SolvePanel.this);
        if (option == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            try (PrintWriter out = new PrintWriter(file)) {
                char[][] grid = board.getGrid();
                for (int i = 0; i < board.getRows(); i++) {
                    for (int j = 0; j < board.getCols(); j++) {
                        out.print(grid[i][j]);
                    }
                    out.println();
                }
                out.println("\nElapsed time: " + elapsedTime + " ms");
                out.println("Steps: " + DefaultSolver.Placing_steps);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(SolvePanel.this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void saveAsImage(JPanel panel) {
        int extraHeight = 50;
        int width = panel.getWidth();
        int height = panel.getHeight() + extraHeight;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = image.createGraphics();
        
        // Background color fill
        g2.setColor(Color.WHITE);
        g2.fillRect(0, 0, width, height);
        
        panel.paint(g2);
        
        // Set up text drawing properties
        g2.setColor(Color.BLACK);
        g2.setFont(new Font("Arial", Font.BOLD, 14));
        
        String elapsedTimeStr = "Elapsed time: " + elapsedTime + " ms";
        String stepsStr = "Steps: " + DefaultSolver.Placing_steps;
        
        int textX = 10;  // left margin
        int textY = panel.getHeight() + 20;
        g2.drawString(elapsedTimeStr, textX, textY);
        g2.drawString(stepsStr, textX, textY + 20);
        
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
