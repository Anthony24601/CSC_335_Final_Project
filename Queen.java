import java.util.ArrayList;

public class Queen extends Piece {
    private static final String FILE_NAME = "queen.png";
    final static char KIND = Piece.QUEEN;

    GameModel gameModel = GameModel.getInstance();
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
        String move;
        int opColor = color == Piece.WHITE ? Piece.BLACK : Piece.WHITE;
        int r, f;

        // right
        r = rank;
        f = file+1;
        while (board.isInBounds(r, f) && board.isEmpty(r, f)) {
            move = MoveParser.constructMove(this, r, f, false);
            if (!gameModel.wouldPutInCheck(getLoc(), move)) {
                move = gameModel.addCheck(getLoc(), move);
                moves.add(move);
            }
            f++;
        }
        if (board.isInBounds(r, f) && board.get(r, f).getColor() == opColor) {
            move = MoveParser.constructMove(this, r, f, true);
            if (!gameModel.wouldPutInCheck(getLoc(), move)) {
                move = gameModel.addCheck(getLoc(), move);
                moves.add(move);
            }
        }

        // up-right
        r = rank+1;
        f = file+1;
        while (board.isInBounds(r, f) && board.isEmpty(r, f)) {
            move = MoveParser.constructMove(this, r, f, false);
            if (!gameModel.wouldPutInCheck(getLoc(), move)) {
                move = gameModel.addCheck(getLoc(), move);
                moves.add(move);
            }
            f++;
            r++;
        }
        if (board.isInBounds(r, f) && board.get(r, f).getColor() == opColor) {
            move = MoveParser.constructMove(this, r, f, true);
            if (!gameModel.wouldPutInCheck(getLoc(), move)) {
                move = gameModel.addCheck(getLoc(), move);
                moves.add(move);
            }
        }

        // up
        r = rank+1;
        f = file;
        while (board.isInBounds(r, f) && board.isEmpty(r, f)) {
            move = MoveParser.constructMove(this, r, f, false);
            if (!gameModel.wouldPutInCheck(getLoc(), move)) {
                move = gameModel.addCheck(getLoc(), move);
                moves.add(move);
            }
            r++;
        }
        if (board.isInBounds(r, f) && board.get(r, f).getColor() == opColor) {
            move = MoveParser.constructMove(this, r, f, true);
            if (!gameModel.wouldPutInCheck(getLoc(), move)) {
                move = gameModel.addCheck(getLoc(), move);
                moves.add(move);
            }
        }

        // up-left
        r = rank+1;
        f = file-1;
        while (board.isInBounds(r, f) && board.isEmpty(r, f)) {
            move = MoveParser.constructMove(this, r, f, false);
            if (!gameModel.wouldPutInCheck(getLoc(), move)) {
                move = gameModel.addCheck(getLoc(), move);
                moves.add(move);
            }
            r++;
            f--;
        }
        if (board.isInBounds(r, f) && board.get(r, f).getColor() == opColor) {
            move = MoveParser.constructMove(this, r, f, true);
            if (!gameModel.wouldPutInCheck(getLoc(), move)) {
                move = gameModel.addCheck(getLoc(), move);
                moves.add(move);
            }
        }

        // left
        r = rank;
        f = file-1;
        while (board.isInBounds(r, f) && board.isEmpty(r, f)) {
            move = MoveParser.constructMove(this, r, f, false);
            if (!gameModel.wouldPutInCheck(getLoc(), move)) {
                move = gameModel.addCheck(getLoc(), move);
                moves.add(move);
            }
            f--;
        }
        if (board.isInBounds(r, f) && board.get(r, f).getColor() == opColor) {
            move = MoveParser.constructMove(this, r, f, true);
            if (!gameModel.wouldPutInCheck(getLoc(), move)) {
                move = gameModel.addCheck(getLoc(), move);
                moves.add(move);
            }
        }

        // down-left
        r = rank-1;
        f = file-1;
        while (board.isInBounds(r, f) && board.isEmpty(r, f)) {
            move = MoveParser.constructMove(this, r, f, false);
            if (!gameModel.wouldPutInCheck(getLoc(), move)) {
                move = gameModel.addCheck(getLoc(), move);
                moves.add(move);
            }
            r--;
            f--;
        }
        if (board.isInBounds(r, f) && board.get(r, f).getColor() == opColor) {
            move = MoveParser.constructMove(this, r, f, true);
            if (!gameModel.wouldPutInCheck(getLoc(), move)) {
                move = gameModel.addCheck(getLoc(), move);
                moves.add(move);
            }
        }

        // down
        r = rank-1;
        f = file;
        while (board.isInBounds(r, f) && board.isEmpty(r, f)) {
            move = MoveParser.constructMove(this, r, f, false);
            if (!gameModel.wouldPutInCheck(getLoc(), move)) {
                move = gameModel.addCheck(getLoc(), move);
                moves.add(move);
            }
            r--;
        }
        if (board.isInBounds(r, f) && board.get(r, f).getColor() == opColor) {
            move = MoveParser.constructMove(this, r, f, true);
            if (!gameModel.wouldPutInCheck(getLoc(), move)) {
                move = gameModel.addCheck(getLoc(), move);
                moves.add(move);
            }
        }

        // down-right
        r = rank-1;
        f = file+1;
        while (board.isInBounds(r, f) && board.isEmpty(r, f)) {
            move = MoveParser.constructMove(this, r, f, false);
            if (!gameModel.wouldPutInCheck(getLoc(), move)) {
                move = gameModel.addCheck(getLoc(), move);
                moves.add(move);
            }
            r--;
            f++;
        }
        if (board.isInBounds(r, f) && board.get(r, f).getColor() == opColor) {
            move = MoveParser.constructMove(this, r, f, true);
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
    public Queen copy() {
        return new Queen(color, rank, file);
    }
}
