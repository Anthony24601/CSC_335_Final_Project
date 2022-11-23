import ChrisIR4.ChrisIR4;

public class TerminalTest {
    private static GameModel gameModel;

    final static String SENTINEL = "q";

    final static String MOVE_PROMPT = "Please enter a move, or 'q' to quit: ";
    final static String MOVE_FORMAT_EXAMPLE = "Move format: <rank><file>-<rank><file> (e.g. a1-b2)";
    final static String BAD_FORMAT_ERROR = "Move is not formatted correctly.";
    final static String INVALID_FIRST_ERROR = "From move string is improperly formatted.";
    final static String INVALID_SECOND_ERROR = "To move string is improperly formatted.";
    final static String MISSING_DASH_ERROR = "Move string is missing a '-'.";

    final static boolean AUTO = false;

    public static void main(String[] args) {
        gameModel = GameModel.getInstance();
        boolean result;

        if (AUTO) {
            String[] sampleGame = {
                "d4",   "e6",
                "Nf3",  "Qf6",
                "Qd3",  "c5",
                "g3",   "Bd6",
                "dxc5", "Nc6",
                "cxd6", "Qf8",
                "e4",   "e5",
                "b3",   "Qd8",
                "Qd5",  "g6",
                "Bg5",  "Nf6",
                "Nc3",  "Qb6",
                "Bxf6", "h6",
                "Bxh8", "h5",
                "Ng5",  "Ke8",
                "Qxf7+","Kd8",
                "Qf8#"
            };

            for (String move : sampleGame) {
                gameModel.printBoard();
                isInvalidMove(move);
                result = gameModel.makeMove(move);
                while (!result) {
                    System.out.println("Invalid move. Please try again.");
                    System.exit(100);
                }
                /*
                String cont = ChrisIR4.getString("Press enter to continue, or enter 'q' to stop: ");
                if (cont.equals("q")) {
                    break;
                }
                */
            }
        } else {
            String move;
            do {
                gameModel.printBoard();
                if (gameModel.isWhitesTurn()) {
                    System.out.println("White's turn");
                } else {
                    System.out.println("Black's turn");
                }
                move = ChrisIR4.getString(MOVE_PROMPT);
                while (isInvalidMove(move)) {
                    move = ChrisIR4.getString(MOVE_PROMPT);
                }
                result = interpretMove(move);
                if (!result) {
                    System.out.println("Invalid move. Please try again.");
                }
            } while (!move.equals(SENTINEL));
        }
    }

    public static String getMove(String prompt) {
        String move = ChrisIR4.getString(prompt).trim();
        while (isInvalidMove(move)) {
            System.out.println(MOVE_FORMAT_EXAMPLE);
            move = ChrisIR4.getString(prompt).trim();
        }
        return move;
    }
    
    private static boolean interpretMove(String move) {
        if (move.equals("0-0")) {
            return gameModel.castleKingside();
        }
        else if (move.equals("0-0-0")) {
            return gameModel.castleQueenside();
        } else {
            return gameModel.makeMove(move);
        }
    }

    static boolean isInvalidMove(String move) {
        if (move.toLowerCase().equals(SENTINEL)) {
            return false;
        }

        if (move.length() > 5) {
            System.out.println(BAD_FORMAT_ERROR);
            return true;
        }

        if (move.equals("0-0") || move.equals("0-0-0")) {
            return false;
        }

        boolean hasPieceMoving = false;
        boolean isCapture = false;
        for (int i = 0; i < move.length(); i++) {
            char c = move.charAt(i);
            if (i == 0) {
                hasPieceMoving = ((c >= 'a' && c <= 'h') ||
                    c == 'K' || c == 'Q' || c == 'R' || c == 'B' || c == 'N');
                if (!hasPieceMoving) {
                    System.out.println(BAD_FORMAT_ERROR);
                    return true;
                }
            } 
            else if (c == 'x' && !isCapture) {
                isCapture = true;
            }
            else if (c >= '1' && c <= '8') {}
            else if (c >= 'a' && c <= 'h') {} 
            else if (c == '#' || c =='+') {}    // Check or Checkmate
            else {
                System.out.println(BAD_FORMAT_ERROR);
                return true;
            }
        }

        return false;


    }
    
}
