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
    final static boolean USE_NEW_MOVE_FORMAT = false;
    final static boolean PLAY_AGAINST_AI = false;
    final static boolean LOAD_GAME = true;
    final static boolean SAVE_GAME = true;

    public static void main(String[] args) {
        gameModel = GameModel.getInstance();
        Board board = new Board(false);
        gameModel.setCurrentBoard(board);
        
        boolean result;

        if (AUTO) {
            String[] sampleGame = {
                "e4",   "e5",
                "Nf3",  "Nc6",
                "d4",   "exd4",
                "Nxd4", "Bc5",
                "Be3",  "Qf6",
                "c3",   "Nge7",
                "g3",   "Ne5",
                "f4",   "c6",
                "fxe5", "Qxe5",
                "Bd3",  "0-0",
                "b4",   "Re8",
                "bxc5", "d5",
                "Qf3",  "b5",
                "0-0",  "Ng6",
                "Qxf7+","Kh8",
                "Nxc6", "Bh3",
                "Nxe5", "Rxe5",
                "Qb7",  "Rg8",
                "exd5", "Rxe3",
                "Bxg6", "hxg6",
                "Qf7",  "Re2",
                "Qxg6", "Bxf1",
                "Kxf1", "Rxh2",
                "Na3",  "Rd2",
                "Qh5#"
            };

            for (String move : sampleGame) {
                gameModel.printBoard();
                isInvalidMove(move);
                result = interpretMove(move);
                while (!result) {
                    System.out.println("Invalid move. Please try again.");
                    System.exit(100);
                }
                if (gameModel.getHasCheckmate()) {
                    if (gameModel.isWhitesTurn()) {
                        System.out.println("WHITE WINS!");
                    } else {
                        System.out.println("BLACK WINS!");
                    }
                    break;
                }
            }
        } else {
            playGame();
        }
    }

    public static void playGame() {
        String move = "";
        boolean result;
        TerminalAI ai = new TerminalAI(false);
        loadGame();

        do {
            gameModel.printBoard();
            if (gameModel.isWhitesTurn()) {
                System.out.println("White's turn");
            } else {
                System.out.println("Black's turn");
            }
            if (gameModel.isWhitesTurn()) {
                move = ChrisIR4.getString(MOVE_PROMPT);
                while (isInvalidMove(move)) {
                    System.out.println("Invalid format.");
                    gameModel.printBoard();
                    move = ChrisIR4.getString(MOVE_PROMPT);
                }
            } else {
                if (PLAY_AGAINST_AI) {
                    move = ai.decideOnMove();
                } else {
                    move = ChrisIR4.getString(MOVE_PROMPT);
                    while (isInvalidMove(move)) {
                        System.out.println("Invalid format.");
                        gameModel.printBoard();
                        move = ChrisIR4.getString(MOVE_PROMPT);
                    }
                }
            }
            result = interpretMove(move);
            if (!result && !move.equals("q")) {
                System.out.println("Invalid move. Please try again.");
            }
            if (gameModel.getHasCheckmate()) {
                if (gameModel.isWhitesTurn()) {
                    System.out.println("WHITE WINS!");
                } else {
                    System.out.println("BLACK WINS!");
                }
                break;
            }
        } while (!move.equals(SENTINEL));
        if(SAVE_GAME){
            String fileName = ChrisIR4.getString("Enter file to save to: ");
            gameModel.saveGame(fileName);
        }
    }

    private static void loadGame() {
        if(LOAD_GAME){
            String input = ChrisIR4.getString("Load from file? y/n");
            if(input.equals("y")){
                String fileName = ChrisIR4.getString("Enter file name: ");
                gameModel.loadGame(fileName);
            }
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
            if (USE_NEW_MOVE_FORMAT) {
                String fromLoc = move.split(" ")[0];
                String toLoc = move.split(" ")[1];
                return MoveParser.movePiece(fromLoc, toLoc);
            }
            return gameModel.makeMove(move);
        }
    }

    static boolean isInvalidMove(String move) {
        if (move.toLowerCase().equals(SENTINEL)) {
            return false;
        }

        if (USE_NEW_MOVE_FORMAT) {
            if (!move.contains(" ")) {
                return true;
            }
            String loc1 = move.split(" ")[0];
            String loc2 = move.split(" ")[1];
            if (loc1.length() != 2 || loc2.length() != 2) {
                return true;
            }
            if (loc1.charAt(0) < 'a' || loc1.charAt(0) > 'h') {
                return true;
            }
            if (loc2.charAt(0) < 'a' || loc2.charAt(0) > 'h') {
                return true;
            }
            if (loc1.charAt(1) < '1' || loc1.charAt(1) > '8') {
                return true;
            }
            if (loc2.charAt(1) < '1' || loc2.charAt(1) > '8') {
                return true;
            }
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
