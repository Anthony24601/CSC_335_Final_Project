package PiecePackage;
/**
File: ChessUI.java
Author: Chris Macholtz
Course: CSC 335
Purpose: Contains all the logic and relevant information regarding the King piece
		 
*/

import java.util.ArrayList;

import Game.Board;
import Game.GameModel;

public class King extends Piece {
    private static final String FILE_NAME = "king.png";
    final static char KIND = Piece.KING;

     /**
     * Constructs a new king of the specified color on the
     * passed Board
     * @param color either Piece.BLACK or Piece.WHITE
     * @param b a Board object
     */
    public King(int color, int rank, int file) {
        super(color, rank, file, "king");
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

        // right
        r = rank;
        f = file+1;
        if (board.isInBounds(r, f) && (board.isEmpty(r, f) || board.get(r, f).getColor() == opColor && board.get(r, f).getKind() != 'K')) {
            isCapture = board.get(r, f).getColor() == opColor;
            move = gameModel.constructMove(this, r, f, isCapture);
            if (!gameModel.wouldPutInCheck(getLoc(), move, board)) {
                move = gameModel.addCheck(getLoc(), move, board);
                moves.add(move);
            }
        }
        
        // up-right
        r = rank+1;
        f = file+1;
        if (board.isInBounds(r, f) && (board.isEmpty(r, f) || board.get(r, f).getColor() == opColor && board.get(r, f).getKind() != 'K')) {
            isCapture = board.get(r, f).getColor() == opColor;
            move = gameModel.constructMove(this, r, f, isCapture);
            if (!gameModel.wouldPutInCheck(getLoc(), move, board)) {
                move = gameModel.addCheck(getLoc(), move, board);
                moves.add(move);
            }
        }
        
        // up
        r = rank+1;
        f = file;
        if (board.isInBounds(r, f) && (board.isEmpty(r, f) || board.get(r, f).getColor() == opColor && board.get(r, f).getKind() != 'K')) {
            isCapture = board.get(r, f).getColor() == opColor;
            move = gameModel.constructMove(this, r, f, isCapture);
            if (!gameModel.wouldPutInCheck(getLoc(), move, board)) {
                move = gameModel.addCheck(getLoc(), move, board);
                moves.add(move);
            }
        }
        
        // up-left
        r = rank+1;
        f = file-1;
        if (board.isInBounds(r, f) && (board.isEmpty(r, f) || board.get(r, f).getColor() == opColor && board.get(r, f).getKind() != 'K')) {
            isCapture = board.get(r, f).getColor() == opColor;
            move = gameModel.constructMove(this, r, f, isCapture);
            if (!gameModel.wouldPutInCheck(getLoc(), move, board)) {
                move = gameModel.addCheck(getLoc(), move, board);
                moves.add(move);
            }
        }
        
        // left
        r = rank;
        f = file-1;
        if (board.isInBounds(r, f) && (board.isEmpty(r, f) || board.get(r, f).getColor() == opColor && board.get(r, f).getKind() != 'K')) {
            isCapture = board.get(r, f).getColor() == opColor;
            move = gameModel.constructMove(this, r, f, isCapture);
            if (!gameModel.wouldPutInCheck(getLoc(), move, board)) {
                move = gameModel.addCheck(getLoc(), move, board);
                moves.add(move);
            }
        }
        
        // down-left
        r = rank-1;
        f = file-1;
        if (board.isInBounds(r, f) && (board.isEmpty(r, f) || board.get(r, f).getColor() == opColor && board.get(r, f).getKind() != 'K')) {
            isCapture = board.get(r, f).getColor() == opColor;
            move = gameModel.constructMove(this, r, f, isCapture);
            if (!gameModel.wouldPutInCheck(getLoc(), move, board)) {
                move = gameModel.addCheck(getLoc(), move, board);
                moves.add(move);
            }
        }
        
        // down
        r = rank-1;
        f = file;
        if (board.isInBounds(r, f) && (board.isEmpty(r, f) || board.get(r, f).getColor() == opColor && board.get(r, f).getKind() != 'K')) {
            isCapture = board.get(r, f).getColor() == opColor;
            move = gameModel.constructMove(this, r, f, isCapture);
            if (!gameModel.wouldPutInCheck(getLoc(), move, board)) {
                move = gameModel.addCheck(getLoc(), move, board);
                moves.add(move);
            }
        }
        
        // down-right
        r = rank-1;
        f = file+1;
        if (board.isInBounds(r, f) && (board.isEmpty(r, f) || board.get(r, f).getColor() == opColor && board.get(r, f).getKind() != 'K')) {
            isCapture = board.get(r, f).getColor() == opColor;
            move = gameModel.constructMove(this, r, f, isCapture);
            if (!gameModel.wouldPutInCheck(getLoc(), move, board)) {
                move = gameModel.addCheck(getLoc(), move, board);
                moves.add(move);
            }
        }
        
        if (gameModel.canCastleKingside(board)) {
            moves.add("0-0");
        }
        if (gameModel.canCastleQueenside(board)) {
            moves.add("0-0-0");
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
        
        // right
        r = rank;
        f = file+1;
        if (board.isInBounds(r, f) && board.get(r, f).getKind() == 'K' && board.get(r, f).getColor() == opColor) {
            return true;
        }

        // up-right
        r = rank+1;
        f = file+1;
        if (board.isInBounds(r, f) && board.get(r, f).getKind() == 'K' && board.get(r, f).getColor() == opColor) {
            return true;
        }

        // up
        r = rank+1;
        f = file;
        if (board.isInBounds(r, f) && board.get(r, f).getKind() == 'K' && board.get(r, f).getColor() == opColor) {
            return true;
        }

        // up-left
        r = rank+1;
        f = file-1;
        if (board.isInBounds(r, f) && board.get(r, f).getKind() == 'K' && board.get(r, f).getColor() == opColor) {
            return true;
        }

        // left
        r = rank;
        f = file-1;
        if (board.isInBounds(r, f) && board.get(r, f).getKind() == 'K' && board.get(r, f).getColor() == opColor) {
            return true;
        }

        // down-left
        r = rank-1;
        f = file-1;
        if (board.isInBounds(r, f) && board.get(r, f).getKind() == 'K' && board.get(r, f).getColor() == opColor) {
            return true;
        }

        // down
        r = rank-1;
        f = file;
        if (board.isInBounds(r, f) && board.get(r, f).getKind() == 'K' && board.get(r, f).getColor() == opColor) {
            return true;
        }

        // down-right
        r = rank-1;
        f = file+1;
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
     * @return  Char of the piece ('K')
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
    public King copy() {
        return new King(color, rank, file);
    }
}
