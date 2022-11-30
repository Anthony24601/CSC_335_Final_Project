import java.util.Random;
import java.util.ArrayList;

public class TerminalAI {
    private static GameModel gameModel = GameModel.getInstance();
    
    public static String decideOnMove() {
        Board board = gameModel.getCurrentBoard();
        return pickRandomMove(board);
    }

    private static String pickRandomMove(Board board) {
        Random rand = new Random();
        ArrayList<String> allMoves = gameModel.getAllPossibleMoves();
        String entry = allMoves.get(rand.nextInt(allMoves.size()));
        return entry.split(":")[1];
    }
}
