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

    public String[] getValidMoves(Board board) {
        ArrayList<String> moves = new ArrayList<>();
        int opColor = color == Piece.WHITE ? Piece.BLACK : Piece.WHITE;
        boolean isCapture;

        // right
        if (board.isInBounds(rank, file+1) && board.isEmpty(rank, file+1)) {
            isCapture = board.get(rank, file+1).getColor() == opColor;
            moves.add(MoveParser.constructMove(this, rank, file+1, isCapture));
        }
        
        // up-right
        if (board.isInBounds(rank+1, file+1) && board.isEmpty(rank+1, file+1)) {
            isCapture = board.get(rank+1, file+1).getColor() == opColor;
            moves.add(MoveParser.constructMove(this, rank+1, file+1, isCapture));
        }
        
        // up
        if (board.isInBounds(rank+1, file) && board.isEmpty(rank+1, file)) {
            isCapture = board.get(rank+1, file).getColor() == opColor;
            moves.add(MoveParser.constructMove(this, rank+1, file, isCapture));
        }
        
        // up-left
        if (board.isInBounds(rank+1, file-1) && board.isEmpty(rank+1, file-1)) {
            isCapture = board.get(rank+1, file-1).getColor() == opColor;
            moves.add(MoveParser.constructMove(this, rank+1, file-1, isCapture));
        }
        
        // left
        if (board.isInBounds(rank, file-1) && board.isEmpty(rank, file-1)) {
            isCapture = board.get(rank, file-1).getColor() == opColor;
            moves.add(MoveParser.constructMove(this, rank, file-1, isCapture));
        }
        
        // down-left
        if (board.isInBounds(rank-1, file-1) && board.isEmpty(rank-1, file-1)) {
            isCapture = board.get(rank-1, file-1).getColor() == opColor;
            moves.add(MoveParser.constructMove(this, rank-1, file-1, isCapture));
        }
        
        // down
        if (board.isInBounds(rank-1, file) && board.isEmpty(rank-1, file)) {
            isCapture = board.get(rank-1, file).getColor() == opColor;
            moves.add(MoveParser.constructMove(this, rank-1, file, isCapture));
        }
        
        // down-right
        if (board.isInBounds(rank-1, file+1) && board.isEmpty(rank-1, file+1)) {
            isCapture = board.get(rank-1, file+1).getColor() == opColor;
            moves.add(MoveParser.constructMove(this, rank-1, file+1, isCapture));
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
