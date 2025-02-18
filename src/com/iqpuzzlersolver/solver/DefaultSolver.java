package com.iqpuzzlersolver.solver;

import com.iqpuzzlersolver.model.Board;
import com.iqpuzzlersolver.model.Piece;
import java.util.List;

public class DefaultSolver {
    private Board board;
    private List<Piece> pieces;

    public DefaultSolver(Board board, List<Piece> pieces) {
        this.board = board;
        this.pieces = pieces;
    }
    
    public boolean solve() {
        return solveHelper(0);
    }
    
    // Recursive untuk brute force
    private boolean solveHelper(int index) {
        if (index == pieces.size()) { //Jika penempatan berhasil
            return true;
        }
        
        Piece current = pieces.get(index);
        // POV, you get rotated
        List<boolean[][]> rotations = current.getRotations(); // Ngamvil semua kemungkinan piece
        
        // Lakukan semua kemungkinan rotasi
        for (boolean[][] shape : rotations) {
            int shapeRows = shape.length;
            int shapeCols = shape[0].length;
            
            // Untuk rotasi skrg, coba semua kemungkinan penempatan
            for (int i = 0; i <= board.getRows() - shapeRows; i++) {
                for (int j = 0; j <= board.getCols() - shapeCols; j++) {
                    if (board.canPlace(shape, i, j)) {
                        board.placePiece(shape, i, j, current.getId());
                        
                        // Aww dangit, again!
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
