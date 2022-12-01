import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.io.Serializable;
import java.util.ArrayList;

public class Board implements Serializable {
	private static final long serialVersionUID = 1L;

	final static boolean USE_TEMP_BOARD = true;

	final static int RANKS = 8;
    final static int FILES = 8;

    public final static char MAX_RANK = '1' + RANKS - 1;
    public final static char MAX_FILE = 'a' + FILES - 1;

	public final static int MAX_ROW = RANKS - 1;
	public final static int MAX_COLUMN = FILES - 1;

	private Piece blackKing;
	private Piece whiteKing;
	private Piece passantSquare;
	private ArrayList<Piece> queens;
	private ArrayList<Piece> bishops;
	private ArrayList<Piece> knights;
	private ArrayList<Piece> rooks;
	private ArrayList<Piece> pawns;

	private Piece[][] board;

	public Board(boolean isBlank) {
		board = new Piece[8][8];
		queens = new ArrayList<>();
		bishops = new ArrayList<>();
		knights = new ArrayList<>();
		rooks = new ArrayList<>();
		pawns = new ArrayList<>();
		if (isBlank) {
			for (int r = 0; r < RANKS; r++) {
				for (int f = 0; f < FILES; f++) {
					board[r][f] = new Blank(Piece.BLANK, r, f);
				}
			}
		} else {
			if (USE_TEMP_BOARD) {
				temp_board();
			} else {
				reset();
			}
		}
	}

	public Board copy() {
		Board newBoard = new Board(true);
		
		newBoard.blackKing = blackKing.copy();
		newBoard.whiteKing = whiteKing.copy();

		newBoard.placePiece(newBoard.blackKing);
		newBoard.placePiece(newBoard.whiteKing);
		for (Piece q : queens) 
			newBoard.placePiece(q.copy());
		for (Piece b : bishops)
			newBoard.placePiece(b.copy());
		for (Piece n : knights)
			newBoard.placePiece(n.copy());
		for (Piece r : rooks)
			newBoard.placePiece(r.copy());
		for (Piece p : pawns)
			newBoard.placePiece(p.copy());

		return newBoard;
	}

	public ArrayList<String> getMoves(boolean isWhite, GameModel gamemodel) {
		ArrayList<String> moveMap = new ArrayList<>();
		String loc;
		int color = isWhite ? Piece.WHITE : Piece.BLACK;

		for (int r = 0; r < 8; r++) {
			for (int f = 0; f < 8; f++) {
				if (board[r][f] != null && board[r][f].getColor() == color) {
					loc = String.format("%c%d", f + 'a', r+1);
					moveMap.add(loc + ':' + board[r][f].getValidMoves(this, gamemodel));
				}
			}
		}

		return moveMap;
	}

	public ArrayList<String> getMoves(char kind, boolean isWhite, GameModel gamemodel) {
		ArrayList<Piece> pieces = new ArrayList<>();
		int color = isWhite ? Piece.WHITE : Piece.BLACK;

		switch (kind) {
			case 0: pieces = pawns; break;
			case 'K': pieces.add(blackKing); pieces.add(whiteKing); break;
			case 'Q': 
				pieces = queens;
				break;
			case 'B': pieces = bishops; break;
			case 'N': pieces = knights; break;
			case 'R': pieces = rooks; break;
		}

		ArrayList<String> moveMap = new ArrayList<>();
		for (Piece p : pieces) {
			if (p.getColor() == color) {
				String[] validMoves = p.getValidMoves(this, gamemodel);
				for (String vm : validMoves) {
					moveMap.add(p.getLoc() + ":" + vm);
				}
			}
		}

		adjustSameSpace(moveMap);
		return moveMap;
	}

	private void adjustSameSpace(ArrayList<String> moveMap) {
		String entry1, entry2, loc1, loc2, move1, move2;
		for (int i = 0; i < moveMap.size(); i++) {
			entry1 = moveMap.get(i);
			for (int j = i+1; j < moveMap.size(); j++) {
				entry2 = moveMap.get(j);
				move1 = entry1.split(":")[1];
				move2 = entry2.split(":")[1];
				if (move1.equals(move2)) {
					loc1 = entry1.split(":")[0];
					loc2 = entry2.split(":")[0];
					if (loc1.charAt(0) == loc2.charAt(0)) {
						moveMap.set(i, loc1 + ":" + String.format("%c%d%s", move1.charAt(0), loc1.charAt(1), move1.substring(1)));
						moveMap.set(j, loc2 + ":" + String.format("%c%d%s", move2.charAt(0), loc2.charAt(1), move2.substring(1)));
					} else {
						moveMap.set(i, loc1 + ":" + String.format("%c%c%s", move1.charAt(0), loc1.charAt(0), move1.substring(1)));
						moveMap.set(j, loc2 + ":" + String.format("%c%c%s", move2.charAt(0), loc2.charAt(0), move2.substring(1)));
					}
				} 
			}
		}
	}


	/**
	 * Fills up board with new Pieces in the starting positions
	 */
	public void reset(){
		for (int r = 0; r < 8; r++) {
			for (int f = 0; f < 8; f++) {
				board[r][f] = new Blank(Piece.BLANK, r+1, f+1);
			}
		}
		
		// rooks
		Rook leftBlackR = new Rook(Piece.BLACK, 8, 1);
		Rook rightBlackR = new Rook(Piece.BLACK, 8, 8);
		Rook leftWhiteR = new Rook(Piece.WHITE, 1, 1);
		Rook rightWhiteR = new Rook(Piece.WHITE, 1, 8);
		
		placePiece(leftBlackR);
		placePiece(rightBlackR);
		placePiece(leftWhiteR);
		placePiece(rightWhiteR);

		// knights
		Knight leftBlackN = new Knight(Piece.BLACK, 8, 2);
		Knight rightBlackN = new Knight(Piece.BLACK, 8, 7);
		Knight leftWhiteN = new Knight(Piece.WHITE, 1, 2);
		Knight rightWhiteN = new Knight(Piece.WHITE, 1, 7);
		
		placePiece(leftBlackN);
		placePiece(rightBlackN);
		placePiece(leftWhiteN);
		placePiece(rightWhiteN);
		
		// bishops
		Bishop leftBlackB = new Bishop(Piece.BLACK, 8, 3);
		Bishop rightBlackB = new Bishop(Piece.BLACK, 8, 6);
		Bishop leftWhiteB = new Bishop(Piece.WHITE, 1, 3);
		Bishop rightWhiteB = new Bishop(Piece.WHITE, 1, 6);
		
		placePiece(leftBlackB);
		placePiece(rightBlackB);
		placePiece(leftWhiteB);
		placePiece(rightWhiteB);
		
		//queens
		Queen blackQueen = new Queen(Piece.BLACK, 8, 4);
		Queen whiteQueen = new Queen(Piece.WHITE, 1, 4);
		
		placePiece(blackQueen);
		placePiece(whiteQueen);
		
		// kings
		blackKing = new King(Piece.BLACK, 8, 5);
		whiteKing = new King(Piece.WHITE, 1, 5);
		
		placePiece(blackKing);
		placePiece(whiteKing);
		
		// pawns
		for (int f = 1; f <= 8; f++) {
			Pawn blackPawn = new Pawn(Piece.BLACK, 7, f);
			Pawn whitePawn = new Pawn(Piece.WHITE, 2, f);
			pawns.add(blackPawn);
			pawns.add(whitePawn);
			board[6][f-1] = blackPawn;
			board[1][f-1] = whitePawn;
		}
	}

	public void temp_board() {
		for (int r = 0; r < 8; r++) {
			for (int f = 0; f < 8; f++) {
				board[r][f] = new Blank(Piece.BLANK, r+1, f+1);
			}
		}
		// USED FOR DEBUGGING MOVES
		whiteKing = new King(Piece.WHITE, 1, 5);
		blackKing = new King(Piece.BLACK, 5, 3);
		Queen wq = new Queen(Piece.WHITE, 2, 8);
		Queen bq = new Queen(Piece.BLACK, 7, 3);
		
		placePiece(whiteKing);
		placePiece(blackKing);
		placePiece(wq);
		placePiece(bq);

		Pawn testPawn = new Pawn(Piece.WHITE, 5, 7);
		placePiece(testPawn);
		Pawn testPawn2 = new Pawn(Piece.BLACK, 7, 6);
		placePiece(testPawn2);
	}

	public Piece get(int rank, int file) {
		return this.board[rank-1][file-1];
	}

	public Piece get(String loc) {
		int rank = loc.charAt(1)-'0';
		int file = loc.charAt(0)-'a'+1;
		return get(rank, file);
	}

	public boolean hasCheck(boolean isWhite) {
		int ownColor = isWhite ? Piece.WHITE : Piece.BLACK;
		for (Piece p : pawns) {
			if (p.getColor() == ownColor && p.canCheck(this)) {
				return true;
			} 
		}
		for (Piece r : rooks) {
			if (r.getColor() == ownColor && r.canCheck(this))
				return true; 
		}
		for (Piece n : knights) {
			if (n.getColor() == ownColor && n.canCheck(this))
				return true; 
		}
		for (Piece b : bishops) {
			if (b.getColor() == ownColor && b.canCheck(this))
				return true; 
		}
		for (Piece q : queens) {
			if (q.getColor() == ownColor && q.canCheck(this))
				return true; 
		}

		if (isWhite && whiteKing.canCheck(this))
			return true;
		else if (!isWhite && blackKing.canCheck(this))
			return true;

		return false;
	}
	
	public boolean hasCheckmate(boolean isWhite) {
		// TODO
        return false;
    }

	public boolean isInBounds(int rank, int file) {
		return rank >= 1 && rank <= 8 && file >= 1 && file <= 8;
	}

	public boolean isEmpty(int rank, int file) {
		return isInBounds(rank, file) && board[rank-1][file-1].getClass() == Blank.class;
	}

	public Piece move(String loc, String move) {
        System.out.println("move is  " + move);

		// Castling
		if (move.equals("0-0")) {
			return kingsideCastleMove(loc); 	
		} else if (move.equals("0-0-0")) {
			return queensideCastleMove(loc);
		}

		int r1 = loc.charAt(1)-'0';
		int f1 = loc.charAt(0)-'a'+1;
		Piece piece = this.get(r1, f1);

		int r2, f2;
		boolean isCapture = false;
		if (move.charAt(0) < 'a') {
			move = move.substring(1);
		}
		if (move.charAt(0) >= 'a' && move.charAt(0) <= 'h' && move.charAt(1) >= 'a' && move.charAt(1) <= 'h') {
			move = move.substring(1);
		}
		if (move.charAt(0) >= '1' && move.charAt(0) <= '8' && move.charAt(1) >= 'a' && move.charAt(1) <= 'h') {
			move = move.substring(1);
		}
		if (move.charAt(0) == 'x') {
			isCapture = true;
			move = move.substring(1);
		}
		if (move.charAt(1) == 'x') {
			isCapture = true;
			move = move.substring(2);
		}
		r2 = move.charAt(1)-'0';
		f2 = move.charAt(0)-'a'+1;

		return move(piece, r2, f2, isCapture);
	}

	private Piece move(Piece piece, int toRank, int toFile, boolean isCapture) {
		Piece capturedPiece = isCapture ? board[toRank-1][toFile-1] : null;
		System.out.println("is capure? " + isCapture);
		resetPassantSquare();

		int fromRank = piece.getRank();
		int fromFile = piece.getFile();

		board[fromRank-1][fromFile-1] = new Blank(Piece.BLANK, fromRank, fromFile);
		board[toRank-1][toFile-1] = piece;
		piece.setRank(toRank);
		piece.setFile(toFile);	

		if(piece.getKind()==Piece.PAWN){
			// Pawn promotion check
			if(toRank==1||toRank==8){
				pawnPromotionMove(piece, toRank, toFile);
			}
			// 2 square move -> set up passantSquare
			if(Math.abs(fromRank-toRank)==2){
				if(piece.getColor()==Piece.WHITE){
					passantSquare = board[2][fromFile-1];
					passantSquare.setPassant(true);
				}
				else{
					passantSquare = board[5][fromFile-1];
					passantSquare.setPassant(true);
				}
			}
			// check if the pawn is doing an En Passant capture
			if((isCapture && capturedPiece.isBlank()) || (toFile!=fromFile && board[fromRank-1][fromFile-1].isBlank())){
				if(piece.getColor()==Piece.WHITE){
					capturedPiece = board[4][toFile-1];
					board[4][toFile-1] = new Blank(Piece.BLANK, 2, toFile-1);
				}
				else{
					capturedPiece = board[3][toFile-1];
					board[3][toFile-1] = new Blank(Piece.BLANK, 4, toFile-1);
				}
			}

		}

		return capturedPiece;
	}

	/**
	 * Gets rid of the marking for a passant square
	 * Sets this Board's passantSquare to null.
	 * If there is a current passantSquare, sets that Piece's
	 * isPassant to false.
	 */
	private void resetPassantSquare(){
		if(passantSquare!=null)
			passantSquare.setPassant(false);
		passantSquare = null;
	}

	private Piece kingsideCastleMove(String loc){
		Piece rook, king;
			// White King
			if (loc.equals("d1")) {
				rook = get(1, 1);
				king = get(1, 4);
				move(rook, 1, 3, false);
				move(king, 1, 2, false);
			} else if (loc.equals("d8")) {
				rook = get(8, 1);
				king = get(8, 4);
				move(rook, 8, 3, false);
				move(king, 8, 2, false);
			} else {
				System.out.println("lol wat?");
				System.exit(300);
			}
			return null; 	
	}

	private Piece queensideCastleMove(String loc){
		Piece rook, king;
			// White King
			if (loc.equals("d1")) {
				rook = get(1, 8);
				king = get(1, 4);
				move(rook, 1, 5, false);
				move(king, 1, 6, false);
			} else if (loc.equals("d8")) {
				rook = get(8, 8);
				king = get(8, 4);
				move(rook, 8, 5, false);
				move(king, 8, 6, false);
			} else {
				System.out.println("lol wat?");
				System.exit(300);
			}
			return null;
	}

	/**
	 * Places a newly constructed Queen of the same color at the indicated
	 * location on this Board, and removes the old piece
	 */
	private Piece pawnPromotionMove(Piece piece, int toRank, int toFile){
		Queen newQueen = new Queen(piece.getColor(), toRank, toFile);
		placePiece(newQueen);
		removePiece(piece);
		return null;
	}

	public void placePiece(Piece piece) {
		int rank, file;
		switch (piece.getKind()) {
			case 0:
				pawns.add(piece);
				break;
			case 'R':
				rooks.add(piece);
				break;
			case 'N':
				knights.add(piece);
				break;
			case 'B':
				bishops.add(piece);
				break;
			case 'Q':
				queens.add(piece);
				break;
		}
		
		rank = piece.getRank();
		file = piece.getFile();
		board[rank-1][file-1] = piece;
	}

	public void removePiece(Piece piece) {
		switch (piece.getKind()) {
			case 0:
				pawns.remove(piece);
				break;
			case 'R':
				rooks.remove(piece);
				break;
			case 'N':
				knights.remove(piece);
				break;
			case 'B':
				bishops.remove(piece);
				break;
			case 'Q':
				queens.remove(piece);
				break;
			case 'K':
				System.out.println("lol wat?");
				System.exit(200);
		}
	}

	@Override
	public String toString() {
		final String HEADER = "     a    b    c    d    e    f    g    h\n";
		final String FOOTER = HEADER;
		final String BORDER = "   +----+----+----+----+----+----+----+----+\n";
		
		StringBuilder out = new StringBuilder();

		out.append(HEADER);
		
		for (int r = 7; r >= 0; r--) {
			out.append(BORDER);

			out.append(r+1);
			out.append("  |");
			for (int f = 0; f < 8; f++) {
				out.append(' ');
				if (board[r][f].isBlank()) {
					out.append("   ");
				} else {
					out.append(board[r][f].toString() + ' ');
				}
				out.append("|");
			}
			out.append('\n');
		}

		out.append(BORDER);
		out.append(FOOTER);
		return out.toString();
	}
}
