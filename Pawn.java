public class Pawn extends Piece {
    private static final String FILE_NAME = "pawn.png";
    private Board board;

    /**
     * Constructs a new pawn of the specified color on the
     * passed Board
     * @param color either Piece.BLACK or Piece.WHITE
     * @param b a Board object
     */
    public Pawn(int color, Board b) {
        super(color);
        board = b;
    }

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
}
