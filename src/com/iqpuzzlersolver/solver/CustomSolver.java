package com.iqpuzzlersolver.solver;

import com.iqpuzzlersolver.model.Board;
import com.iqpuzzlersolver.model.Piece;
import java.util.List;

public class CustomSolver {
    private Board board;
    private List<Piece> pieces;
    
    public static long Placing_steps = 0;
    
    private volatile boolean cancelled = false;
    
    public void cancel() {
        cancelled = true;
    }
    
    public CustomSolver(Board board, List<Piece> pieces) {
        this.board = board;
        this.pieces = pieces;
    }
    
    public boolean solve() {
        return solveHelper(0);
    }
    
    // Recursive brute force solver for CUSTOM mode
    private boolean solveHelper(int index) {
        if (cancelled) return false;
        if (index == pieces.size()) {
            return true;
        }
        
        Piece current = pieces.get(index);
        // Ambil semua kemungkinan rotasi (termasuk cermin) dari piece
        List<boolean[][]> rotations = current.getRotations();
        
        for (boolean[][] shape : rotations) {
            if (cancelled) return false;
            int shapeRows = shape.length;
            int shapeCols = shape[0].length;
            
            for (int i = 0; i <= board.getRows() - shapeRows; i++) {
                for (int j = 0; j <= board.getCols() - shapeCols; j++) {
                    if (cancelled) return false;
                    // Make sure only place the piece if it's valid
                    if (board.canPlace(shape, i, j)) {
                        board.placePiece(shape, i, j, current.getId());
                        Placing_steps++; // Increment the step counter for each placement
                        
                        if (solveHelper(index + 1)) {
                            return true;
                        }
                        board.removePiece(shape, i, j);
                    }
                }
            }
        }
        return false;
    }
}
