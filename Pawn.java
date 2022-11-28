import java.util.ArrayList;

public class Pawn extends Piece {
    private static final String FILE_NAME = "pawn.png";
    private static final char KIND = Piece.PAWN;

    /**
     * Constructs a new pawn of the specified color on the
     * passed Board
     * @param color either Piece.BLACK or Piece.WHITE
     */
    public Pawn(int color, int rank, int file) {
        super(color, rank, file, "pawn");
    }

    public char getKind() {
        return KIND;
    }

    public String[] getValidMoves(Board board) {
        ArrayList<String> moves = new ArrayList<>();
        if (this.color == Piece.WHITE) {
            // One space
            if (board.isInBounds(rank+1, file) && board.isEmpty(rank+1, file)) {
                moves.add(MoveParser.constructMove(this, rank+1, file, false));
            }

            // Two space
            if (rank == 2 && board.isInBounds(rank+2, file) && board.isEmpty(rank+1, file) && board.isEmpty(rank+2, file)) {
                moves.add(MoveParser.constructMove(this, rank+2, file, false));
            }

            // Capture
            if (board.isInBounds(rank+1, file-1) && board.get(rank+1, file-1).getColor() == Piece.BLACK) {
                moves.add(MoveParser.constructMove(this, rank+1, file-1, true));
            }
            if (board.isInBounds(rank+1, file+1) && board.get(rank+1, file+1).getColor() == Piece.BLACK) {
                moves.add(MoveParser.constructMove(this, rank+1, file+1, true));
            } 

            // Promotion
            if(rank==7){
                moves.add(MoveParser.constructPromotionMove(rank+1, file, Piece.QUEEN));
            }
        } else {
            // One space
            if (board.isInBounds(rank-1, file) && board.isEmpty(rank-1, file)) {
                moves.add(MoveParser.constructMove(this, rank-1, file, false));
            }

            // Two space
            if (rank == 7 && board.isInBounds(rank-2, file) && board.isEmpty(rank-1, file) && board.isEmpty(rank-2, file)) {
                moves.add(MoveParser.constructMove(this, rank-2, file, false));
            }

            // Capture
            if (board.isInBounds(rank-1, file-1) && board.get(rank-1, file-1).getColor() == Piece.WHITE) {
                moves.add(MoveParser.constructMove(this, rank-1, file-1, true));
            }
            if (board.isInBounds(rank-1, file+1) && board.get(rank-1, file+1).getColor() == Piece.WHITE) {
                moves.add(MoveParser.constructMove(this, rank-1, file+1, true));
            } 

            // Promotion
             if(rank==2){
                moves.add(MoveParser.constructPromotionMove(rank-1, file, Piece.QUEEN));
            }
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
    public String toString() {
        if (color == Piece.WHITE) {
            return "wp";
        } else {
            return "bp";
        }
    }

    @Override
    public Pawn copy() {
        return new Pawn(color, rank, file);
    }
}
