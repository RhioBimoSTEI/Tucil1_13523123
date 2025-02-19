package com.iqpuzzlersolver;

import com.iqpuzzlersolver.model.Board;
import com.iqpuzzlersolver.model.Piece;
import com.iqpuzzlersolver.solver.DefaultSolver;
import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner myObj = new Scanner(System.in);
        System.out.println("Enter username");
        String fileName = myObj.nextLine(); // temp: Read user input
        String inputFile = "test/"+fileName+".txt";
        
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
            long startTime = System.nanoTime();
            
            DefaultSolver solver = new DefaultSolver(board, pieces);
            if (solver.solve()) {
                if (!board.isComplete()) {
                    System.out.println("Error: Board has empty spaces");
                    return;
                }
                board.print();
                System.out.println("\nPuzzle Solved");
                System.out.println("Placing Steps:" + DefaultSolver.Placing_steps); //Print Placing Steps that Solver use
                System.out.println("Backtrack Times: " + DefaultSolver.Backtrack_steps); //Print How many times Solver backtrack
            } else {
                System.out.println("No Solution Found");
            }
            
            String estimatedTime = ((System.nanoTime() - startTime)/1000000) + " ms";

            System.out.println("Waktu pencarian: " + estimatedTime); //Print estimated time taken for brute force

            DefaultSolver.Placing_steps = 0;
            DefaultSolver.Backtrack_steps = 0;

        } catch (IOException | IllegalArgumentException e) {
            System.out.println(e.getMessage());
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
