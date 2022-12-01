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
        ArrayList<String> moveMap = b.getMoves(kind, whitesTurn, this);
        for (String entry : moveMap) {
            String loc = entry.split(":")[0];
            String m = entry.split(":")[1];
            if (m.equals(move)) {
                addHasMoved(loc, m);
                Piece capturedPiece = b.move(loc, m);
                if (capturedPiece != null) {
                    b.removePiece(capturedPiece);
                }
                whitesTurn = !whitesTurn;
                return true;
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

    public boolean wouldPutInCheck(String loc, String move) {
        Board futureBoard = currentBoard.copy();
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
    
    public boolean castleKingside() {
        if (whitesTurn) {
            // If either piece has moved, abandon
            if (whiteKingHasMoved || whiteKingRookHasMoved)
                return false;

            // Check if King would be in check moving across
            String f1 = "Kf1";
            String g1 = "Kg1";
            if (f1.equals(addCheck("e1", f1)) && g1.equals(addCheck("e1", g1)) && currentBoard.isEmpty(1, 6) && currentBoard.isEmpty(1, 7)) {
                currentBoard.move("e1", "0-0");     // TODO: Add Check for logs
                whiteKingHasMoved = true;
                whiteKingRookHasMoved = true;
                whitesTurn = !whitesTurn;
                return true;
            }
        } else {
            if (blackKingHasMoved || blackKingRookHasMoved)
                return false;

            String f8 = "Kf8";
            String g8 = "Kg8";
            if (f8.equals(addCheck("e8", f8)) && g8.equals(addCheck("e8", g8)) && currentBoard.isEmpty(8, 6) && currentBoard.isEmpty(8, 7)) {
                currentBoard.move("e8", "0-0");     // TODO: Add Check for logs
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

            String d1 = "Kd1";
            String c1 = "Kc1";
            if (d1.equals(addCheck("e1", d1)) && c1.equals(addCheck("e1", c1)) && currentBoard.isEmpty(1, 2)) {
                currentBoard.move("e1", "0-0-0");
                whiteKingHasMoved = true;
                whiteQueenRookHasMoved = true;
                return true;
            }
        } else {
            if (blackKingHasMoved || blackQueenRookHasMoved) {
                return false;
            }

            String d8 = "Ke8";
            String c8 = "Kf8";
            if (d8.equals(addCheck("e8", d8)) && c8.equals(addCheck("e8", c8)) && currentBoard.isEmpty(8, 2)) {
                currentBoard.move("d8", "0-0-0");
                blackKingHasMoved = true;
                blackQueenRookHasMoved = true;
                return true;
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
