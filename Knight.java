import java.util.ArrayList;

public class Knight extends Piece {
    private static final String FILE_NAME = "knight.png";
    final static char KIND = Piece.KNIGHT;

    GameModel gameModel = GameModel.getInstance();

     /**
     * Constructs a new king of the specified color on the
     * passed Board
     * @param color either Piece.BLACK or Piece.WHITE
     * @param b a Board object
     */
    public Knight(int color, int rank, int file) {
        super(color, rank, file, "knight");
    }

    public String[] getValidMoves(Board board, GameModel gameModel) {
        ArrayList<String> moves = new ArrayList<>();
        int opColor = color == Piece.WHITE ? Piece.BLACK : Piece.WHITE;
        int r, f;
        String move;
        boolean isCapture;

        // right-up
        r = rank+1;
        f = file+2;
        if (board.isInBounds(r, f) && (board.isEmpty(r, f) || board.get(r, f).getColor() == opColor)) {
            isCapture = board.get(r, f).getColor() == opColor;
            move = MoveParser.constructMove(this, r, f, isCapture);
            if (!gameModel.wouldPutInCheck(getLoc(), move)) {
                move = gameModel.addCheck(getLoc(), move);
                moves.add(move);
            }
        }

        // up-right
        r = rank+2;
        f = file+1;
        if (board.isInBounds(r, f) && (board.isEmpty(r, f) || board.get(r, f).getColor() == opColor)) {
            isCapture = board.get(r, f).getColor() == opColor;
            move = MoveParser.constructMove(this, r, f, isCapture);
            if (!gameModel.wouldPutInCheck(getLoc(), move)) {
                move = gameModel.addCheck(getLoc(), move);
                moves.add(move);
            }
        }

        // up-left
        r = rank+2;
        f = file-1;
        if (board.isInBounds(r, f) && (board.isEmpty(r, f) || board.get(r, f).getColor() == opColor)) {
            isCapture = board.get(r, f).getColor() == opColor;
            move = MoveParser.constructMove(this, r, f, isCapture);
            if (!gameModel.wouldPutInCheck(getLoc(), move)) {
                move = gameModel.addCheck(getLoc(), move);
                moves.add(move);
            }
        }

        // left-up
        r = rank+1;
        f = file-2;
        if (board.isInBounds(r, f) && (board.isEmpty(r, f) || board.get(r, f).getColor() == opColor)) {
            isCapture = board.get(r, f).getColor() == opColor;
            move = MoveParser.constructMove(this, r, f, isCapture);
            if (!gameModel.wouldPutInCheck(getLoc(), move)) {
                move = gameModel.addCheck(getLoc(), move);
                moves.add(move);
            }
        }

        // left-down
        r = rank-1;
        f = file-2;
        if (board.isInBounds(r, f) && (board.isEmpty(r, f) || board.get(r, f).getColor() == opColor)) {
            isCapture = board.get(r, f).getColor() == opColor;
            move = MoveParser.constructMove(this, r, f, isCapture);
            if (!gameModel.wouldPutInCheck(getLoc(), move)) {
                move = gameModel.addCheck(getLoc(), move);
                moves.add(move);
            }
        }

        // down-left
        r = rank-2;
        f = file-1;
        if (board.isInBounds(r, f) && (board.isEmpty(r, f) || board.get(r, f).getColor() == opColor)) {
            isCapture = board.get(r, f).getColor() == opColor;
            move = MoveParser.constructMove(this, r, f, isCapture);
            if (!gameModel.wouldPutInCheck(getLoc(), move)) {
                move = gameModel.addCheck(getLoc(), move);
                moves.add(move);
            }
        }

        // down-right
        r = rank-2;
        f = file+1;
        if (board.isInBounds(r, f) && (board.isEmpty(r, f) || board.get(r, f).getColor() == opColor)) {
            isCapture = board.get(r, f).getColor() == opColor;
            move = MoveParser.constructMove(this, r, f, isCapture);
            if (!gameModel.wouldPutInCheck(getLoc(), move)) {
                move = gameModel.addCheck(getLoc(), move);
                moves.add(move);
            }
        }

        // right-down
        r = rank-1;
        f = file+2;
        if (board.isInBounds(r, f) && (board.isEmpty(r, f) || board.get(r, f).getColor() == opColor)) {
            isCapture = board.get(r, f).getColor() == opColor;
            move = MoveParser.constructMove(this, r, f, isCapture);
            if (!gameModel.wouldPutInCheck(getLoc(), move)) {
                move = gameModel.addCheck(getLoc(), move);
                moves.add(move);
            }
        }

        String[] ret = new String[moves.size()];
        ret = moves.toArray(ret);
        return ret;
    }
   
    public boolean canCheck(Board board) {
        int opColor = this.color == Piece.WHITE ? Piece.BLACK : Piece.WHITE;
        int r, f;

        // right-up
        r = rank+1;
        f = file+2;
        if (board.isInBounds(r, f) && board.get(r, f).getKind() == 'K' && board.get(r, f).getColor() == opColor) {
            return true;
        }

        // up-right
        r = rank+2;
        f = file+1;
        if (board.isInBounds(r, f) && board.get(r, f).getKind() == 'K' && board.get(r, f).getColor() == opColor) {
            return true;
        }

        // up-left
        r = rank+2;
        f = file-1;
        if (board.isInBounds(r, f) && board.get(r, f).getKind() == 'K' && board.get(r, f).getColor() == opColor) {
            return true;
        }

        // left-up
        r = rank+1;
        f = file-2;
        if (board.isInBounds(r, f) && board.get(r, f).getKind() == 'K' && board.get(r, f).getColor() == opColor) {
            return true;
        }

        // left-down
        r = rank-1;
        f = file-2;
        if (board.isInBounds(r, f) && board.get(r, f).getKind() == 'K' && board.get(r, f).getColor() == opColor) {
            return true;
        }

        // down-left
        r = rank-2;
        f = file-1;
        if (board.isInBounds(r, f) && board.get(r, f).getKind() == 'K' && board.get(r, f).getColor() == opColor) {
            return true;
        }

        // down-right
        r = rank-2;
        f = file+1;
        if (board.isInBounds(r, f) && board.get(r, f).getKind() == 'K' && board.get(r, f).getColor() == opColor) {
            return true;
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
    public Knight copy() {
        return new Knight(color, rank, file);
    }
}
