/**
 * File: Pawm.java
 * Author: Grace Driskill
 * Course: CSC 335
 * Purpose: Represents a single piece on a chess board.
 * Constructor
 * Pawn(int color, int rank, int file)
 */
package PiecePackage;
import java.util.ArrayList;

import Game.Board;
import Game.GameModel;

public class Pawn extends Piece {
    private static final String FILE_NAME = "pawn.png";
    private static final char KIND = Piece.PAWN;

    /**
     * Constructs a new pawn of the specified color on the
     * passed Board
     * @param color either Piece.BLACK or Piece.WHITE
     */
    public Pawn(int color, int rank, int file) {
        super(color, rank, file, "pawn");
    }

    @Override
    public String[] getValidMoves(Board board, GameModel gameModel) {
    	ArrayList<String> moves = new ArrayList<>();
        String move;

        if (this.color == Piece.WHITE) {
            // One space
            if (board.isInBounds(rank+1, file) && board.isEmpty(rank+1, file)) {
                move = gameModel.constructMove(this, rank+1, file, false);
                if (!gameModel.wouldPutInCheck(getLoc(), move, board)) {
                    move = gameModel.addCheck(getLoc(), move, board);
                    moves.add(move);
                }
            }

            // Two space
            if (rank == 2 && board.isInBounds(rank+2, file) && board.isEmpty(rank+1, file) && board.isEmpty(rank+2, file)) {
                move = gameModel.constructMove(this, rank+2, file, false);
                if (!gameModel.wouldPutInCheck(getLoc(), move, board)) {
                    move = gameModel.addCheck(getLoc(), move, board);
                    moves.add(move);
                }
            }

            // Capture
            if (board.isInBounds(rank+1, file-1) && (board.get(rank+1, file-1).getColor() == Piece.BLACK || board.get(rank+1, file-1).isPassant()) && board.get(rank+1, file-1).getKind() != 'K') {
                move = gameModel.constructMove(this, rank+1, file-1, true);
                if (!gameModel.wouldPutInCheck(getLoc(), move, board)) {
                    move = gameModel.addCheck(getLoc(), move, board);
                    moves.add(move);
                }
            }
            if (board.isInBounds(rank+1, file+1) && (board.get(rank+1, file+1).getColor() == Piece.BLACK|| board.get(rank+1, file+1).isPassant()) && board.get(rank+1, file+1).getKind() != 'K') {
                move = gameModel.constructMove(this, rank+1, file+1, true);
                if (!gameModel.wouldPutInCheck(getLoc(), move, board)) {
                    move = gameModel.addCheck(getLoc(), move, board);
                    moves.add(move);
                }
            } 
        } else {
            // One space
            if (board.isInBounds(rank-1, file) && board.isEmpty(rank-1, file)) {
                move = gameModel.constructMove(this, rank-1, file, false);
                if (!gameModel.wouldPutInCheck(getLoc(), move, board)) {
                    move = gameModel.addCheck(getLoc(), move, board);
                    moves.add(move);
                }
            }

            // Two space
            if (rank == 7 && board.isInBounds(rank-2, file) && board.isEmpty(rank-1, file) && board.isEmpty(rank-2, file)) {
                move = gameModel.constructMove(this, rank-2, file, false);
                if (!gameModel.wouldPutInCheck(getLoc(), move, board)) {
                    move = gameModel.addCheck(getLoc(), move, board);
                    moves.add(move);
                }
            }

            // Capture
            if (board.isInBounds(rank-1, file-1) && (board.get(rank-1, file-1).getColor() == Piece.WHITE || board.get(rank-1, file-1).isPassant()) && board.get(rank-1, file-1).getKind() != 'K') {
                move = gameModel.constructMove(this, rank-1, file-1, true);
                if (!gameModel.wouldPutInCheck(getLoc(), move, board)) {
                    move = gameModel.addCheck(getLoc(), move, board);
                    moves.add(move);
                }
            }
            if (board.isInBounds(rank-1, file+1) && (board.get(rank-1, file+1).getColor() == Piece.WHITE || board.get(rank-1, file+1).isPassant()) && board.get(rank-1, file+1).getKind() != 'K') {
                move = gameModel.constructMove(this, rank-1, file+1, true);
                if (!gameModel.wouldPutInCheck(getLoc(), move, board)) {
                    move = gameModel.addCheck(getLoc(), move, board);
                    moves.add(move);
                }
            }
        }

        String[] ret = new String[moves.size()];
        ret = moves.toArray(ret);
        return ret;
    }

    @Override
    public boolean canCheck(Board board) {
        if (this.color == Piece.WHITE) {
            if (board.isInBounds(rank+1, file-1) && board.get(rank+1, file-1).getColor() == Piece.BLACK && board.get(rank+1, file-1).getKind() == 'K') {
                return true;
            }
            if (board.isInBounds(rank+1, file+1) && board.get(rank+1, file+1).getColor() == Piece.BLACK && board.get(rank+1, file+1).getKind() == 'K') {
                return true;
            }
        } else {
            if (board.isInBounds(rank-1, file-1) && board.get(rank-1, file-1).getColor() == Piece.WHITE && board.get(rank-1, file-1).getKind() == 'K') {
                return true;
            }
            if (board.isInBounds(rank-1, file+1) && board.get(rank-1, file+1).getColor() == Piece.WHITE && board.get(rank-1, file+1).getKind() == 'K') {
                return true;
            } 
        }
        return false;
    }

    @Override
    public int getColor() {
        return color;
    }
    
    @Override
    public char getKind() {
        return KIND;
    }

    @Override
    public Pawn copy() {
        return new Pawn(color, rank, file);
    }

    @Override
    public String toString() {
        if (color == Piece.WHITE) {
            return "wp";
        } else {
            return "bp";
        }
    }

    @Override
    public boolean isBlank() {
        return false;
    }
}
