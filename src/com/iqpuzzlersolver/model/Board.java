package com.iqpuzzlersolver.model;

import java.util.HashMap;
import java.util.Map;

public class Board {
    private int rows;
    private int cols;
    private char[][] grid;
    
    public Board(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        grid = new char[rows][cols];
        // Initialize board dengan '.' saat kosong
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                grid[i][j] = '.'; // Ketika kosong
            }
        }
    }
    
    public int getRows() {
        return rows;
    }
    
    public int getCols() {
        return cols;
    }
    
    // Cek klo misal 'shape' bisa di masukin ke board
    public boolean canPlace(boolean[][] shape, int row, int col) {
        int shapeRows = shape.length;
        int shapeCols = shape[0].length;
        if (row < 0 || col < 0 || row + shapeRows > rows || col + shapeCols > cols) { // Cek boundary board
            return false;
        }
        
        // Cek bisa masuk atau tidak (shape ke board)
        for (int i = 0; i < shapeRows; i++) {
            for (int j = 0; j < shapeCols; j++) {
                if (shape[i][j] && grid[row + i][col + j] != '.') {
                    return false;
                }
            }
        }
        return true;
    }
    
    // literally cuman place piece di board
    public void placePiece(boolean[][] shape, int row, int col, char id) {
        int shapeRows = shape.length;
        int shapeCols = shape[0].length;
        for (int i = 0; i < shapeRows; i++) {
            for (int j = 0; j < shapeCols; j++) {
                if (shape[i][j]) {
                    grid[row + i][col + j] = id;
                }
            }
        }
    }
    
    // kebalikannya yang diatas :v
    public void removePiece(boolean[][] shape, int row, int col) {
        int shapeRows = shape.length;
        int shapeCols = shape[0].length;
        for (int i = 0; i < shapeRows; i++) {
            for (int j = 0; j < shapeCols; j++) {
                if (shape[i][j]) {
                    grid[row + i][col + j] = '.';
                }
            }
        }
    }
    
    // Keknya bakal dihapus saat pake GUI (idk man)
    public void print() {
        Map<Character, String> colorMap = new HashMap<>();
        // kolor
        String[] colors = { ""
            // "\u001B[31m", // Red
            // "\u001B[32m", // Green
            // "\u001B[33m", // Yellow
            // "\u001B[34m", // Blue
            // "\u001B[35m", // Magenta
            // "\u001B[36m", // Cyan
            // "\u001B[37m", // White
            // "\u001B[91m", // Bright Red
            // "\u001B[92m", // Bright Green
            // "\u001B[93m", // Bright Yellow
            // "\u001B[94m", // Bright Blue
            // "\u001B[95m", // Bright Magenta
            // "\u001B[96m"  // Bright Cyan
        };
        int colorIndex = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                char cell = grid[i][j];
                if (cell != '.') {
                    if (!colorMap.containsKey(cell)) {
                        colorMap.put(cell, colors[colorIndex % colors.length]);
                        colorIndex++;
                    }
                    // Print the cell in its designated color.
                    System.out.print(colorMap.get(cell) + cell + "\u001B[0m");
                } else {
                    System.out.print(cell);
                }
            }
            System.out.println();
        }
    }
}
