package com.iqpuzzlersolver.gui;

import com.iqpuzzlersolver.model.Board;
import com.iqpuzzlersolver.model.Piece;
import com.iqpuzzlersolver.solver.DefaultSolver;
import com.iqpuzzlersolver.solver.CustomSolver;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class MainPanel extends JFrame {
    private Object currentSolver = null; // can be DefaultSolver or CustomSolver
    
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
                JOptionPane.showMessageDialog(this, "Inpwut file is ewmpty. T^T", "Error", JOptionPane.ERROR_MESSAGE);
                backToMain();
                return;
            }
            String[] tokens = line.trim().split("\\s+");
            int rows = Integer.parseInt(tokens[0]);
            int cols = Integer.parseInt(tokens[1]);
            int expectedPieceCount = Integer.parseInt(tokens[2]);

            // Line 2: Puzzle type (S)
            String puzzleType = br.readLine().trim();
            Board board;
            if (puzzleType.equalsIgnoreCase("DEFAULT")) {
                board = new Board(rows, cols);
            } else if (puzzleType.equalsIgnoreCase("CUSTOM")) {
                String[] pattern = new String[rows];
                for (int i = 0; i < rows; i++) {
                    pattern[i] = br.readLine();
                }
                board = new Board(pattern);
            } else {
                JOptionPane.showMessageDialog(this, "Only DEFAULT and CUSTOM puzzle modes are supported.", "Error", JOptionPane.ERROR_MESSAGE);
                backToMain();
                return;
            }

            // Line 3++: P pieces
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
            
            // Solver:
            Runnable stopCallback = () -> {
                if (currentSolver instanceof DefaultSolver) {
                    ((DefaultSolver) currentSolver).cancel();
                } else if (currentSolver instanceof CustomSolver) {
                    ((CustomSolver) currentSolver).cancel();
                }
                backToMain();
            };


            // Default mode
            if (puzzleType.equalsIgnoreCase("DEFAULT")) {
                DefaultSolver solver = new DefaultSolver(board, pieces);
                currentSolver = solver;
                long startTime = System.nanoTime();
            
                // If DEBUG mode is off, show a loading indicator.
                if (!DefaultSolver.DEBUG) {
                    setContentPane(new LoadingPanel(stopCallback));
                    revalidate();
                } else {
                    // If debug is on, create a live, colorized debug view.
                    GUIPieces debugPanel = new GUIPieces(board);
                    solver.setDebugPanel(debugPanel);
                    // Optionally, add stop button to debug view
                    JPanel debugContainer = new JPanel(new BorderLayout());
                    debugContainer.add(debugPanel, BorderLayout.CENTER);
                    JButton stopButton = new JButton("Stop");
                    stopButton.addActionListener(e -> stopCallback.run());
                    debugContainer.add(stopButton, BorderLayout.SOUTH);
                    setContentPane(debugContainer);
                    revalidate();
                }
            
                new Thread(() -> {
                    boolean solved = solver.solve();
                    long elapsedTime = (System.nanoTime() - startTime) / 1000000;
                    SwingUtilities.invokeLater(() -> {
                        if (solved && board.isSolved(pieces)) {
                            setContentPane(new SolvePanel(board, elapsedTime));
                            revalidate();
                        } else {
                            JOptionPane.showMessageDialog(MainPanel.this, "Oh nyo... Nyo Sowution Found or Puzzle wequiwements not met UwU.", "Error", JOptionPane.ERROR_MESSAGE);
                            backToMain();
                        }
                    });
                }).start();           

            // Custom mode
            } else if (puzzleType.equalsIgnoreCase("CUSTOM")) {
                CustomSolver solver = new CustomSolver(board, pieces);
                currentSolver = solver;
                long startTime = System.nanoTime();
            
                setContentPane(new LoadingPanel(stopCallback));
                revalidate();
            
                new Thread(() -> {
                    boolean solved = solver.solve();
                    long elapsedTime = (System.nanoTime() - startTime) / 1000000;
                    SwingUtilities.invokeLater(() -> {
                        if (solved && board.isSolved(pieces)) {
                            setContentPane(new SolvePanel(board, elapsedTime));
                            revalidate();
                        } else {
                            JOptionPane.showMessageDialog(MainPanel.this, "Oh nyo... Nyo Sowution Found or Puzzle wequiwements not met UwU.", "Error", JOptionPane.ERROR_MESSAGE);
                            backToMain();
                        }
                    });
                }).start();
            }
            
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
            DefaultSolver.Placing_steps = 0;
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MainPanel().setVisible(true);
        });
    }
}
