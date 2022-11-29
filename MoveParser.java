
public class MoveParser {
    static GameModel gameModel;

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

    public static void movePiece(String fromLoc, String toLoc) {
        
    }
}
