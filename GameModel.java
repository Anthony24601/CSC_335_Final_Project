import java.io.Serializable;
import java.util.Map;

public class GameModel implements Serializable {
	private static final long serialVersionUID = 1L;

	private static GameModel instance;

    private Board currentBoard;
    private boolean whitesTurn;

    private GameModel(){
        currentBoard = new Board();
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

    public boolean isWhitesTurn() {
        return whitesTurn;
    }

    public boolean makeMove(String move) {
        char kind = MoveParser.getKind(move);
        Map<String, String[]> moveMap = currentBoard.getMoves(kind, whitesTurn);
        
        for (Map.Entry<String, String[]> me : moveMap.entrySet()) {
            for (String m : me.getValue()) {
                if (m.equals(move)) {
                    Piece capturedPiece = currentBoard.move(me.getKey(), m);
                    if (capturedPiece != null) {
                        currentBoard.removePiece(capturedPiece);
                    }
                    whitesTurn = !whitesTurn;
                    return true;
                }
            }
        } 
        return false;
    }

    public void printBoard() {
        System.out.println(currentBoard.toString());
    }

    public boolean castleKingside() {
        // TODO
        return false;
    }

    public boolean castleQueenside() {
        // TODO
        return false;
    }
}
