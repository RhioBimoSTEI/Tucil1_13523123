package com.iqpuzzlersolver.gui;

import com.iqpuzzlersolver.model.Board;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;

public class GUIPieces extends JPanel {
    private Board board;
    private Map<Character, Color> colorMap = new HashMap<>();

    public GUIPieces(Board board) {
        this.board = board;
        Color[] colors = {
            Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN, Color.BLUE,
            new Color(75, 0, 130), new Color(148, 0, 211), Color.WHITE,
            Color.CYAN, new Color(128, 0, 128), Color.MAGENTA
        };
        int colorIndex = 0;
        char[][] grid = board.getGrid();
        for (int i = 0; i < board.getRows(); i++) {
            for (int j = 0; j < board.getCols(); j++) {
                char cell = grid[i][j];
                if (cell != '.' && cell != ' ' && !colorMap.containsKey(cell)) {
                    colorMap.put(cell, colors[colorIndex % colors.length]);
                    colorIndex++;
                }
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int rows = board.getRows();
        int cols = board.getCols();
        int cellSize = Math.min(getWidth() / cols, getHeight() / rows);
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(2));
        char[][] grid = board.getGrid();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                int x = j * cellSize;
                int y = i * cellSize;
                if (board.isCustom()) {
                    if (board.isPlayable(i, j)) {
                        char cell = grid[i][j];
                        if (cell == '.') {
                            // Empty playable cell:
                            g2.setColor(Color.WHITE);
                            g2.fillOval(x, y, cellSize, cellSize);
                        } else {
                            // Placed piece: use its color
                            Color pieceColor = colorMap.get(cell);
                            if (pieceColor == null) {
                                pieceColor = Color.GRAY;
                            }
                            g2.setColor(pieceColor);
                            g2.fillOval(x, y, cellSize, cellSize);
                            g2.setColor(Color.BLACK);
                            g2.drawOval(x, y, cellSize, cellSize);
                            g2.setColor(Color.BLACK);
                            g2.setFont(new Font("Arial", Font.BOLD, cellSize / 2));
                            FontMetrics fm = g2.getFontMetrics();
                            String idStr = String.valueOf(cell);
                            int textWidth = fm.stringWidth(idStr);
                            int textHeight = fm.getAscent();
                            int textX = x + (cellSize - textWidth) / 2;
                            int textY = y + (cellSize + textHeight) / 2;
                            g2.drawString(idStr, textX, textY);
                        }
                    } else {
                        // Non-playable cell
                        g2.setColor(Color.BLACK);
                        g2.fillOval(x, y, cellSize, cellSize);
                        g2.setColor(Color.BLACK);
                        g2.drawOval(x, y, cellSize, cellSize);
                    }
                } else {
                    // DEFAULT mode:
                    g2.setColor(Color.LIGHT_GRAY);
                    g2.drawOval(x, y, cellSize, cellSize);
                    char cell = grid[i][j];
                    if (cell != '.') {
                        Color pieceColor = colorMap.get(cell);
                        g2.setColor(pieceColor);
                        g2.fillOval(x, y, cellSize, cellSize);
                        g2.setColor(Color.BLACK);
                        g2.drawOval(x, y, cellSize, cellSize);
                        g2.setColor(Color.BLACK);
                        g2.setFont(new Font("Arial", Font.BOLD, cellSize / 2));
                        FontMetrics fm = g2.getFontMetrics();
                        String idStr = String.valueOf(cell);
                        int textWidth = fm.stringWidth(idStr);
                        int textHeight = fm.getAscent();
                        int textX = x + (cellSize - textWidth) / 2;
                        int textY = y + (cellSize + textHeight) / 2;
                        g2.drawString(idStr, textX, textY);
                    }
                }
            }
        }
    }
}
