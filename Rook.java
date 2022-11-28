import java.util.ArrayList;

public class Rook extends Piece {
    private static final String FILE_NAME = "rook.png";
    final static char KIND = 'R';

     /**
     * Constructs a new rook of the specified color on the
     * passed Board
     * @param color either Piece.BLACK or Piece.WHITE
     * @param b a Board object
     */
    public Rook(int color, int rank, int file) {
        super(color, rank, file, "rook");
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

        String[] ret = new String[moves.size()];
        ret = moves.toArray(ret);
        return ret;
    }
   
    public boolean canCheck(Board board) {
        // TODO
        return false;
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

    @Override
    public Rook copy() {
        return new Rook(color, rank, file);
    }
}
