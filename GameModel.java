import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.Map;
import java.util.Scanner;

public class GameModel implements Serializable {
	private static final long serialVersionUID = 1L;

	private static GameModel instance;

    private Board currentBoard;
    private boolean whitesTurn;
    private boolean whiteKingRookHasMoved = false;
    private boolean whiteQueenRookHasMoved = false;
    private boolean blackKingRookHasMoved = false;
    private boolean blackQueenRookHasMoved = false;
    private boolean whiteKingHasMoved = false;
    private boolean blackKingHasMoved = false;
    private boolean hasCheckmate = false;

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

    public void flipTurn() {
        whitesTurn = !whitesTurn;
    }

    public boolean getHasCheckmate() {
        return hasCheckmate;
    }
    
    public boolean makeMove(String move) {
        return makeMove(move, currentBoard);
    }

    public boolean makeMove(String move, Board b) {
        if (move.equals("0-0")) {
            if (canCastleKingside(b)) {
                castleKingside(b);
                return true;
            }
        } else if (move.equals("0-0-0")) {
            if (canCastleQueenside(b)) {
                castleQueenside(b);
                return true;
            }
        } else {
            char kind = getKindFromMove(move);
            ArrayList<String> moveMap = b.getMoves(kind, whitesTurn);
            for (String entry : moveMap) {
                String loc = entry.split(":")[0];
                String m = entry.split(":")[1];
                if (m.equals(move)) {
                    addHasMoved(loc, m);
                    b.move(loc, m);
                    if (move.charAt(move.length()-1) == '#') {
                        hasCheckmate = true;
                    } else {
                        flipTurn();
                    }      
                    return true;
                }
            }
        }

        return false;
    }

    public Piece getPieceCaptured(String loc, String move) {
        Board futureBoard = currentBoard.copy();
        return futureBoard.move(loc, move);
    }

    public ArrayList<String> getPossibleMoves(String loc) {
        Piece p = currentBoard.get(loc);
        return getPossibleMoves(p);
    }

    public ArrayList<String> getPossibleMoves(Piece p) {
        ArrayList<String> moveMap = new ArrayList<>();
        String loc = p.getLoc();
        String[] validMoves = p.getValidMoves(currentBoard);
        for (String move : validMoves) {
            moveMap.add(loc + ":" + move);
        }

        return moveMap;
    }

    public ArrayList<String> getAllPossibleMoves() {
        return getAllPossibleMoves(currentBoard);
    }

    public ArrayList<String> getAllPossibleMoves(Board b) {
        ArrayList<String> moves = new ArrayList<>();
        char[] pieceKinds = {Piece.PAWN, Piece.ROOK, Piece.KNIGHT, Piece.BISHOP, Piece.QUEEN, Piece.KING};
        for (char kind : pieceKinds) {
            moves.addAll(b.getMoves(kind, whitesTurn));
        }
        return moves;
    }

    public ArrayList<String> getAllPossibleOpMoves(String loc, String move) {
        Board futureBoard = currentBoard.copy();
        return getAllPossibleOpMoves(loc, move, futureBoard);
    }

    public ArrayList<String> getAllPossibleOpMoves(String loc, String move, Board b) {
        b.move(loc, move);
        flipTurn();

        ArrayList<String> moves = new ArrayList<>();
        char[] pieceKinds = {Piece.PAWN, Piece.ROOK, Piece.KNIGHT, Piece.BISHOP, Piece.QUEEN, Piece.KING};
        for (char kind : pieceKinds) {
            moves.addAll(b.getMoves(kind, whitesTurn));
        }

        flipTurn();
        return moves;
    }

    public String addCheck(String loc, String move) {
        Board futureBoard = currentBoard.copy();
        futureBoard.move(loc, move);

        if (futureBoard.hasCheck(whitesTurn)) {
            flipTurn();
            if (futureBoard.hasCheckmate(whitesTurn)) {
                flipTurn();
                return move + "#";
            } else {
                flipTurn();
                return move + "+";
            }
        }

        return move;
    }

    public boolean wouldPutInCheck(String loc, String move, Board b) {
        Board futureBoard = b.copy();
        futureBoard.move(loc, move);
        return futureBoard.hasCheck(!whitesTurn);
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

    public boolean canCastleKingside() {
        return canCastleKingside(currentBoard);
    }

    public boolean canCastleKingside(Board b) {
        boolean result;
        if (whitesTurn) {
            // If either piece has moved, abandon
            if (whiteKingHasMoved || whiteKingRookHasMoved)
                return false;

            // Check if King would be in check moving across
            String f1 = "Kf1";
            String g1 = "Kg1";
            result = f1.equals(addCheck("e1", f1)) && g1.equals(addCheck("e1", g1)) && b.isEmpty(1, 6) && b.isEmpty(1, 7);
        } else {
            if (blackKingHasMoved || blackKingRookHasMoved)
                return false;
            
            String f8 = "Kf8";
            String g8 = "Kg8";
            result = f8.equals(addCheck("e8", f8)) && g8.equals(addCheck("e8", g8)) && b.isEmpty(8, 6) && b.isEmpty(8, 7);
        }

        return result;
    }

    public boolean castleKingside(Board b) {
        if (whitesTurn && canCastleKingside()) {
            b.move("e1", "0-0");    
            whiteKingHasMoved = true;
            whiteKingRookHasMoved = true;
            flipTurn();
            return true;
        } else if (canCastleKingside()) {
            b.move("e8", "0-0");     
            blackKingHasMoved = true;
            blackKingRookHasMoved = true;
            flipTurn();
            return true;
        }
        
        return false;
    }

    public boolean canCastleQueenside() {
        return canCastleQueenside(currentBoard);
    }

    public boolean canCastleQueenside(Board b) {
        boolean result;
        if (whitesTurn) {
            if (whiteKingHasMoved || whiteQueenRookHasMoved) {
                return false;
            }

            String d1 = "Kd1";
            String c1 = "Kc1";
            result = d1.equals(addCheck("e1", d1)) && c1.equals(addCheck("e1", c1)) && currentBoard.isEmpty(1, 2);
        } else {
            if (blackKingHasMoved || blackQueenRookHasMoved) {
                return false;
            }

            String d8 = "Ke8";
            String c8 = "Kf8";
            result = d8.equals(addCheck("e8", d8)) && c8.equals(addCheck("e8", c8)) && currentBoard.isEmpty(8, 2);
        }

        return result;
    }

    public boolean castleQueenside(Board b) {
        if (whitesTurn && canCastleQueenside()) {
            b.move("e1", "0-0-0");
            whiteKingHasMoved = true;
            whiteQueenRookHasMoved = true;
            return true;
        } else if (canCastleQueenside()) {
            b.move("e8", "0-0-0");
            blackKingHasMoved = true;
            blackQueenRookHasMoved = true;
            return true;
        }
        return false;
    }

    // Move Parser stuff
    private char getKindFromMove(String move) {
        return move.charAt(0) >= 'a' && move.charAt(0) <= 'h' ? 0 : move.charAt(0);
    }

    public String constructMove(Piece p, int toRank, int toFile, boolean addCapture) {
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
    public String constructPromotionMove(int toRank, int toFile, char newType){
        return String.format("%c%d=%c", toFile + 'a' - 1, toRank, newType);
    }
    
    public String convertAlgebraicToLocs(String move) {
        char kind = getKindFromMove(move);
        ArrayList<String> moveMap = currentBoard.getMoves(kind, whitesTurn);
        String fromLoc = "";
        String toLoc = "";
        for (String entry : moveMap) {
            fromLoc = entry.split(":")[0];
            String m = entry.split(":")[1];
            if (m.equals(move)) {
                toLoc = getLocFromMove(move);
                break;
            }
        }

        if (fromLoc.isEmpty() || toLoc.isEmpty()) {
            System.out.println("malformation of loc happened!");
            System.out.println("Move: " + move);
            System.out.println("FromLoc: " + fromLoc);
            System.out.println("ToLoc: " + toLoc);
            System.exit(600);
        }
        return fromLoc + " " + toLoc;
    }

    public static String getLocFromMove(String move) {
        if (move.charAt(move.length()-1) == '+' || move.charAt(move.length()-1) == '#') {
            move = move.substring(0, move.length()-1);
        }
        return move.substring(move.length()-2);
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

    public boolean movePieceFromLocs(String fromLoc, String toLoc) {
        Piece p = currentBoard.get(fromLoc);
        ArrayList<String> moveMap = currentBoard.getMoves(p.getKind(), whitesTurn);
        for (String entry : moveMap) {
            if (fromLoc.equals(entry.split(":")[0])) {
                String move = entry.split(":")[1];
                String moveLoc = getLocFromMove(move);
                if (moveLoc.equals(toLoc)) {
                    return makeMove(entry.split(":")[1]);
                }
            }
        }
        return false;
    }

    /**
     * Saves the current game state to a file that 
     * can be loaded into a different GameModel in order
     * to resume the game
     * @param fileName  String, name of file to save to
     * @return true if game was saved, false otherwise
     * 
     */
    public boolean saveGame(String fileName){
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(fileName);
        } catch (IOException e) {
            System.out.println("Exception while opening file");
            return false;
        }
        PrintWriter printWriter = new PrintWriter(fileWriter);
        ArrayList<String> moves = currentBoard.getMoveHistory();
        for(String move : moves){
            printWriter.println(move);
        }
        if(moves.size()%2==1){
            printWriter.println("turn:black");
        }
        else{
            printWriter.println("turn:white");
        }
        printWriter.close();
        return true;
    }

    /**
     * Reads information from a file and populates this
     * GameModel with it
     * @param fileName String, name of file containing info
     * @return true if the game was loaded succesfully, false 
     *          otherwise
     */
    public boolean loadGame(String fileName){
        Scanner scanner = null;
        try {
            scanner = new Scanner(new File(fileName));
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
            return false;
        }

        while(scanner.hasNext()){
            String move = scanner.nextLine();
            if(move.startsWith("turn:")){
                String currentTurn = move.substring(5);
                whitesTurn = (currentTurn.equals("white"));
            }
            else{
                this.makeMove(move);
            }
        }
        return true;
    }

}
