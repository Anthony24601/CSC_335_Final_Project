import java.util.ArrayList;

public class Bishop extends Piece {
    private static final char KIND = Piece.BISHOP;

    GameModel gameModel = GameModel.getInstance();

    public Bishop(int color, int rank, int file) {
        super(color, rank, file, "bishop");
    }

    public String[] getValidMoves(Board board) {
        ArrayList<String> moves = new ArrayList<>();
        int opColor = color == Piece.WHITE ? Piece.BLACK : Piece.WHITE;
        String move;

        // up-left
        int r = rank+1;
        int f = file-1;
        while (board.isInBounds(r, f) && board.isEmpty(r, f)) {
            move = MoveParser.constructMove(this, r, f, false);
            move = gameModel.addCheck(getLoc(), move);
            moves.add(move);
            r++;
            f--;            
        }
        if (board.isInBounds(r, f) && board.get(r, f).getColor() == opColor) {
            move = MoveParser.constructMove(this, r, f, true);
            move = gameModel.addCheck(getLoc(), move);
            moves.add(move);
        }

        // up-right
        r = rank+1;
        f = file+1;
        while (board.isInBounds(r, f) && board.isEmpty(r, f)) {
            move = MoveParser.constructMove(this, r, f, false);
            move = gameModel.addCheck(getLoc(), move);
            moves.add(move);
            r++;
            f++;
        }
        if (board.isInBounds(r, f) && board.get(r, f).getColor() == opColor) {
            move = MoveParser.constructMove(this, r, f, true);
            move = gameModel.addCheck(getLoc(), move);
            moves.add(move);
        }

        // down-left
        r = rank-1;
        f = file-1;
        while (board.isInBounds(r, f) && board.isEmpty(r, f)) {
            move = MoveParser.constructMove(this, r, f, false);
            move = gameModel.addCheck(getLoc(), move);
            moves.add(move);
            r--;
            f--;
        }
        if (board.isInBounds(r, f) && board.get(r, f).getColor() == opColor) {
            move = MoveParser.constructMove(this, r, f, true);
            move = gameModel.addCheck(getLoc(), move);
            moves.add(move);
        }

        // down-right
        r = rank-1;
        f = file+1;
        while (board.isInBounds(r, f) && board.isEmpty(r, f)) {
            move = MoveParser.constructMove(this, r, f, false);
            move = gameModel.addCheck(getLoc(), move);
            moves.add(move);
            r--;
            f++;
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
        int opColor = color == Piece.WHITE ? Piece.BLACK : Piece.WHITE;
        int r, f;

        // up-left
        r = rank+1;
        f = file-1;
        while (board.isInBounds(r, f)) {
            if (board.get(r, f).getKind() == 'K' && board.get(r, f).getColor() == opColor) {
                return true;
            } else if (!board.isEmpty(r, f)) {
                break;
            }
            r++;
            f--;
        }

        // up-right
        r = rank+1;
        f = file+1;
        while (board.isInBounds(r, f)) {
            if (board.get(r, f).getKind() == 'K' && board.get(r, f).getColor() == opColor) {
                return true;
            } else if (!board.isEmpty(r, f)) {
                break;
            }
            r++;
            f++;
        }

        // down-left
        r = rank-1;
        f = file-1;
        while (board.isInBounds(r, f)) {
            if (board.get(r, f).getKind() == 'K' && board.get(r, f).getColor() == opColor) {
                return true;
            } else if (!board.isEmpty(r, f)) {
                break;
            }
            r--;
            f--;
        }

        // down-right
        r = rank-1;
        f = file+1;
        while (board.isInBounds(r, f)) {
            if (board.get(r, f).getKind() == 'K' && board.get(r, f).getColor() == opColor) {
                return true;
            } else if (!board.isEmpty(r, f)) {
                break;
            }
            r--;
            f++;
        }
        
        return false;
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
    public Bishop copy() {
        return new Bishop(color, rank, file);
    }
}
