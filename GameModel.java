import java.util.Map;

public class GameModel {
    private static GameModel instance;

    private Board currentBoard;
    private boolean whitesTurn;
    private boolean whiteKingRookHasMoved = false;
    private boolean whiteQueenRookHasMoved = false;
    private boolean blackKingRookHasMoved = false;
    private boolean blackQueenRookHasMoved = false;
    private boolean whiteKingHasMoved = false;
    private boolean blackKingHasMoved = false;

    private GameModel(){
        whitesTurn = true;
    }

    public static GameModel getInstance() {
        if (instance == null) {
            instance = new GameModel();
        }
        return instance;
    }

    public Board getCurrentBoard() {
        return currentBoard;
    }

    public void setCurrentBoard(Board board) {
        this.currentBoard = board;
    }

    public boolean isWhitesTurn() {
        return whitesTurn;
    }

    public boolean makeMove(String move) {
        return makeMove(move, currentBoard);
    }

    public boolean makeMove(String move, Board b) {
        char kind = MoveParser.getKind(move);
        Map<String, String[]> moveMap = b.getMoves(kind, whitesTurn);
        
        for (Map.Entry<String, String[]> me : moveMap.entrySet()) {
            for (String m : me.getValue()) {
                if (m.equals(move)) {
                    addHasMoved(me.getKey(), m);
                    Piece capturedPiece = b.move(me.getKey(), m);
                    if (capturedPiece != null) {
                        b.removePiece(capturedPiece);
                    }
                    whitesTurn = !whitesTurn;
                    return true;
                }
            }
        } 
        return false;
    }

    public String addCheck(String loc, String move) {
        Board futureBoard = currentBoard.copy();
        futureBoard.move(loc, move);

        if (futureBoard.hasCheck(whitesTurn)) {
            if (futureBoard.hasCheckmate(whitesTurn)) {
                return move + "#";
            } else {
                return move + "+";
            }
        }

        return move;
    }

    private void addHasMoved(String loc, String move) {
        int rank = loc.charAt(1)-'0';
        int file = loc.charAt(0) - 'a'+1;
        Piece piece = currentBoard.get(rank, file);
        if (piece.getKind() == 'R') {
            whiteKingRookHasMoved = rank == 1 && file == 1 || whiteKingRookHasMoved;
            whiteQueenRookHasMoved = rank == 1 && file == 8 || whiteQueenRookHasMoved;
            blackKingRookHasMoved = rank == 8 && file == 1 || blackKingRookHasMoved;
            blackQueenRookHasMoved = rank == 8 && file == 8 || blackQueenRookHasMoved; 
        }
        else if (piece.getKind() == 'K') {
            whiteKingHasMoved = rank == 1 && file == 4 || whiteKingHasMoved;
            blackKingHasMoved = rank == 8 && file == 4 || blackKingHasMoved;
        }
    }



    public void printBoard() {
        System.out.println(currentBoard.toString());
    }

    public boolean castleKingside() {
        if (whitesTurn) {
            // If either piece has moved, abandon
            if (whiteKingHasMoved || whiteKingRookHasMoved)
                return false;

            // Check if King would be in check moving across
            String c1 = "Kc1";
            String b1 = "Kb1";
            if (c1.equals(addCheck("d1", c1)) && b1.equals(addCheck("d1", b1)) && currentBoard.isEmpty(1, 3) && currentBoard.isEmpty(1, 2)) {
                currentBoard.move("d1", "0-0");     // TODO: Add Check for logs
                whiteKingHasMoved = true;
                whiteKingRookHasMoved = true;
                whitesTurn = !whitesTurn;
                return true;
            }
        } else {
            if (blackKingHasMoved || blackKingRookHasMoved)
                return false;

            String c8 = "Kc8";
            String b8 = "Kb8";
            if (c8.equals(addCheck("d8", c8)) && b8.equals(addCheck("d8", b8)) && currentBoard.isEmpty(8, 3) && currentBoard.isEmpty(8, 2)) {
                currentBoard.move("d8", "0-0");     // TODO: Add Check for logs
                blackKingHasMoved = true;
                blackKingRookHasMoved = true;
                whitesTurn = !whitesTurn;
                return true;
            }
        }
        
        return false;
    }

    public boolean castleQueenside() {
        if (whitesTurn) {
            if (whiteKingHasMoved || whiteQueenRookHasMoved) {
                return false;
            }

            String e1 = "Ke1";
            String f1 = "Kf1";
            if (e1.equals(addCheck("d1", e1)) && f1.equals(addCheck("d1", f1)) && currentBoard.isEmpty(1, 7)) {
                currentBoard.move("d1", "0-0-0");
                whiteKingHasMoved = true;
                whiteQueenRookHasMoved = true;
                return true;
            }
        } else {
            if (blackKingHasMoved || blackQueenRookHasMoved) {
                return false;
            }

            String e8 = "Ke8";
            String f8 = "Kf8";
            if (e8.equals(addCheck("d8", e8)) && f8.equals(addCheck("d8", f8)) && currentBoard.isEmpty(8, 7)) {
                currentBoard.move("d8", "0-0-0");
                blackKingHasMoved = true;
                blackQueenRookHasMoved = true;
                return true;
            }
        }
        return false;
    }
}
