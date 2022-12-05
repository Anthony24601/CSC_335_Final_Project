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

    /*
    private boolean whiteKingRookHasMoved = false;
    private boolean whiteQueenRookHasMoved = false;
    private boolean blackKingRookHasMoved = false;
    private boolean blackQueenRookHasMoved = false;
    private boolean whiteKingHasMoved = false;
    private boolean blackKingHasMoved = false;
    */
    private boolean hasCheckmate = false;
    private boolean isOver = false;
    
    private int count;

    private GameModel(){
        whitesTurn = true;
        count = 0;
    }

    public static GameModel getInstance() {
        if (instance == null) {
            instance = new GameModel();
        }
        return instance;
    }
    
    public void checkDraws() {
    	checkStalemate();
    	checkCombo();
    	check50MoveRule();
    }
    
    private void checkStalemate() {
    	int[] numColors = currentBoard.getNumColors();
    	//System.out.println("White: " + numColors[0]);
    	//System.out.println("Black: " + numColors[1]);
    	if (numColors[0] == 1) {
    		if (currentBoard.getMoves(Piece.KING, true, this).size() == 0) {
    			isOver = true;
    		}
    	} else if (numColors[1] == 1) {
    		if (currentBoard.getMoves(Piece.KING, false, this).size() == 0) {
    			isOver = true;
    		}
    	}
    }
    
    private void checkCombo() {
    	int[] numColors = currentBoard.getNumColors();
    	if (numColors[0] == 1 && numColors[1] == 1) { //Only two kings left
    		isOver = true;
    	} 
    	//King and bishop vs king OR king and knight vs king
    	else if ((numColors[0] == 2 && numColors[1] == 1) || (numColors[0] == 1 && numColors[1] == 2)) {
    		if (currentBoard.checkOneBishop() || currentBoard.checkOneKnight()) {
    			isOver = true;
    		}
    	}
    	//King and bishop vs. king and bishop of the same color as the opponent's bishop
    	else if (numColors[0] == 2 && numColors[1] == 2) {
    		if (currentBoard.checkBishops()) {
    			isOver = true;
    		}
    	}
    }
    
    private void check50MoveRule() {
    	if (count >= 50) {
    		isOver = true;
    	}
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
    
    public boolean getIsOver() {
        return isOver;
    }
    
    public void setIsOver() {
    	isOver = true;
    }
    
    public void setHasCheckmate() {
    	hasCheckmate = true;
    }
    
    public boolean makeMove(String move) {
        return makeMove(move, currentBoard);
    }

    public boolean makeMove(String move, Board b) {
        if (move.equals("0-0")) {
            if (canCastleKingside(b)) {
                castleKingside(b);
                flipTurn();
                count++;
                return true;
            }
        } else if (move.equals("0-0-0")) {
            if (canCastleQueenside(b)) {
                castleQueenside(b);
                flipTurn();
                count++;
                return true;
            }
        } else {
            char kind = getKindFromMove(move);
            if (move.contains("x") || kind == Piece.PAWN) {
            	count = 0;
            } else {
            	count++;
            }
            ArrayList<String> moveMap = b.getMoves(kind, whitesTurn, this);
            for (String entry : moveMap) {
                String loc = entry.split(":")[0];
                String m = entry.split(":")[1];
                if (m.equals(move)) {
                    //addHasMoved(loc, m);
                    b.moveAndSave(loc, m);
                    if (move.charAt(move.length()-1) == '#') {
                        hasCheckmate = true;
                        isOver = true;
                    } else {
                        flipTurn();
                    }      
                    return true;
                }
            }
        }

        return false;
    }

    public Piece getPieceCaptured(String loc, String move, Board b) {
        Board futureBoard = b.copy();
        return futureBoard.move(loc, move);
    }

    public ArrayList<String> getPossibleMoves(String loc) {
        Piece p = currentBoard.get(loc);
        return getPossibleMoves(p);
    }

    public ArrayList<String> getPossibleMoves(Piece p) {
        ArrayList<String> moveMap = new ArrayList<>();
        String loc = p.getLoc();
        String[] validMoves = p.getValidMoves(currentBoard, this);
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
            moves.addAll(b.getMoves(kind, whitesTurn, this));
        }
        return moves;
    }

    public ArrayList<String> getAllPossibleOpMoves() {
        return getAllPossibleOpMoves(currentBoard); 
    }

    public ArrayList<String> getAllPossibleOpMoves(Board b) {
        flipTurn();
        ArrayList<String> moves = getAllPossibleMoves(b);
        flipTurn();
        return moves;
    }

    public String addCheck(String loc, String move, Board b) {
        Board futureBoard = b.copy();
        futureBoard.move(loc, move);

        if (futureBoard.hasCheck(whitesTurn)) {
            flipTurn();
            if (futureBoard.hasCheckmate(whitesTurn, this)) {
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
            if (b.getWhiteKingHasMoved() || b.getWhiteKingRookHasMoved())
                return false;

            // Check if King would be in check moving across
            String f1 = "Kf1";
            String g1 = "Kg1";
            result = !wouldPutInCheck("e1", f1, b) && !wouldPutInCheck("e1", g1, b)
                && b.isEmpty(1, 6) && b.isEmpty(1, 7);
        } else {
            if (b.getBlackKingHasMoved() || b.getBlackKingRookHasMoved())
                return false;
            
            String f8 = "Kf8";
            String g8 = "Kg8";
            result = !wouldPutInCheck("e8", f8, b) && !wouldPutInCheck("e8", g8, b)
                && b.isEmpty(8, 6) && b.isEmpty(8, 7);
        }

        return result;
    }

    public boolean castleKingside(Board b) {
        if (whitesTurn) {
            b.move("e1", "0-0");    
            return true;
        } else {
            b.move("e8", "0-0");     
            return true;
        }
    }

    public boolean canCastleQueenside() {
        return canCastleQueenside(currentBoard);
    }

    public boolean canCastleQueenside(Board b) {
        boolean result;
        if (whitesTurn) {
            if (b.getWhiteKingHasMoved() || b.getWhiteQueenRookHasMoved()) {
                return false;
            }

            String d1 = "Kd1";
            String c1 = "Kc1";
            result = !wouldPutInCheck("e1", d1, b) && !wouldPutInCheck("e1", c1, b)
                && b.isEmpty(1, 2) && b.isEmpty(1, 3) && b.isEmpty(1, 4);
        } else {
            if (b.getBlackKingHasMoved() || b.getBlackQueenRookHasMoved()) {
                return false;
            }
            String d8 = "Kd8";
            String c8 = "Kc8";
            result = !wouldPutInCheck("e8", d8, b) && !wouldPutInCheck("e8", c8, b)
                && b.isEmpty(8, 2) && b.isEmpty(8, 3) && b.isEmpty(8, 4);
        }

        return result;
    }

    public boolean castleQueenside(Board b) {
        if (whitesTurn && canCastleQueenside()) {
            b.move("e1", "0-0-0");
            return true;
        } else if (canCastleQueenside()) {
            b.move("e8", "0-0-0");
            return true;
        }
        return false;
    }

    // Move Parser stuff ///////////////////////////////////////////////////////////

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
        ArrayList<String> moveMap = currentBoard.getMoves(kind, whitesTurn, this);
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

    public String getLocFromMove(String move) {
        if (move.equals("0-0")) {
            if (whitesTurn) {
                return "g1";
            } else {
                return "g8";
            }
        }
        else if (move.equals("0-0-0")) {
            if (whitesTurn) {
                return "c1";
            } else {
                return "c8";
            }
        }
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
        ArrayList<String> moveMap = currentBoard.getMoves(p.getKind(), whitesTurn, this);
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

    // SAVE AND LOAD GAME

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
