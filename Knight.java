import java.util.ArrayList;

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

    public String[] getValidMoves(Board board) {
        ArrayList<String> moves = new ArrayList<>();
        int opColor = color == Piece.WHITE ? Piece.BLACK : Piece.WHITE;
        boolean isCapture;

        // right-up
        if (board.isInBounds(rank+1, file+2) && board.isEmpty(rank+1, file+2)) {
            isCapture = board.get(rank+1, file+2).getColor() == opColor;
            moves.add(MoveParser.constructMove(this, rank+1, file+2, isCapture));
        }

        // up-right
        if (board.isInBounds(rank+2, file+1) && board.isEmpty(rank+2, file+1)) {
            isCapture = board.get(rank+2, file+1).getColor() == opColor;
            moves.add(MoveParser.constructMove(this, rank+2, file+1, isCapture));
        }

        // up-left
        if (board.isInBounds(rank+2, file-1) && board.isEmpty(rank+2, file-1)) {
            isCapture = board.get(rank+2, file-1).getColor() == opColor;
            moves.add(MoveParser.constructMove(this, rank+2, file-1, isCapture));
        }

        // left-up
        if (board.isInBounds(rank+1, file-2) && board.isEmpty(rank+1, file-2)) {
            isCapture = board.get(rank+1, file-2).getColor() == opColor;
            moves.add(MoveParser.constructMove(this, rank+1, file-2, isCapture));
        }

        // left-down
        if (board.isInBounds(rank-1, file-2) && board.isEmpty(rank-1, file-2)) {
            isCapture = board.get(rank-1, file-2).getColor() == opColor;
            moves.add(MoveParser.constructMove(this, rank-1, file-2, isCapture));
        }

        // down-left
        if (board.isInBounds(rank-2, file-1) && board.isEmpty(rank-2, file-1)) {
            isCapture = board.get(rank-2, file-1).getColor() == opColor;
            moves.add(MoveParser.constructMove(this, rank-2, file-1, isCapture));
        }

        // down-right
        if (board.isInBounds(rank-2, file+1) && board.isEmpty(rank-2, file+1)) {
            isCapture = board.get(rank-2, file+1).getColor() == opColor;
            moves.add(MoveParser.constructMove(this, rank-2, file+1, isCapture));
        }

        // right-down
        if (board.isInBounds(rank-1, file+2) && board.isEmpty(rank-1, file+2)) {
            isCapture = board.get(rank-1, file+2).getColor() == opColor;
            moves.add(MoveParser.constructMove(this, rank-1, file+2, isCapture));
        }

        String[] ret = new String[moves.size()];
        ret = moves.toArray(ret);
        return ret;
    }

   /*  @Override
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
   */
    @Override
    public int getColor() {
        return color;
    }

    @Override
    public boolean isBlank() {
        return false;
    }

    @Override
    public char getKind() {
        return KIND;
    }
}
