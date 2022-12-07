package PiecePackage;
/**
File: ChessUI.java
Author: Chris Macholtz
Course: CSC 335
Purpose: Contains all the logic and relevant information regarding the Knight piece
		 
*/

import java.util.ArrayList;

import Game.Board;
import Game.GameModel;

public class Knight extends Piece {
    private static final String FILE_NAME = "knight.png";
    final static char KIND = Piece.KNIGHT;

     /**
     * Constructs a new king of the specified color on the
     * passed Board
     * @param color either Piece.BLACK or Piece.WHITE
     * @param b a Board object
     */
    public Knight(int color, int rank, int file) {
        super(color, rank, file, "knight");
    }

    /**
     * All the valid moves the piece can make
     * @return  A String array of all the valid moves the piece can make
     */
    public String[] getValidMoves(Board board, GameModel gameModel) {
        ArrayList<String> moves = new ArrayList<>();
        int opColor = color == Piece.WHITE ? Piece.BLACK : Piece.WHITE;
        int r, f;
        String move;
        boolean isCapture;

        // right-up
        r = rank+1;
        f = file+2;
        if (board.isInBounds(r, f) && (board.isEmpty(r, f) || board.get(r, f).getColor() == opColor && board.get(r, f).getKind() != 'K')) {
            isCapture = board.get(r, f).getColor() == opColor;
            move = gameModel.constructMove(this, r, f, isCapture);
            if (!gameModel.wouldPutInCheck(getLoc(), move, board)) {
                move = gameModel.addCheck(getLoc(), move, board);
                moves.add(move);
            }
        }

        // up-right
        r = rank+2;
        f = file+1;
        if (board.isInBounds(r, f) && (board.isEmpty(r, f) || board.get(r, f).getColor() == opColor && board.get(r, f).getKind() != 'K')) {
            isCapture = board.get(r, f).getColor() == opColor;
            move = gameModel.constructMove(this, r, f, isCapture);
            if (!gameModel.wouldPutInCheck(getLoc(), move, board)) {
                move = gameModel.addCheck(getLoc(), move, board);
                moves.add(move);
            }
        }

        // up-left
        r = rank+2;
        f = file-1;
        if (board.isInBounds(r, f) && (board.isEmpty(r, f) || board.get(r, f).getColor() == opColor && board.get(r, f).getKind() != 'K')) {
            isCapture = board.get(r, f).getColor() == opColor;
            move = gameModel.constructMove(this, r, f, isCapture);
            if (!gameModel.wouldPutInCheck(getLoc(), move, board)) {
                move = gameModel.addCheck(getLoc(), move, board);
                moves.add(move);
            }
        }

        // left-up
        r = rank+1;
        f = file-2;
        if (board.isInBounds(r, f) && (board.isEmpty(r, f) || board.get(r, f).getColor() == opColor && board.get(r, f).getKind() != 'K')) {
            isCapture = board.get(r, f).getColor() == opColor;
            move = gameModel.constructMove(this, r, f, isCapture);
            if (!gameModel.wouldPutInCheck(getLoc(), move, board)) {
                move = gameModel.addCheck(getLoc(), move, board);
                moves.add(move);
            }
        }

        // left-down
        r = rank-1;
        f = file-2;
        if (board.isInBounds(r, f) && (board.isEmpty(r, f) || board.get(r, f).getColor() == opColor && board.get(r, f).getKind() != 'K')) {
            isCapture = board.get(r, f).getColor() == opColor;
            move = gameModel.constructMove(this, r, f, isCapture);
            if (!gameModel.wouldPutInCheck(getLoc(), move, board)) {
                move = gameModel.addCheck(getLoc(), move, board);
                moves.add(move);
            }
        }

        // down-left
        r = rank-2;
        f = file-1;
        if (board.isInBounds(r, f) && (board.isEmpty(r, f) || board.get(r, f).getColor() == opColor && board.get(r, f).getKind() != 'K')) {
            isCapture = board.get(r, f).getColor() == opColor;
            move = gameModel.constructMove(this, r, f, isCapture);
            if (!gameModel.wouldPutInCheck(getLoc(), move, board)) {
                move = gameModel.addCheck(getLoc(), move, board);
                moves.add(move);
            }
        }

        // down-right
        r = rank-2;
        f = file+1;
        if (board.isInBounds(r, f) && (board.isEmpty(r, f) || board.get(r, f).getColor() == opColor && board.get(r, f).getKind() != 'K')) {
            isCapture = board.get(r, f).getColor() == opColor;
            move = gameModel.constructMove(this, r, f, isCapture);
            if (!gameModel.wouldPutInCheck(getLoc(), move, board)) {
                move = gameModel.addCheck(getLoc(), move, board);
                moves.add(move);
            }
        }

        // right-down
        r = rank-1;
        f = file+2;
        if (board.isInBounds(r, f) && (board.isEmpty(r, f) || board.get(r, f).getColor() == opColor && board.get(r, f).getKind() != 'K')) {
            isCapture = board.get(r, f).getColor() == opColor;
            move = gameModel.constructMove(this, r, f, isCapture);
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
        int opColor = this.color == Piece.WHITE ? Piece.BLACK : Piece.WHITE;
        int r, f;

        // right-up
        r = rank+1;
        f = file+2;
        if (board.isInBounds(r, f) && board.get(r, f).getKind() == 'K' && board.get(r, f).getColor() == opColor) {
            return true;
        }

        // up-right
        r = rank+2;
        f = file+1;
        if (board.isInBounds(r, f) && board.get(r, f).getKind() == 'K' && board.get(r, f).getColor() == opColor) {
            return true;
        }

        // up-left
        r = rank+2;
        f = file-1;
        if (board.isInBounds(r, f) && board.get(r, f).getKind() == 'K' && board.get(r, f).getColor() == opColor) {
            return true;
        }

        // left-up
        r = rank+1;
        f = file-2;
        if (board.isInBounds(r, f) && board.get(r, f).getKind() == 'K' && board.get(r, f).getColor() == opColor) {
            return true;
        }

        // left-down
        r = rank-1;
        f = file-2;
        if (board.isInBounds(r, f) && board.get(r, f).getKind() == 'K' && board.get(r, f).getColor() == opColor) {
            return true;
        }

        // down-left
        r = rank-2;
        f = file-1;
        if (board.isInBounds(r, f) && board.get(r, f).getKind() == 'K' && board.get(r, f).getColor() == opColor) {
            return true;
        }

        // down-right
        r = rank-2;
        f = file+1;
        if (board.isInBounds(r, f) && board.get(r, f).getKind() == 'K' && board.get(r, f).getColor() == opColor) {
            return true;
        }

        // right-down
        r = rank-1;
        f = file+2;
        if (board.isInBounds(r, f) && board.get(r, f).getKind() == 'K' && board.get(r, f).getColor() == opColor) {
            return true;
        }

        return false;
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
     * Getter for kind (the type of piece)
     * @return  Char of the piece ('N')
     */
    @Override
    public char getKind() {
        return KIND;
    }

    /**
     * Copier for piece
     * @return  A copy of the piece
     */
    @Override
    public Knight copy() {
        return new Knight(color, rank, file);
    }
}
