import java.util.ArrayList;

public class Bishop extends Piece {
    private static final String FILE_NAME = "bishop.png";
    private static final char KIND = Piece.BISHOP;

    public Bishop(int color, int rank, int file) {
        super(color, rank, file, "bishop");
    }

    public String[] getValidMoves(Board board) {
        ArrayList<String> moves = new ArrayList<>();
        int opColor = color == Piece.WHITE ? Piece.BLACK : Piece.WHITE;

        // up-left
        int r = rank+1;
        int f = file-1;
        while (board.isInBounds(r, f) && board.isEmpty(r, f)) {
            moves.add(MoveParser.constructMove(this, r, f, false));
            r++;
            f--;            
        }
        if (board.isInBounds(r, f) && board.get(r, f).getColor() == opColor) {
            moves.add(MoveParser.constructMove(this, r, f, true));
        }

        // up-right
        r = rank+1;
        f = file+1;
        while (board.isInBounds(r, f) && board.isEmpty(r, f)) {
            moves.add(MoveParser.constructMove(this, r, f, false));
            r++;
            f++;
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

    public int getRank() {
        return rank;
    }

    public int getFile() {
        return file;
    }

    public char getKind() {
        return KIND;
    } 

    /*
    @Override
    public boolean isValidMove(int fromRow, int fromCol, int toRow, int toCol) {
        // TODO Auto-generated method stub
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
    */

    @Override
    public int getColor() {
        return color;
    }

    @Override
    public boolean isBlank() {
        return false;
    }
}
