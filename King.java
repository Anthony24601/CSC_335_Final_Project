import java.util.ArrayList;

public class King extends Piece {
    private static final String FILE_NAME = "king.png";
    final static char KIND = Piece.KING;

     /**
     * Constructs a new king of the specified color on the
     * passed Board
     * @param color either Piece.BLACK or Piece.WHITE
     * @param b a Board object
     */
    public King(int color, int rank, int file) {
        super(color, rank, file, "king");
    }

    public String[] getValidMoves(Board board, GameModel gameModel) {
        ArrayList<String> moves = new ArrayList<>();
        int opColor = color == Piece.WHITE ? Piece.BLACK : Piece.WHITE;
        int r, f;
        String move;
        boolean isCapture;

        // right
        r = rank;
        f = file+1;
        if (board.isInBounds(r, f) && (board.isEmpty(r, f) || board.get(r, f).getColor() == opColor)) {
            isCapture = board.get(r, f).getColor() == opColor;
            move = gameModel.constructMove(this, r, f, isCapture);
            if (!gameModel.wouldPutInCheck(getLoc(), move, board)) {
                move = gameModel.addCheck(getLoc(), move);
                moves.add(move);
            }
        }
        
        // up-right
        r = rank+1;
        f = file+1;
        if (board.isInBounds(r, f) && (board.isEmpty(r, f) || board.get(r, f).getColor() == opColor)) {
            isCapture = board.get(r, f).getColor() == opColor;
            move = gameModel.constructMove(this, r, f, isCapture);
            if (!gameModel.wouldPutInCheck(getLoc(), move, board)) {
                move = gameModel.addCheck(getLoc(), move);
                moves.add(move);
            }
        }
        
        // up
        r = rank+1;
        f = file;
        if (board.isInBounds(r, f) && (board.isEmpty(r, f) || board.get(r, f).getColor() == opColor)) {
            isCapture = board.get(r, f).getColor() == opColor;
            move = gameModel.constructMove(this, r, f, isCapture);
            if (!gameModel.wouldPutInCheck(getLoc(), move, board)) {
                move = gameModel.addCheck(getLoc(), move);
                moves.add(move);
            }
        }
        
        // up-left
        r = rank+1;
        f = file-1;
        if (board.isInBounds(r, f) && (board.isEmpty(r, f) || board.get(r, f).getColor() == opColor)) {
            isCapture = board.get(r, f).getColor() == opColor;
            move = gameModel.constructMove(this, r, f, isCapture);
            if (!gameModel.wouldPutInCheck(getLoc(), move, board)) {
                move = gameModel.addCheck(getLoc(), move);
                moves.add(move);
            }
        }
        
        // left
        r = rank;
        f = file-1;
        if (board.isInBounds(r, f) && (board.isEmpty(r, f) || board.get(r, f).getColor() == opColor)) {
            isCapture = board.get(r, f).getColor() == opColor;
            move = gameModel.constructMove(this, r, f, isCapture);
            if (!gameModel.wouldPutInCheck(getLoc(), move, board)) {
                move = gameModel.addCheck(getLoc(), move);
                moves.add(move);
            }
        }
        
        // down-left
        r = rank-1;
        f = file-1;
        if (board.isInBounds(r, f) && (board.isEmpty(r, f) || board.get(r, f).getColor() == opColor)) {
            isCapture = board.get(r, f).getColor() == opColor;
            move = gameModel.constructMove(this, r, f, isCapture);
            if (!gameModel.wouldPutInCheck(getLoc(), move, board)) {
                move = gameModel.addCheck(getLoc(), move);
                moves.add(move);
            }
        }
        
        // down
        r = rank-1;
        f = file;
        if (board.isInBounds(r, f) && (board.isEmpty(r, f) || board.get(r, f).getColor() == opColor)) {
            isCapture = board.get(r, f).getColor() == opColor;
            move = gameModel.constructMove(this, r, f, isCapture);
            if (!gameModel.wouldPutInCheck(getLoc(), move, board)) {
                move = gameModel.addCheck(getLoc(), move);
                moves.add(move);
            }
        }
        
        // down-right
        r = rank-1;
        f = file+1;
        if (board.isInBounds(r, f) && (board.isEmpty(r, f) || board.get(r, f).getColor() == opColor)) {
            isCapture = board.get(r, f).getColor() == opColor;
            move = gameModel.constructMove(this, r, f, isCapture);
            if (!gameModel.wouldPutInCheck(getLoc(), move, board)) {
                move = gameModel.addCheck(getLoc(), move);
                moves.add(move);
            }
        }
        
        if (gameModel.canCastleKingside()) {
            moves.add("0-0");
        }
        if (gameModel.canCastleQueenside()) {
            moves.add("0-0-0");
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
        if (board.isInBounds(r, f) && board.get(r, f).getKind() == 'K' && board.get(r, f).getColor() == opColor) {
            return true;
        }

        // up-right
        r = rank+1;
        f = file+1;
        if (board.isInBounds(r, f) && board.get(r, f).getKind() == 'K' && board.get(r, f).getColor() == opColor) {
            return true;
        }

        // up
        r = rank+1;
        f = file;
        if (board.isInBounds(r, f) && board.get(r, f).getKind() == 'K' && board.get(r, f).getColor() == opColor) {
            return true;
        }

        // up-left
        r = rank+1;
        f = file-1;
        if (board.isInBounds(r, f) && board.get(r, f).getKind() == 'K' && board.get(r, f).getColor() == opColor) {
            return true;
        }

        // left
        r = rank;
        f = file-1;
        if (board.isInBounds(r, f) && board.get(r, f).getKind() == 'K' && board.get(r, f).getColor() == opColor) {
            return true;
        }

        // down-left
        r = rank-1;
        f = file-1;
        if (board.isInBounds(r, f) && board.get(r, f).getKind() == 'K' && board.get(r, f).getColor() == opColor) {
            return true;
        }

        // down
        r = rank-1;
        f = file;
        if (board.isInBounds(r, f) && board.get(r, f).getKind() == 'K' && board.get(r, f).getColor() == opColor) {
            return true;
        }

        // down-right
        r = rank-1;
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
    public King copy() {
        return new King(color, rank, file);
    }
}
