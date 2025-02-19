package com.iqpuzzlersolver.solver;

import com.iqpuzzlersolver.model.Board;
import com.iqpuzzlersolver.model.Piece;
import java.util.List;

public class DefaultSolver {
    private Board board;
    private List<Piece> pieces;
    
    public static boolean DEBUG = false; //Ganti jadi true kala mau lihat steps setiap program place/backtrack piece di board

    public DefaultSolver(Board board, List<Piece> pieces) {
        this.board = board;
        this.pieces = pieces;
    }
    
    public boolean solve() {
        return solveHelper(0);
    }
    
    public static long Placing_steps = 0;
    public static long Backtrack_steps = 0;

    // Recursive untuk brute force
    private boolean solveHelper(int index) {
        if (index == pieces.size()) { // Jika penempatan berhasil
            return true;
        }
        
        Piece current = pieces.get(index);
        // POV, you get rotated
        // Ambil semua kemungkinan rotasi (termasuk cermin) dari piece
        List<boolean[][]> rotations = current.getRotations();
        

        // Lakukan semua kemungkinan rotasi
        for (boolean[][] shape : rotations) {
            int shapeRows = shape.length;
            int shapeCols = shape[0].length;
            

            // Untuk rotasi sekarang, coba semua kemungkinan penempatan
            for (int i = 0; i <= board.getRows() - shapeRows; i++) {
                for (int j = 0; j <= board.getCols() - shapeCols; j++) {
                    if (board.canPlace(shape, i, j)) {
                        board.placePiece(shape, i, j, current.getId());
                        
                        // Jika DEBUG true, cetak status board dan lokasi penempatan
                        if (DEBUG) {
                            board.print();
                            System.out.println("Placed piece " + current.getId() + " at (" + i + ", " + j + ")");
                            System.out.println("----------------------");
                        }
                        Placing_steps++;
                        
                        // Aww dangit, again!
                        if (solveHelper(index + 1)) {
                            return true;
                        }
                        board.removePiece(shape, i, j);
                        
                        if (DEBUG) {
                            board.print();
                            System.out.println("Backtracked piece " + current.getId() + " from (" + i + ", " + j + ")");
                            System.out.println("----------------------");
                        }
                        Backtrack_steps++;
                    }
                    
                }
            }
        }
        
        return false;
    }
}
