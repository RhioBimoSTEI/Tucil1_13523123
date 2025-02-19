package com.iqpuzzlersolver.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Piece {
    private char id;
    private boolean[][] shape;
    
    public Piece(char id, boolean[][] shape) {
        this.id = id;
        this.shape = shape;
    }
    
    public char getId() {return id;}
    
    public boolean[][] getShape() {return shape;}
    
    public static Piece fromString(String s) {
        String[] lines = s.split("\n");
        int rows = lines.length;
        int cols = 0;
        // Cek panjang column max si piece
        for (String line : lines) {
            if (line.length() > cols) {
                cols = line.length();
            }
        }
        boolean[][] shape = new boolean[rows][cols];
        char id = ' '; // ketika di (x,Y) space kosong
        for (int i = 0; i < rows; i++) {
            String line = lines[i];
            for (int j = 0; j < cols; j++) {
                if (j < line.length() && line.charAt(j) != ' ') { // Kalau space tidak kosong
                    // If id is not set, assign it and validate it
                    if (id == ' ') {
                        id = line.charAt(j);
                        if (!Character.isUpperCase(id) || id < 'A' || id > 'Z') {
                            throw new IllegalArgumentException("Error: Piece ID is not valid");
                        }
                    } else {
                        if (line.charAt(j) != id) {
                            throw new IllegalArgumentException("Error: Piece ID is not valid");
                        }
                    }
                    shape[i][j] = true;
                } else {
                    shape[i][j] = false;
                }
            }
        }
        return new Piece(id, shape);
    }
    
    public List<boolean[][]> getRotations() {
        List<boolean[][]> transformations = new ArrayList<>();
        Set<String> seen = new HashSet<>();

        // Possible Rotations
        List<boolean[][]> rotations = new ArrayList<>();
        rotations.add(shape); // 0/360 degree(s).
        boolean[][] rot90 = rotate(shape);
        rotations.add(rot90); 
        boolean[][] rot180 = rotate(rot90);
        rotations.add(rot180);
        boolean[][] rot270 = rotate(rot180);
        rotations.add(rot270);
        
        // for every rotation, mirror it
        for (boolean[][] mat : rotations) {
            String keyOriginal = matrixToString(mat);
            if (!seen.contains(keyOriginal)) {
                transformations.add(mat);
                seen.add(keyOriginal);
            }
            
            boolean[][] mirrored = mirror(mat);
            String keyMirrored = matrixToString(mirrored);
            if (!seen.contains(keyMirrored)) {
                transformations.add(mirrored); // Tambah piece tercermin
                seen.add(keyMirrored);
            }
        }
    
        return transformations;
    }

    // rotasi 90 derajat searah jarum jam
    private boolean[][] rotate(boolean[][] matrix) {
        int r = matrix.length;
        int c = matrix[0].length;
        boolean[][] rotated = new boolean[c][r];
        for (int i = 0; i < r; i++) {
            for (int j = 0; j < c; j++) {
                rotated[j][r - 1 - i] = matrix[i][j];
            }
        }
        return rotated;
    }

    // cermin matrix
    private boolean[][] mirror(boolean[][] matrix) {
        int rows = matrix.length;
        int cols = matrix[0].length;
        boolean[][] mirrored = new boolean[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                mirrored[i][j] = matrix[i][cols - 1 - j];
            }
        }
        return mirrored;
    }
    
    // helper untuk ubha matrix jadi string
    private String matrixToString(boolean[][] matrix) {
        StringBuilder sb = new StringBuilder();
        for (boolean[] row : matrix) {
            for (boolean cell : row) {
                sb.append(cell ? "1" : "0");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
