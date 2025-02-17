package com.iqpuzzlersolver.model;

public class Board {
    private int rows;
    private int cols;
    private char[][] grid;
    
    public Board(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        grid = new char[rows][cols];
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
    
    // keknya bakal dihapus saat pake GUI (idk man)
    public void print() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                System.out.print(grid[i][j] + " ");
            }
            System.out.println();
        }
    }
}

// My IQ dropped by 10 points every-time I used javva, idk man....
