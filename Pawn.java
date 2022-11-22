import java.util.ArrayList;

public class Pawn extends Piece {
    private static final String FILE_NAME = "pawn.png";
    private static final char KIND = Piece.PAWN;

    /**
     * Constructs a new pawn of the specified color on the
     * passed Board
     * @param color either Piece.BLACK or Piece.WHITE
     */
    public Pawn(int color, int rank, int file) {
        super(color, rank, file);
    }

    public char getKind() {
        return KIND;
    }

    public String[] getValidMoves(Board board) {
        ArrayList<String> moves = new ArrayList<>();
        if (this.color == Piece.WHITE) {
            // One space
            if (board.isInBounds(rank+1, file) && board.isEmpty(rank+1, file)) {
                moves.add(MoveParser.constructMove(this, rank+1, file, false));
            }

            // Two space
            if (rank == 2 && board.isInBounds(rank+2, file) && board.isEmpty(rank+1, file) && board.isEmpty(rank+2, file)) {
                moves.add(MoveParser.constructMove(this, rank+2, file, false));
            }

            // Capture
            if (board.isInBounds(rank+1, file-1) && board.get(rank+1, file-1).getColor() == Piece.BLACK) {
                moves.add(MoveParser.constructMove(this, rank+1, file-1, true));
            }
            if (board.isInBounds(rank+1, file+1) && board.get(rank+1, file+1).getColor() == Piece.BLACK) {
                moves.add(MoveParser.constructMove(this, rank+1, file+1, true));
            } 
        } else {
            // One space
            if (board.isInBounds(rank-1, file) && board.isEmpty(rank-1, file)) {
                moves.add(MoveParser.constructMove(this, rank-1, file, false));
            }

            // Two space
            if (rank == 7 && board.isInBounds(rank-2, file) && board.isEmpty(rank-1, file) && board.isEmpty(rank-2, file)) {
                moves.add(MoveParser.constructMove(this, rank-2, file, false));
            }

            // Capture
            if (board.isInBounds(rank-1, file-1) && board.get(rank-1, file-1).getColor() == Piece.WHITE) {
                moves.add(MoveParser.constructMove(this, rank-1, file-1, true));
            }
            if (board.isInBounds(rank-1, file+1) && board.get(rank-1, file+1).getColor() == Piece.WHITE) {
                moves.add(MoveParser.constructMove(this, rank-1, file+1, true));
            } 
        }

        String[] ret = new String[moves.size()];
        ret = moves.toArray(ret);
        return ret;
    }

    /*
    public boolean isValidMove(int fromRow, int fromCol, int toRow, int toCol){
        boolean whitesMove = color==Piece.WHITE;
        int f = whitesMove? 1 : -1;
        boolean canMoveOne = isInBounds(fromRow + f, fromCol) && board.get(fromRow + f, fromCol).isBlank();
        boolean canMoveTwo = whitesMove ? fromRow == 2 && board.get(fromRow+f*2, fromCol).isBlank()
                                        : fromRow == 7 && board.get(fromRow+f*2, fromCol).isBlank();
        boolean canCaptureLeft = isInBounds(fromRow+f, fromCol-1)
                && board.get(fromRow+f, fromCol-1).getColor()!=this.color;
        boolean canCaptureRight = isInBounds(fromRow+f, fromCol+1)
                && board.get(fromRow+f, fromCol+1).getColor()!=this.color;

        if (canMoveOne && toRow == fromRow+f && toCol == fromCol) {
            return true;
        } else if (canMoveTwo && toRow == fromRow+f*2 && toCol == fromCol) {
            return true;
        } else if (canCaptureLeft && toRow == fromRow+f && toCol == fromCol-1) {
            return true;
        } else if (canCaptureRight && toRow == fromRow+f && toCol == fromCol+1) {
            return true;
        }
        return false; 
    }
    */


    
    public String getPicture(int row, int col) {
        String path = "images/";
		if (row%2 == 0) {
			if (col%2 == 0) {
                path += "light/";
			} else {
                path += "dark/";
			}
		} else {
			if (col%2 == 1) {
                path += "light/";
			} else {
                path += "dark/";
			}
		}
        if(color==Piece.WHITE){
            path += "white/";
        }
        else{
            path += "black/";
        }
        return path+FILE_NAME;
	}

    @Override
    public int getColor() {
        return color;
    }

    @Override
    public boolean isBlank() {
        return false;
    }

    @Override
    public String toString() {
        if (color == Piece.WHITE) {
            return "wp";
        } else {
            return "bp";
        }
    }
}
