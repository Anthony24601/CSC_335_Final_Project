/**
 * File: GameModel.java
 * Author: Grace Driskill
 * Course: CSC 335
 * Purpose: Model for chess game. Holds the current game's 
 *  Board, and is responsible for moving pieces on the Board.
 *  Allows you to check for checks, draws, valid moves... in
 *  the game.
 *  Is a singleton. Use getInstance() to access the GameModel 
 */
package Game;
import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.Scanner;

import PiecePackage.Piece;

public class GameModel implements Serializable {
	private static final long serialVersionUID = 1L;

	private static GameModel instance;

    private Board currentBoard;
    private boolean whitesTurn;
    private boolean hasCheckmate = false;
    private boolean isOver = false;
    private int count;

    /**
     * Private constructor. Creates a new GameModel
     */
    private GameModel(){
        whitesTurn = true;
        count = 0;
    }

    /**
     * Returns the current GameModel instance. Creates
     * a new instance if one doesn't already exist
     * @return this GameModel
     */
    public static GameModel getInstance() {
        if (instance == null) {
            instance = new GameModel();
        }
        return instance;
    }
    
    /**
     * Checks for all types of draws and sets the isOver
     * field accordingly. The tree types of draws are 
     * stalemate, combo and 50 move rule
     */
    public void checkDraws() {
    	checkStalemate();
    	checkCombo();
    	check50MoveRule();
    }

    /**
     * @return the current Board being used
     */
    public Board getCurrentBoard() {
        return currentBoard;
    }

    /**
     * Sets the Board being used
     * @param board Board to be current board
     */
    public void setCurrentBoard(Board board) {
        this.currentBoard = board;
    }

    /**
     * @return true if it is the white player's turn 
     */
    public boolean isWhitesTurn() {
        return whitesTurn;
    }

    /**
     * Changes the turn 
     */
    public void flipTurn() {
        whitesTurn = !whitesTurn;
    }

    /**
     * @return true if there is a checkmate in the
     *  game
     */
    public boolean getHasCheckmate() {
        return hasCheckmate;
    }

    /**
     * Sets the hasCheckmate field to true
     */
    public void setHasCheckmate() {
    	hasCheckmate = true;
    }
    
    /**
     * Checks if the game is over from a draw or 
     * checkmate
     * @return true if the game is over
     */
    public boolean getIsOver() {
        return isOver;
    }
    
    /**
     * Sets the isOver field to true
     */
    public void setIsOver() {
    	isOver = true;
    }

    /**
     * USing two location on Chess board, execute the inidcate move on 
     * the current board in the GameModel
     * @param fromLoc location the Piece starts on
     * @param toLoc location the Piece ends on
     * @return true if the move was valid, false otherwise
     */
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
     * Makes the specified move on the current board
     * @param move algebraic notation of a chess move
     * @return  true if the move was valid, false otherwise
     */
    public boolean makeMove(String move) {
        return makeMove(move, currentBoard);
    }

    /**
     * Makes the specified move on the specified board
     * @param move algebraic notation of a chess move
     * @param b Board to move on
     * @return true if the move was valid, false otherwise
     */
    public boolean makeMove(String move, Board b) {
        // check for kingside castle move
        if (move.equals("0-0")) { 
            if (canCastleKingside(b)) {
                castleKingside(b);
                flipTurn();
                count++;
                return true;
            }
        } 
        // check for queenside castle move
        else if (move.equals("0-0-0")) {
            if (canCastleQueenside(b)) {
                castleQueenside(b);
                flipTurn();
                count++;
                return true;
            }
        } 
        // typical moves
        else {
            char kind = getKindFromMove(move);
            // logic for keeping track of 50 move rule
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
                    b.moveAndSave(loc, m);
                    // "#" in chess notation indicates a checkmate
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

    /**
     * Returns the piece that would be captured if the specified move
     * was made
     * @param loc Location to move from
     * @param move move to make
     * @param b Board to move on
     * @return  the Piece captured
     */
    public Piece getPieceCaptured(String loc, String move, Board b) {
        Board futureBoard = b.copy();
        return futureBoard.move(loc, move);
    }

    /**
     * Gets all the moves possible from the specified location
     * @param loc String, location on board in algebraic notation
     * @return ArrayList of moves
     */
    public ArrayList<String> getPossibleMoves(String loc) {
        Piece p = currentBoard.get(loc);
        return getPossibleMoves(p);
    }

    /**
     * Gets all the moves possible of the specified Piece
     * @param p Piece to move
     * @return ArrayList of moves
     */
    public ArrayList<String> getPossibleMoves(Piece p) {
        ArrayList<String> moveMap = new ArrayList<>();
        String loc = p.getLoc();
        String[] validMoves = p.getValidMoves(currentBoard, this);
        for (String move : validMoves) {
            moveMap.add(loc + ":" + move);
        }
        return moveMap;
    }

    /**
     * Gets all the possible moves on the current board
     * @return ArrayList of moves
     */
    public ArrayList<String> getAllPossibleMoves() {
        return getAllPossibleMoves(currentBoard);
    }

    /**
     * Gets all the possible moves on the specified board
     * @param b Board to get moves on
     * @return ArrayList of moves
     */
    public ArrayList<String> getAllPossibleMoves(Board b) {
        ArrayList<String> moves = new ArrayList<>();
        char[] pieceKinds = {Piece.PAWN, Piece.ROOK, Piece.KNIGHT, Piece.BISHOP, Piece.QUEEN, Piece.KING};
        for (char kind : pieceKinds) {
            moves.addAll(b.getMoves(kind, whitesTurn, this));
        }
        return moves;
    }

    /**
     * Gets all the possibles moves for the opposite player 
     * on the curernt board
     * @return ArrayList of moves
     */
    public ArrayList<String> getAllPossibleOpMoves() {
        return getAllPossibleOpMoves(currentBoard); 
    }

    /**
     * Gets all the possible moves for the opposite player on 
     * the specified board
     * @param b Board to get moves on
     * @return ArrayList of moves
     */
    public ArrayList<String> getAllPossibleOpMoves(Board b) {
        flipTurn();
        ArrayList<String> moves = getAllPossibleMoves(b);
        flipTurn();
        return moves;
    }

    /**
     * Adds a check or checkmate to a move string
     * @param loc location the move starts from
     * @param move original move
     * @param b Board to work with
     * @return move string with the check flag
     */
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

    /**
     * Checks if a move would create a check
     * @param loc location the move starts from
     * @param move original move
     * @param b Board to work with
     * @return true if the move would put in a check
     */
    public boolean wouldPutInCheck(String loc, String move, Board b) {
        Board futureBoard = b.copy();
        futureBoard.move(loc, move);
        return futureBoard.hasCheck(!whitesTurn);
    }

    /**
     * Prints a string representation of the current board
     */
    public void printBoard() {
        System.out.println(currentBoard.toString());
    }

    /**
     * Check if a castle is possible on the current board
     * @return true if there can be a kindside castle
     */
    public boolean canCastleKingside() {
        return canCastleKingside(currentBoard);
    }

    /**
     * Check if a castle is possible on the specified board
     * @return true if there can be a kindside castle
     */
    public boolean canCastleKingside(Board b) {
        boolean result;
        if (whitesTurn) {
            // If either piece has moved, abandon
            if (b.getWhiteKingHasMoved() || b.getWhiteKingRookHasMoved() || b.hasCheck(!whitesTurn))
                return false;

            // Check if King would be in check moving across
            String f1 = "Kf1";
            String g1 = "Kg1";
            result = !wouldPutInCheck("e1", f1, b) && !wouldPutInCheck("e1", g1, b)
                && b.isEmpty(1, 6) && b.isEmpty(1, 7);
        } else {
            if (b.getBlackKingHasMoved() || b.getBlackKingRookHasMoved() || b.hasCheck(!whitesTurn))
                return false;
            
            String f8 = "Kf8";
            String g8 = "Kg8";
            result = !wouldPutInCheck("e8", f8, b) && !wouldPutInCheck("e8", g8, b)
                && b.isEmpty(8, 6) && b.isEmpty(8, 7);
        }

        return result;
    }

    /**
     * Performs a kingside castle
     * @param b Board to castle
     * @return ture if the castle happen 
     */
    public boolean castleKingside(Board b) {
        if (whitesTurn) {
            b.moveAndSave("e1", "0-0");    
            return true;
        } else {
            b.moveAndSave("e8", "0-0");     
            return true;
        }
    }

    /**
     * Check if a castle is possible on the current board
     * @return true if there can be a kindside castle
     */
    public boolean canCastleQueenside() {
        return canCastleQueenside(currentBoard);
    }

    /**
     * Check if a castle is possible on the specified board
     * @return true if there can be a kindside castle
     */
    public boolean canCastleQueenside(Board b) {
        boolean result;
        if (whitesTurn) {
            if (b.getWhiteKingHasMoved() || b.getWhiteQueenRookHasMoved() || b.hasCheck(!whitesTurn)) {
                return false;
            }

            String d1 = "Kd1";
            String c1 = "Kc1";
            result = !wouldPutInCheck("e1", d1, b) && !wouldPutInCheck("e1", c1, b)
                && b.isEmpty(1, 2) && b.isEmpty(1, 3) && b.isEmpty(1, 4);
        } else {
            if (b.getBlackKingHasMoved() || b.getBlackQueenRookHasMoved() || b.hasCheck(!whitesTurn)) {
                return false;
            }
            String d8 = "Kd8";
            String c8 = "Kc8";
            result = !wouldPutInCheck("e8", d8, b) && !wouldPutInCheck("e8", c8, b)
                && b.isEmpty(8, 2) && b.isEmpty(8, 3) && b.isEmpty(8, 4);
        }

        return result;
    }

    /**
     * Performs a kingside castle
     * @param b Board to castle
     * @return ture if the castle happen 
     */
    public boolean castleQueenside(Board b) {
        if (whitesTurn && canCastleQueenside()) {
            b.moveAndSave("e1", "0-0-0");
            return true;
        } else if (canCastleQueenside()) {
            b.moveAndSave("e8", "0-0-0");
            return true;
        }
        return false;
    }

    // ----PRIVATE METHODS----

    /**
     * Checks for a stalemate and sets the isOver field to
     * true if there is
     */
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

    /**
     * Checks if there is combination of piece that would
     * never result in a check make and needs to be a draw.
     * Sets the isOver field to true if so.
     */
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
    
    /**
     * Check if there is a draw from the 50 move rule and
     * sets the isOver field accordingly
     */
    private void check50MoveRule() {
    	if (count >= 50) {
    		isOver = true;
    	}
    }

    // ----Move Parser stuff----

    /**
     * Gets what kind of piece is used in the specified move
     * @param move Move string to check
     * @return char of piece type ex Q for queen, K for king
     */
    private char getKindFromMove(String move) {
        return move.charAt(0) >= 'a' && move.charAt(0) <= 'h' ? 0 : move.charAt(0);
    }

    /**
     * Constructs an algebraic notion move string from the give info
     * @param p Piece being moved
     * @param toRank rank moving to
     * @param toFile file moving to
     * @param addCapture if this move is a capture
     * @return the move String
     */
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
    
    /**
     * Converts an algebraic notion move string to two locations
     * (start and end). 
     * @param move algebraic move string to convert
     * @return String in format "<fromRank><fromFile <toRank><toFile>"
     */
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

    /**
     * Extracts the end location from an algebraic notion move string
     * @param move algebraic notion string
     * @return end location in format <toRank><toFile>
     */
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

    /**
     * Checks if a String represents a rank-file location on chess
     * board. Rank can be a-h inclusive, file can be 1-8 inclusive
     * @param loc String
     * @return true if the string is a location
     */
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

    // -----SAVE AND LOAD GAME-----
    
    /**
     * Saves the current game state to a file that 
     * can be loaded into a different GameModel in order
     * to resume the game
     * @param fileName  String, name of file to save to
     * @return true if game was saved, false otherwise
     * 
     */
    public boolean saveGame(String fileName){
  		String filestr = "games/" + fileName + ".txt";
  		int i = 1;
  		while (new File(filestr).exists()) {
  			filestr = "games/" + fileName + "(" + i + ")" + ".txt";
  			i++;
  		}
    	
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(filestr);
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
    	String filestr = "games/" + fileName;
        Scanner scanner = null;
        try {
            scanner = new Scanner(new File(filestr));
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
