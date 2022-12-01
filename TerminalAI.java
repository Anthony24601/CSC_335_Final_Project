import java.util.Random;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

public class TerminalAI {
    private static GameModel gameModel = GameModel.getInstance();
    private static Map<String, Integer> scoreVals;
    private static Map<String, Boolean> hasMoved;

    private boolean isWhite;

    private static final String AI_TYPE = "greedy";
    
    public TerminalAI(boolean isWhite) {
        this.isWhite = isWhite;
    }

  /*public String decideOnMove() {
        Board board = gameModel.getCurrentBoard();
        switch (AI_TYPE) {
            case "random": return pickRandomMove(board);
            case "greedy": return pickGreedyMove(board);
        }
        return "";
    }

    private static String pickRandomMove(Board board) {
        Random rand = new Random();
        ArrayList<String> allMoves = gameModel.getAllPossibleMoves();
        String entry = allMoves.get(rand.nextInt(allMoves.size()));
        return entry.split(":")[1];
    }

    private String pickGreedyMove(Board board) {
        ArrayList<String> allMoves = gameModel.getAllPossibleMoves();
        initializeScoreVals();
        initializedHasMoved();
        Random rand = new Random();
        
        ArrayList<String> bestMoves = new ArrayList<>();
        int bestPoints = Integer.MIN_VALUE;
        int score;
        for (String entry : allMoves) {
            score = getMoveVal(entry);
            if (score > bestPoints) {
                bestMoves.clear();
                bestMoves.add(entry.split(":")[1]);
                bestPoints = score;
            } else if (score == bestPoints) {
                bestMoves.add(entry.split(":")[1]);
            }
        }

        return bestMoves.get(rand.nextInt(bestMoves.size()));
    }
*/
    private void initializeScoreVals() {
        if (scoreVals == null) {
            scoreVals = new HashMap<>();
            scoreVals.put("checkmate", 100);
            scoreVals.put("check", 5);
            scoreVals.put("captureGeneric", 5);
            scoreVals.put("forwardPawnPos", 1);
            scoreVals.put("forwardPawnPos2", 2);
            scoreVals.put("pieceDevelopment", 3);
        }
    }

    private void initializedHasMoved() {
        if (hasMoved == null) {
            hasMoved = new HashMap<>();
            if (isWhite) {
                String[] locs = {"a1", "b1", "c1", "d1", "f1", "g1", "h1"};
                for (String loc : locs) {
                    hasMoved.put(loc, false);
                }
            } else {
                String[] locs = {"a8", "b8", "c8", "d8", "f8", "g8", "h8"};
                for (String loc : locs) {
                    hasMoved.put(loc, false);
                }  
            }
        }
    }

    private int getMoveVal(String entry) {
        int score = 0;
        String loc = entry.split(":")[0];
        String move = entry.split(":")[1];

        // Positives --------
        // Checkmate
        if (move.charAt(move.length()-1) == '#') {
            recordHasMoved(loc);            
            score += scoreVals.get("checkmate");
        }

        // Check
        if (move.charAt(move.length()-1) == '+') {
            recordHasMoved(loc);            
            score += scoreVals.get("check");
        }

        // Capture piece
        if (move.contains("x")) {
            recordHasMoved(loc);            
            score += scoreVals.get("captureGeneric");
        }

        // Forward position of pawn
        boolean isPawn = move.charAt(0) >= 'a' && move.charAt(0) <= 'h' && move.charAt(1) >= '1' && move.charAt(1) <= '8';
        if (isPawn) {
            recordHasMoved(loc);
            boolean isTwo = Math.abs((int) (loc.charAt(1) - move.charAt(1))) == 2;            
            if (isTwo) {
                score += scoreVals.get("forwardPawnPos2");
            } else {
                score += scoreVals.get("forwardPawnPos");
            }
        }

        // Piece development
        if (getHasMoved(loc)) {
            recordHasMoved(loc);
            score += scoreVals.get("pieceDevelopment");            

        }

        // TODO: Openings

        // Negatives ---------
        // Lose game
        // Lose piece
        // Put into check

        return score;
    }

    private void recordHasMoved(String loc) {
        for (String key : hasMoved.keySet()) {
            if (loc.equals(key)) {
                hasMoved.replace(key, true);
                return;
            }
        }
    }

    private boolean getHasMoved(String key) {
        if (hasMoved.containsKey(key)) {
            return hasMoved.get(key);
        }
        return false;
    }
}
