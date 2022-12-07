package Game;
import java.util.Random;

import PiecePackage.Piece;

import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

public class AI extends Thread {
	public static final String[] VALID_TYPES = new String[]{"random", "greedy", "minimax", "Noob", "Easy", "Hard"};
	
    private GameModel gameModel;
    private static Map<String, Integer> scoreVals;
    private static Map<String, Boolean> selfHasMoved;
    private static Map<String, Boolean> opHasMoved;

    private boolean isWhite;

    private final String AI_TYPE;
    private static final int MINIMAX_LEVELS = 1;
    private static final int CAPTURE_MULTIPLIER = 3;
    private static final int PIECE_DEVELOPMENT_DIVISOR = 2;

    public AI(boolean isWhite, String type) {
        this.isWhite = isWhite;
        this.AI_TYPE = type;
        this.gameModel = GameModel.getInstance();
        initializeScoreVals();
        initializedHasMoved();
    }

    public String decideOnMove() {
        String move = "";
        
        switch (AI_TYPE) {
        	case "Noob":
            case "random": 
                move = pickRandomMove().split(":")[1];
                break;
            case "Easy":
            case "greedy": 
                move = pickGreedyMove(true).split(":")[1];
                break;
            case "Hard":
            case "minimax":
                move = pickMinimaxMove(true, MINIMAX_LEVELS).split(":")[1];
                break;
        }
        return move;
    }
    
    // utility
    public static boolean isValidType(String type) {
    	for (String s : VALID_TYPES) {
    		if (s.equals(type)) { return true; }
    	}
    	return false;
    }

    private String pickRandomMove() {
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
            score = getMoveVal(entry, isSelf, gameModel.getCurrentBoard());
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

    private String pickMinimaxMove(boolean isSelf, int levels) {
        Random rand = new Random();
        ArrayList<String> allMoves = gameModel.getAllPossibleMoves();
        Board currentBoard = gameModel.getCurrentBoard();
        
        ArrayList<String> bestMoves = new ArrayList<>();
        int bestPoints = Integer.MIN_VALUE;
        int score;
        for (String entry : allMoves) {
            score = getMinimaxVal(entry, currentBoard, !isSelf, levels);
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

    private int getMinimaxVal(String entry, Board board, boolean isSelf, int levels) {
        String loc = entry.split(":")[0];
        String move = entry.split(":")[1];

        Board futureBoard = board.copy();
        futureBoard.move(loc, move);
        ArrayList<String> nextMoves;
        if (!isSelf) {
            nextMoves = gameModel.getAllPossibleOpMoves(futureBoard);
        } else {
            nextMoves = gameModel.getAllPossibleMoves(futureBoard);
        }

        int bestPoints = isSelf ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        int score;
        for (String nextEntry : nextMoves) {
            if (levels <= 0) {
                score = getMoveVal(nextEntry, isSelf, futureBoard);
            } else {
                score = getMinimaxVal(nextEntry, futureBoard, !isSelf, levels-1) + getMoveVal(nextEntry, isSelf, futureBoard);
            }

            if (isSelf) {
                if (score > bestPoints) {
                    bestPoints = score;
                }
            } else {
                if (score < bestPoints) {
                    bestPoints = score;
                }
            }
        }

        return bestPoints;
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
            scoreVals.put("losePawn", -1*CAPTURE_MULTIPLIER);
            scoreVals.put("loseKnight", -3*CAPTURE_MULTIPLIER);
            scoreVals.put("loseBishop", -3*CAPTURE_MULTIPLIER);
            scoreVals.put("loseRook", -5*CAPTURE_MULTIPLIER);
            scoreVals.put("loseQueen", -9*CAPTURE_MULTIPLIER);
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

    private int getMoveVal(String entry, boolean isSelf, Board board) {
        int score = 0;
        String loc = entry.split(":")[0];
        String move = entry.split(":")[1];
        Piece p = board.get(loc);

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
            switch (gameModel.getPieceCaptured(loc, move, board).getKind()) {
                case 0: score += scoreVals.get("capturePawn"); break;
                case 'R': score += scoreVals.get("captureRook"); break;
                case 'N': score += scoreVals.get("captureKnight"); break;
                case 'B': score += scoreVals.get("captureBishop"); break;
                case 'Q': score += scoreVals.get("captureQueen"); break;
            }            
        }

        // Forward position of pawn
        if (p.getKind() == Piece.PAWN) {
            recordHasMoved(loc, isSelf);
            boolean isTwo = Math.abs((int) (loc.charAt(1) - move.charAt(1))) == 2;            
            if (isTwo) {
                score += scoreVals.get("forwardPawnPos2");
            } else {
                score += scoreVals.get("forwardPawnPos");
            }
        }

        // Piece development
        if (!getHasMoved(loc, isSelf)) {
            recordHasMoved(loc, isSelf);
            score += scoreVals.get("pieceDevelopment");            
        }

        if (!isSelf) {
            score = -score;
        }
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
