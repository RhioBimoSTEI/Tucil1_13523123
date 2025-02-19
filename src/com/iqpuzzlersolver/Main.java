package com.iqpuzzlersolver;

import com.iqpuzzlersolver.model.Board;
import com.iqpuzzlersolver.model.Piece;
import com.iqpuzzlersolver.solver.DefaultSolver;
import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        String inputFile = "test/default.txt";
        
        try (BufferedReader br = new BufferedReader(new FileReader(inputFile))) {
            // Line 1: N M P
            String line = br.readLine();
            if (line == null) {
                System.out.println("Input file is empty.");
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
                System.out.println("Only DEFAULT puzzle mode is supported in this example.");
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
                System.out.println("Program Exitted: Expected " + expectedPieceCount + " pieces, but found " + pieces.size());
                return;
            }
            
            // Solver:
            DefaultSolver solver = new DefaultSolver(board, pieces);
            if (solver.solve()) {
                board.print();
                System.out.println("\nPuzzle Solved");
            } else {
                System.out.println("No Solution Found");
            }
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private static char getFirstNonWhitespaceChar(String line) {
        for (int i = 0; i < line.length(); i++) {
            if (!Character.isWhitespace(line.charAt(i))) {
                return line.charAt(i);
            }
        }
        return ' ';
    }
}
