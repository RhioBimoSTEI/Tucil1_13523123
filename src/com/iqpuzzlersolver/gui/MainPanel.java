package com.iqpuzzlersolver.gui;

import com.iqpuzzlersolver.model.Board;
import com.iqpuzzlersolver.model.Piece;
import com.iqpuzzlersolver.solver.DefaultSolver;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class MainPanel extends JFrame {
    public MainPanel() {
        setTitle("IQ Puzzler Solver");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600); // Pls ganti tergantung resolusi layar ehe
        setLocationRelativeTo(null);

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JToggleButton debugToggle = new JToggleButton("DEBUG");
        debugToggle.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                DefaultSolver.DEBUG = debugToggle.isSelected();
            }
        });
        topPanel.add(debugToggle);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        JLabel titleLabel = new JLabel("IQ Puzzler Solver");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 36));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton uploadButton = new JButton("Upload a File");
        uploadButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        uploadButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int result = fileChooser.showOpenDialog(MainPanel.this);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    runPuzzle(selectedFile);
                }
            }
        });

        centerPanel.add(Box.createVerticalGlue());
        centerPanel.add(titleLabel);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        centerPanel.add(uploadButton);
        centerPanel.add(Box.createVerticalGlue());

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(topPanel, BorderLayout.NORTH);
        getContentPane().add(centerPanel, BorderLayout.CENTER);
    }

    private void runPuzzle(File file) {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            // Line 1: N M P
            String line = br.readLine();
            if (line == null) {
                JOptionPane.showMessageDialog(this, "Input file is empty. OwO", "Error", JOptionPane.ERROR_MESSAGE);
                backToMain();
                return;
            }
            String[] tokens = line.trim().split("\\s+");
            int rows = Integer.parseInt(tokens[0]);
            int cols = Integer.parseInt(tokens[1]);
            int expectedPieceCount = Integer.parseInt(tokens[2]);

            // Buat board
            Board board = new Board(rows, cols);

            // Line 2: Puzzle type (S)
            String puzzleType = br.readLine().trim();
            if (!puzzleType.equalsIgnoreCase("DEFAULT")) {
                JOptionPane.showMessageDialog(this, "Only DEFAULT puzzle mode is supported.", "Error", JOptionPane.ERROR_MESSAGE);
                backToMain();
                return;
            }

            // Lines 3++: Puzzle pieces
            List<Piece> pieces = new ArrayList<>();
            char currentPieceId = '\0';
            StringBuilder pieceBlock = new StringBuilder();

            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue; // skip blank lines
                char firstChar = getFirstNonWhitespaceChar(line);
                if (currentPieceId == '\0') { // Mulai membaca piece pertama
                    currentPieceId = firstChar;
                    pieceBlock.append(line).append("\n");
                } else if (firstChar == currentPieceId) { // Kalau huruf pertama sama, lanjut piece
                    pieceBlock.append(line).append("\n");
                } else { // Huruf berubah
                    pieces.add(Piece.fromString(pieceBlock.toString()));
                    pieceBlock.setLength(0);
                    currentPieceId = firstChar;
                    pieceBlock.append(line).append("\n");
                }
            }
            // Tambahkan piece terakhir (kaalu ada)
            if (pieceBlock.length() > 0) {
                pieces.add(Piece.fromString(pieceBlock.toString()));
            }
            if (pieces.size() != expectedPieceCount) {
                JOptionPane.showMessageDialog(this, "Program Exitted: Expected " + expectedPieceCount + " pieces, but found " + pieces.size() + " OwO", "Error", JOptionPane.ERROR_MESSAGE);
                backToMain();
                return;
            }
            
            DefaultSolver solver = new DefaultSolver(board, pieces);
            long startTime = System.nanoTime();

            // If DEBUG mode is off, show a loading indicator.
            if (!DefaultSolver.DEBUG) {
                setContentPane(new LoadingPanel());
                revalidate();
            } else {
                // If debug is on, create a live, colorized debug view.
                GUIPieces debugPanel = new GUIPieces(board);
                solver.setDebugPanel(debugPanel);
                setContentPane(debugPanel);
                revalidate();
            }

            new Thread(() -> {
                boolean solved = solver.solve();
                long elapsedTime = (System.nanoTime() - startTime) / 1000000;
                SwingUtilities.invokeLater(() -> {
                    if (solved && board.isSolved(pieces)) {
                        // Switch to final view with solved configuration and save options.
                        setContentPane(new SolvePanel(board, elapsedTime));
                        revalidate();
                    } else {
                        JOptionPane.showMessageDialog(MainPanel.this, "Nyo Sowution Found or Puzzle wequiwements not met UwU.", "Error", JOptionPane.ERROR_MESSAGE);
                        backToMain();
                    }
                });
            }).start();

        } catch (IOException | IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            backToMain();
        }
    }

    private char getFirstNonWhitespaceChar(String line) {
        for (int i = 0; i < line.length(); i++) {
            if (!Character.isWhitespace(line.charAt(i))) {
                return line.charAt(i);
            }
        }
        return ' ';
    }
    
    // Open a new MainPanel
    private void backToMain() {
        // Dispose the current frame
        SwingUtilities.invokeLater(() -> {
            dispose();
            new MainPanel().setVisible(true); // Open a new MainPanel
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MainPanel().setVisible(true);
        });
    }
}
