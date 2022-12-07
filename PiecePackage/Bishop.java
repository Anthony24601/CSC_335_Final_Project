package PiecePackage;
/**
File: ChessUI.java
Author: Chris Macholtz
Course: CSC 335
Purpose: Contains all the logic and relevant information regarding the Bishop piece
		 
*/

import java.util.ArrayList;

import Game.Board;
import Game.GameModel;

public class Bishop extends Piece {
    private static final char KIND = Piece.BISHOP;

    /**
     * Constructor
     * @param color     The piece's color
     * @param rank      The piece's rank
     * @param file      The piece's file
     */
    public Bishop(int color, int rank, int file) {
        super(color, rank, file, "bishop");
    }

    /**
     * All the valid moves the piece can make
     * @return  A String array of all the valid moves the piece can make
     */
    public String[] getValidMoves(Board board, GameModel gameModel) {
        ArrayList<String> moves = new ArrayList<>();
        int opColor = color == Piece.WHITE ? Piece.BLACK : Piece.WHITE;
        String move;

        // up-left
        int r = rank+1;
        int f = file-1;
        while (board.isInBounds(r, f) && board.isEmpty(r, f)) {
            move = gameModel.constructMove(this, r, f, false);
            if (!gameModel.wouldPutInCheck(getLoc(), move, board)) {
                move = gameModel.addCheck(getLoc(), move, board);
                moves.add(move);
            }
            r++;
            f--;            
        }
        if (board.isInBounds(r, f) && board.get(r, f).getColor() == opColor && board.get(r, f).getKind() != 'K') {
            move = gameModel.constructMove(this, r, f, true);
            if (!gameModel.wouldPutInCheck(getLoc(), move, board)) {
                move = gameModel.addCheck(getLoc(), move, board);
                moves.add(move);
            }
        }

        // up-right
        r = rank+1;
        f = file+1;
        while (board.isInBounds(r, f) && board.isEmpty(r, f)) {
            move = gameModel.constructMove(this, r, f, false);
            if (!gameModel.wouldPutInCheck(getLoc(), move, board)) {
                move = gameModel.addCheck(getLoc(), move, board);
                moves.add(move);
            }
            r++;
            f++;
        }
        if (board.isInBounds(r, f) && board.get(r, f).getColor() == opColor && board.get(r, f).getKind() != 'K') {
            move = gameModel.constructMove(this, r, f, true);
            if (!gameModel.wouldPutInCheck(getLoc(), move, board)) {
                move = gameModel.addCheck(getLoc(), move, board);
                moves.add(move);
            }
        }

        // down-left
        r = rank-1;
        f = file-1;
        while (board.isInBounds(r, f) && board.isEmpty(r, f)) {
            move = gameModel.constructMove(this, r, f, false);
            if (!gameModel.wouldPutInCheck(getLoc(), move, board)) {
                move = gameModel.addCheck(getLoc(), move, board);
                moves.add(move);
            }
            r--;
            f--;
        }
        if (board.isInBounds(r, f) && board.get(r, f).getColor() == opColor && board.get(r, f).getKind() != 'K') {
            move = gameModel.constructMove(this, r, f, true);
            if (!gameModel.wouldPutInCheck(getLoc(), move, board)) {
                move = gameModel.addCheck(getLoc(), move, board);
                moves.add(move);
            }
        }

        // down-right
        r = rank-1;
        f = file+1;
        while (board.isInBounds(r, f) && board.isEmpty(r, f)) {
            move = gameModel.constructMove(this, r, f, false);
            if (!gameModel.wouldPutInCheck(getLoc(), move, board)) {
                move = gameModel.addCheck(getLoc(), move, board);
                moves.add(move);
            }
            r--;
            f++;
        }
        if (board.isInBounds(r, f) && board.get(r, f).getColor() == opColor && board.get(r, f).getKind() != 'K') {
            move = gameModel.constructMove(this, r, f, true);
            if (!gameModel.wouldPutInCheck(getLoc(), move, board)) {
                move = gameModel.addCheck(getLoc(), move, board);
                moves.add(move);
            }
        }

        String[] ret = new String[moves.size()];
        ret = moves.toArray(ret);
        return ret;
    }

    /**
     * Determines if the piece has the opposing king in check
     * @return  True if the opposing king is in check by this piece
     */
    public boolean canCheck(Board board) {
        int opColor = color == Piece.WHITE ? Piece.BLACK : Piece.WHITE;
        int r, f;

        // up-left
        r = rank+1;
        f = file-1;
        while (board.isInBounds(r, f)) {
            if (board.get(r, f).getKind() == 'K' && board.get(r, f).getColor() == opColor) {
                return true;
            } else if (!board.isEmpty(r, f)) {
                break;
            }
            r++;
            f--;
        }

        // up-right
        r = rank+1;
        f = file+1;
        while (board.isInBounds(r, f)) {
            if (board.get(r, f).getKind() == 'K' && board.get(r, f).getColor() == opColor) {
                return true;
            } else if (!board.isEmpty(r, f)) {
                break;
            }
            r++;
            f++;
        }

        // down-left
        r = rank-1;
        f = file-1;
        while (board.isInBounds(r, f)) {
            if (board.get(r, f).getKind() == 'K' && board.get(r, f).getColor() == opColor) {
                return true;
            } else if (!board.isEmpty(r, f)) {
                break;
            }
            r--;
            f--;
        }

        // down-right
        r = rank-1;
        f = file+1;
        while (board.isInBounds(r, f)) {
            if (board.get(r, f).getKind() == 'K' && board.get(r, f).getColor() == opColor) {
                return true;
            } else if (!board.isEmpty(r, f)) {
                break;
            }
            r--;
            f++;
        }
        
        return false;
    }

    /**
     * Getter for kind (the type of piece)
     * @return  Char of the piece ('B')
     */
    public char getKind() {
        return KIND;
    } 

    /**
     * Getter for color
     * @return  Piece's color
     */
    @Override
    public int getColor() {
        return color;
    }

    /**
     * Returns that this piece is not a blank space
     */
    @Override
    public boolean isBlank() {
        return false;
    }

    /**
     * Copier for piece
     * @return  A copy of the piece
     */
    @Override
    public Bishop copy() {
        return new Bishop(color, rank, file);
    }
}
