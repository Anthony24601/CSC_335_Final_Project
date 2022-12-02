import java.util.Random;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

public class AI {
    private static GameModel gameModel = GameModel.getInstance();
    private static Map<String, Integer> scoreVals;
    private static Map<String, Boolean> selfHasMoved;
    private static Map<String, Boolean> opHasMoved;

    private boolean isWhite;
    private Board board;

    private static final String AI_TYPE = "greedy";
    private static final int CAPTURE_MULTIPLIER = 3;
    
    public AI(boolean isWhite) {
        this.isWhite = isWhite;
        initializeScoreVals();
        initializedHasMoved();
    }

    public String decideOnMove() {
        board = gameModel.getCurrentBoard();
        String move = "";
        switch (AI_TYPE) {
            case "random": 
                move = pickRandomMove().split(":")[1];
                break;
            case "greedy": 
                move = pickGreedyMove(true).split(":")[1];
                break;
        }
        return move;
    }

    private static String pickRandomMove() {
        Random rand = new Random();
        ArrayList<String> allMoves = gameModel.getAllPossibleMoves();
        return allMoves.get(rand.nextInt(allMoves.size()));
    }

    private String pickGreedyMove(boolean isSelf) {
        ArrayList<String> allMoves = gameModel.getAllPossibleMoves();
        return pickGreedyMove(allMoves, isSelf);
    }

    private String pickGreedyMove(ArrayList<String> allMoves, boolean isSelf) {
        Random rand = new Random();
        
        ArrayList<String> bestMoves = new ArrayList<>();
        int bestPoints = Integer.MIN_VALUE;
        int score;
        for (String entry : allMoves) {
            score = getMoveVal(entry, isSelf);
            if (score > bestPoints) {
                bestMoves.clear();
                bestMoves.add(entry);
                bestPoints = score;
            } else if (score == bestPoints) {
                bestMoves.add(entry);
            }
        }

        return bestMoves.get(rand.nextInt(bestMoves.size()));
    }

    private void initializeScoreVals() {
        if (scoreVals == null) {
            scoreVals = new HashMap<>();
            scoreVals.put("checkmate", 100);
            scoreVals.put("check", 5);
            //scoreVals.put("captureGeneric", 5);
            scoreVals.put("capturePawn", 1*CAPTURE_MULTIPLIER);
            scoreVals.put("captureKnight", 3*CAPTURE_MULTIPLIER);
            scoreVals.put("captureBishop", 3*CAPTURE_MULTIPLIER);
            scoreVals.put("captureRook", 5*CAPTURE_MULTIPLIER);
            scoreVals.put("captureQueen", 9*CAPTURE_MULTIPLIER);
            scoreVals.put("forwardPawnPos", 1);
            scoreVals.put("forwardPawnPos2", 2);
            scoreVals.put("pieceDevelopment", 3);
            scoreVals.put("kingCastle", 3);
            scoreVals.put("queenCastle", 3);
        }
    }

    private void initializedHasMoved() {
        if (selfHasMoved == null) {
            selfHasMoved = new HashMap<>();
            if (isWhite) {
                String[] locs = {"a1", "b1", "c1", "d1", "f1", "g1", "h1"};
                for (String loc : locs) {
                    selfHasMoved.put(loc, false);
                }
            } else {
                String[] locs = {"a8", "b8", "c8", "d8", "f8", "g8", "h8"};
                for (String loc : locs) {
                    selfHasMoved.put(loc, false);
                }  
            }
        }
        if (opHasMoved == null) {
            opHasMoved = new HashMap<>();
            if (!isWhite) {
                String[] locs = {"a1", "b1", "c1", "d1", "f1", "g1", "h1"};
                for (String loc : locs) {
                    opHasMoved.put(loc, false);
                }
            } else {
                String[] locs = {"a8", "b8", "c8", "d8", "f8", "g8", "h8"};
                for (String loc : locs) {
                    opHasMoved.put(loc, false);
                }  
            }
        }
    }

    private int getMoveVal(String entry, boolean isSelf) {
        int score = 0;
        String loc = entry.split(":")[0];
        String move = entry.split(":")[1];

        // Positives --------
        // Checkmate
        if (move.charAt(move.length()-1) == '#') {
            recordHasMoved(loc, isSelf);            
            score += scoreVals.get("checkmate");
        }

        // Check
        if (move.charAt(move.length()-1) == '+') {
            recordHasMoved(loc, isSelf);            
            score += scoreVals.get("check");
        }

        // Capture piece
        if (move.contains("x")) {
            recordHasMoved(loc, isSelf);
            switch (gameModel.getPieceCaptured(loc, move).getKind()) {
                case 0: score += scoreVals.get("capturePawn"); break;
                case 'R': score += scoreVals.get("captureRook"); break;
                case 'N': score += scoreVals.get("captureKnight"); break;
                case 'B': score += scoreVals.get("captureBishop"); break;
                case 'Q': score += scoreVals.get("captureQueen"); break;
            }            
        }

        // Forward position of pawn
        boolean isPawn = move.charAt(0) >= 'a' && move.charAt(0) <= 'h' && move.charAt(1) >= '1' && move.charAt(1) <= '8';
        if (isPawn) {
            recordHasMoved(loc, isSelf);
            boolean isTwo = Math.abs((int) (loc.charAt(1) - move.charAt(1))) == 2;            
            if (isTwo) {
                score += scoreVals.get("forwardPawnPos2");
            } else {
                score += scoreVals.get("forwardPawnPos");
            }
        }

        // Piece development
        if (getHasMoved(loc, isSelf)) {
            recordHasMoved(loc, isSelf);
            score += scoreVals.get("pieceDevelopment");            
        }

        // TODO: Openings

        // Negatives ---------
        // Lose game
        // Lose piece
        // Put into check

        return score;
    }

    private void recordHasMoved(String loc, boolean isSelf) {
        if (isSelf) {
            for (String key : selfHasMoved.keySet()) {
                if (loc.equals(key)) {
                    selfHasMoved.replace(key, true);
                    return;
                }
            }
        } else {
            for (String key : opHasMoved.keySet()) {
                if (loc.equals(key)) {
                    opHasMoved.replace(key, true);
                    return;
                }
            }
        }
    }

    private boolean getHasMoved(String key, boolean isSelf) {
        if (isSelf) {
            if (selfHasMoved.containsKey(key)) {
                return selfHasMoved.get(key);
            }
            return false;
        } else {
            if (opHasMoved.containsKey(key)) {
                return opHasMoved.get(key);
            }
            return false;
        }
    }
}
