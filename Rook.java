import java.util.ArrayList;

public class Rook extends Piece {
    private static final String FILE_NAME = "rook.png";
    final static char KIND = 'R';

    GameModel gameModel = GameModel.getInstance();

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
        String move;
        int r, f;

        // right
        r = rank;
        f = file+1;
        while (board.isInBounds(r, f) && board.isEmpty(r, f)) {
            move = MoveParser.constructMove(this, r, f, false);
            move = gameModel.addCheck(getLoc(), move);
            moves.add(move);
            f++;
        }
        if (board.isInBounds(r, f) && board.get(r, f).getColor() == opColor) {
            move = MoveParser.constructMove(this, r, f, true);
            move = gameModel.addCheck(getLoc(), move);
            moves.add(move);
        }

        // up
        r = rank+1;
        f = file;
        while (board.isInBounds(r, f) && board.isEmpty(r, f)) {
            move = MoveParser.constructMove(this, r, f, false);
            move = gameModel.addCheck(getLoc(), move);
            moves.add(move);
            r++;
        }
        if (board.isInBounds(r, f) && board.get(r, f).getColor() == opColor) {
            move = MoveParser.constructMove(this, r, f, true);
            move = gameModel.addCheck(getLoc(), move);
            moves.add(move);
        }

        // left
        r = rank;
        f = file-1;
        while (board.isInBounds(r, f) && board.isEmpty(r, f)) {
            move = MoveParser.constructMove(this, r, f, false);
            move = gameModel.addCheck(getLoc(), move);
            moves.add(move);
            f--;
        }
        if (board.isInBounds(r, f) && board.get(r, f).getColor() == opColor) {
            move = MoveParser.constructMove(this, r, f, true);
            move = gameModel.addCheck(getLoc(), move);
            moves.add(move);
        }

        // down
        r = rank-1;
        f = file;
        while (board.isInBounds(r, f) && board.isEmpty(r, f)) {
            move = MoveParser.constructMove(this, r, f, false);
            move = gameModel.addCheck(getLoc(), move);
            moves.add(move);
            r--;
        }
        if (board.isInBounds(r, f) && board.get(r, f).getColor() == opColor) {
            move = MoveParser.constructMove(this, r, f, true);
            move = gameModel.addCheck(getLoc(), move);
            moves.add(move);
        }

        String[] ret = new String[moves.size()];
        ret = moves.toArray(ret);
        return ret;
    }
   
    public boolean canCheck(Board board) {
        int opColor = this.color == Piece.WHITE ? Piece.BLACK : Piece.WHITE;
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
