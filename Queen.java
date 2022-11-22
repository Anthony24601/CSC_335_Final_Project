import java.util.ArrayList;

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

    public String[] getValidMoves(Board board) {
        ArrayList<String> moves = new ArrayList<>();
        int opColor = color == Piece.WHITE ? Piece.BLACK : Piece.WHITE;
        int r, f;

        // right
        r = rank;
        f = file+1;
        while (board.isInBounds(r, f) && board.isEmpty(r, f)) {
            moves.add(MoveParser.constructMove(this, r, f, false));
            f++;
        }
        if (board.isInBounds(r, f) && board.get(r, f).getColor() == opColor) {
            moves.add(MoveParser.constructMove(this, r, f, true));
        }

        // up-right
        r = rank+1;
        f = file+1;
        while (board.isInBounds(r, f) && board.isEmpty(r, f)) {
            moves.add(MoveParser.constructMove(this, r, f, false));
            f++;
            r++;
        }
        if (board.isInBounds(r, f) && board.get(r, f).getColor() == opColor) {
            moves.add(MoveParser.constructMove(this, r, f, true));
        }

        // up
        r = rank+1;
        f = file;
        while (board.isInBounds(r, f) && board.isEmpty(r, f)) {
            moves.add(MoveParser.constructMove(this, r, f, false));
            r++;
        }
        if (board.isInBounds(r, f) && board.get(r, f).getColor() == opColor) {
            moves.add(MoveParser.constructMove(this, r, f, true));
        }

        // up-left
        r = rank+1;
        f = file-1;
        while (board.isInBounds(r, f) && board.isEmpty(r, f)) {
            moves.add(MoveParser.constructMove(this, r, f, false));
            r++;
            f--;
        }
        if (board.isInBounds(r, f) && board.get(r, f).getColor() == opColor) {
            moves.add(MoveParser.constructMove(this, r, f, true));
        }

        // left
        r = rank;
        f = file-1;
        while (board.isInBounds(r, f) && board.isEmpty(r, f)) {
            moves.add(MoveParser.constructMove(this, r, f, false));
            f--;
        }
        if (board.isInBounds(r, f) && board.get(r, f).getColor() == opColor) {
            moves.add(MoveParser.constructMove(this, r, f, true));
        }

        // down-left
        r = rank-1;
        f = file-1;
        while (board.isInBounds(r, f) && board.isEmpty(r, f)) {
            moves.add(MoveParser.constructMove(this, r, f, false));
            r--;
            f--;
        }
        if (board.isInBounds(r, f) && board.get(r, f).getColor() == opColor) {
            moves.add(MoveParser.constructMove(this, r, f, true));
        }

        // down
        r = rank-1;
        f = file;
        while (board.isInBounds(r, f) && board.isEmpty(r, f)) {
            moves.add(MoveParser.constructMove(this, r, f, false));
            r--;
        }
        if (board.isInBounds(r, f) && board.get(r, f).getColor() == opColor) {
            moves.add(MoveParser.constructMove(this, r, f, true));
        }

        // down-right
        r = rank-1;
        f = file+1;
        while (board.isInBounds(r, f) && board.isEmpty(r, f)) {
            moves.add(MoveParser.constructMove(this, r, f, false));
            r--;
            f++;
        }
        if (board.isInBounds(r, f) && board.get(r, f).getColor() == opColor) {
            moves.add(MoveParser.constructMove(this, r, f, true));
        }

        String[] ret = new String[moves.size()];
        ret = moves.toArray(ret);
        return ret;
    }

    @Override
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
    public char getKind() {
        return KIND;
    }
}
