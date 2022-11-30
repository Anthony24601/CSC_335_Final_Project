import java.util.ArrayList;

public class MoveParser {
    static GameModel gameModel = GameModel.getInstance();

    public static char getKind(String move) {
        return move.charAt(0) >= 'a' && move.charAt(0) <= 'h' ? 0 : move.charAt(0);
    }

    public static String constructMove(Piece p, int toRank, int toFile, boolean addCapture) {
        StringBuilder out = new StringBuilder();
        if (p.getKind() != 0) {
            out.append(p.getKind());
        } else {
            out.append(String.format("%c", p.getFile()+'a'-1));
        }
        if (addCapture) {
            out.append('x');
        }
        if (p.getKind() != 0 || addCapture) {
            out.append(String.format("%c%d", toFile + 'a' - 1, toRank ));
        } else {
            out.append(String.format("%d", toRank));
        }

        return out.toString();
    }

    /**
     * Constructs a move string for a pawn promotion event.
     * Is in form <newLocation>=<newType> (for example e8=Q means moves to e8 and 
     * promotes to a Queen)
     */
    public static String constructPromotionMove(int toRank, int toFile, char newType){
        return String.format("%c%d=%c", toFile + 'a' - 1, toRank, newType);
    }

    public static boolean isValidLoc(String loc) {
        if (loc.length() != 2) {
            return false;
        }
        if (loc.charAt(0) < 'a' || loc.charAt(0) > 'h') {
            return false;
        }
        if (loc.charAt(1) < '1' || loc.charAt(1) > '8') {
            return false;
        }
        return true;
    }

    public static boolean movePiece(String fromLoc, String toLoc) {
        Board board = gameModel.getCurrentBoard();
        boolean whitesTurn = gameModel.isWhitesTurn();
        Piece p = board.get(fromLoc);
        ArrayList<String> moveMap = board.getMoves(p.getKind(), whitesTurn);
        for (String entry : moveMap) {
            if (fromLoc.equals(entry.split(":")[0])) {
                String move = entry.split(":")[1];
                // TODO: Check against pawn promotion
                if (move.charAt(move.length()-1) == '+' || move.charAt(move.length()-1) == '#') {
                    move = move.substring(0, move.length()-1);
                }
                String moveLoc = move.substring(move.length()-2);
                if (moveLoc.equals(toLoc)) {
                    return gameModel.makeMove(entry.split(":")[1]);
                }
            }
        }
        return false;
    }
}
