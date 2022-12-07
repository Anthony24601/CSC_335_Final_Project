package PiecePackage;
/**
File: ChessUI.java
Author: Chris Macholtz
Course: CSC 335
Purpose: Contains all the logic and relevant information regarding the Queen piece
		 
*/

import java.util.ArrayList;

import Game.Board;
import Game.GameModel;

public class Queen extends Piece {
    private static final String FILE_NAME = "queen.png";
    final static char KIND = Piece.QUEEN;
    
     /**
     * Constructs a new queen of the specified color on the
     * passed Board
     * @param color either Piece.BLACK or Piece.WHITE
     * @param b a Board object
     */
    public Queen(int color, int rank, int file) {
        super(color, rank, file, "queen");
    }

    /**
     * All the valid moves the piece can make
     * @return  A String array of all the valid moves the piece can make
     */
    public String[] getValidMoves(Board board, GameModel gameModel) {
        ArrayList<String> moves = new ArrayList<>();
        String move;
        int opColor = color == Piece.WHITE ? Piece.BLACK : Piece.WHITE;
        int r, f;

        // right
        r = rank;
        f = file+1;
        while (board.isInBounds(r, f) && board.isEmpty(r, f)) {
            move = gameModel.constructMove(this, r, f, false);
            if (!gameModel.wouldPutInCheck(getLoc(), move, board)) {
                move = gameModel.addCheck(getLoc(), move, board);
                moves.add(move);
            }
            f++;
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
            f++;
            r++;
        }
        if (board.isInBounds(r, f) && board.get(r, f).getColor() == opColor && board.get(r, f).getKind() != 'K') {
            move = gameModel.constructMove(this, r, f, true);
            if (!gameModel.wouldPutInCheck(getLoc(), move, board)) {
                move = gameModel.addCheck(getLoc(), move, board);
                moves.add(move);
            }
        }

        // up
        r = rank+1;
        f = file;
        while (board.isInBounds(r, f) && board.isEmpty(r, f)) {
            move = gameModel.constructMove(this, r, f, false);
            if (!gameModel.wouldPutInCheck(getLoc(), move, board)) {
                move = gameModel.addCheck(getLoc(), move, board);
                moves.add(move);
            }
            r++;
        }
        if (board.isInBounds(r, f) && board.get(r, f).getColor() == opColor && board.get(r, f).getKind() != 'K') {
            move = gameModel.constructMove(this, r, f, true);
            if (!gameModel.wouldPutInCheck(getLoc(), move, board)) {
                move = gameModel.addCheck(getLoc(), move, board);
                moves.add(move);
            }
        }

        // up-left
        r = rank+1;
        f = file-1;
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

        // left
        r = rank;
        f = file-1;
        while (board.isInBounds(r, f) && board.isEmpty(r, f)) {
            move = gameModel.constructMove(this, r, f, false);
            if (!gameModel.wouldPutInCheck(getLoc(), move, board)) {
                move = gameModel.addCheck(getLoc(), move, board);
                moves.add(move);
            }
            f--;
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

        // down
        r = rank-1;
        f = file;
        while (board.isInBounds(r, f) && board.isEmpty(r, f)) {
            move = gameModel.constructMove(this, r, f, false);
            if (!gameModel.wouldPutInCheck(getLoc(), move, board)) {
                move = gameModel.addCheck(getLoc(), move, board);
                moves.add(move);
            }
            r--;
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

        // right
        r = rank;
        f = file+1;
        while (board.isInBounds(r, f)) {
            if (board.get(r, f).getKind() == 'K' && board.get(r, f).getColor() == opColor) {
                return true;
            }
            else if (!board.isEmpty(r, f)) {
                break;
            }
            f++;
        }

        // up-right
        r = rank+1;
        f = file+1;
        while (board.isInBounds(r, f)) {
            if (board.get(r, f).getKind() == 'K' && board.get(r, f).getColor() == opColor) {
                return true;
            }
            else if (!board.isEmpty(r, f)) {
                break;
            }
            r++;
            f++;
        }

        // up
        r = rank+1;
        f = file;
        while (board.isInBounds(r, f)) {
            if (board.get(r, f).getKind() == 'K' && board.get(r, f).getColor() == opColor) {
                return true;
            }
            else if (!board.isEmpty(r, f)) {
                break;
            }
            r++;
        }

        // up-left
        r = rank+1;
        f = file-1;
        while (board.isInBounds(r, f)) {
            if (board.get(r, f).getKind() == 'K' && board.get(r, f).getColor() == opColor) {
                return true;
            }
            else if (!board.isEmpty(r, f)) {
                break;
            }
            r++;
            f--;
        }

        // left
        r = rank;
        f = file-1;
        while (board.isInBounds(r, f)) {
            if (board.get(r, f).getKind() == 'K' && board.get(r, f).getColor() == opColor) {
                return true;
            }
            else if (!board.isEmpty(r, f)) {
                break;
            }
            f--;
        }

        // down-left
        r = rank-1;
        f = file-1;
        while (board.isInBounds(r, f)) {
            if (board.get(r, f).getKind() == 'K' && board.get(r, f).getColor() == opColor) {
                return true;
            }
            else if (!board.isEmpty(r, f)) {
                break;
            }
            r--;
            f--;
        }

        // down
        r = rank-1;
        f = file;
        while (board.isInBounds(r, f)) {
            if (board.get(r, f).getKind() == 'K' && board.get(r, f).getColor() == opColor) {
                return true;
            }
            else if (!board.isEmpty(r, f)) {
                break;
            }
            r--;
        }

        // down-right
        r = rank-1;
        f = file+1;
        while (board.isInBounds(r, f)) {
            if (board.get(r, f).getKind() == 'K' && board.get(r, f).getColor() == opColor) {
                return true;
            }
            else if (!board.isEmpty(r, f)) {
                break;
            }
            r--;
            f++;
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
     * @return  Char of the piece ('B')
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
    public Queen copy() {
        return new Queen(color, rank, file);
    }
}
