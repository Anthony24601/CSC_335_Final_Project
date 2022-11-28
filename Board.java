import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;

public class Board {
	final static int RANKS = 8;
    final static int FILES = 8;

    public final static char MAX_RANK = '1' + RANKS - 1;
    public final static char MAX_FILE = 'a' + FILES - 1;

	public final static int MAX_ROW = RANKS - 1;
	public final static int MAX_COLUMN = FILES - 1;

	private Piece blackKing;
	private Piece whiteKing;
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
			reset();
			//temp_board();
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

	public Map<String, String[]> getMoves(boolean isWhite) {
		Map<String, String[]> moveMap = new HashMap<>();
		String loc;
		int color = isWhite ? Piece.WHITE : Piece.BLACK;

		for (int r = 0; r < 8; r++) {
			for (int f = 0; f < 8; f++) {
				if (board[r][f] != null && board[r][f].getColor() == color) {
					loc = String.format("%c%d", f + 'a', r+1);
					moveMap.put(loc, board[r][f].getValidMoves(this));
				}
			}
		}

		return moveMap;
	}

	public Map<String, String[]> getMoves(char kind, boolean isWhite) {
		ArrayList<Piece> pieces = new ArrayList<>();
		int color = isWhite ? Piece.WHITE : Piece.BLACK;

		switch (kind) {
			case 0: pieces = pawns; break;
			case 'K': pieces.add(blackKing); pieces.add(whiteKing); break;
			case 'Q': pieces = queens; break;
			case 'B': pieces = bishops; break;
			case 'N': pieces = knights; break;
			case 'R': pieces = rooks; break;
		}

		Map<String, String[]> moveMap = new HashMap<>();
		for (Piece p : pieces) {
			if (p.getColor() == color) {
				moveMap.put(p.getLoc(), p.getValidMoves(this));
			}
		}
		return moveMap;
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
		whiteKing = new King(Piece.WHITE, 1, 4);
		blackKing = new King(Piece.BLACK, 8, 4);
		Rook bkr = new Rook(Piece.BLACK, 8, 1);
		Rook bqr = new Rook(Piece.BLACK, 8, 8);
		
		placePiece(whiteKing);
		placePiece(blackKing);
		placePiece(bkr);
		placePiece(bqr);
	}

	public Piece get(int rank, int file) {
		return this.board[rank-1][file-1];
	}

	public boolean hasCheck(boolean isWhite) {
		for (Piece p : pawns) {
			if (p.canCheck(this))
				return true; 
		}
		for (Piece r : rooks) {
			if (r.canCheck(this))
				return true; 
		}
		for (Piece n : knights) {
			if (n.canCheck(this))
				return true; 
		}
		for (Piece b : bishops) {
			if (b.canCheck(this))
				return true; 
		}
		for (Piece q : queens) {
			if (q.canCheck(this))
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

		// Pawn promotion
		if(move.contains("=")){
			return pawnPromotionMove(piece, r2, f2);
		}

		return move(piece, r2, f2, isCapture);
	}

	private Piece move(Piece piece, int toRank, int toFile, boolean isCapture) {
		Piece capturedPiece = isCapture ? board[toRank-1][toFile-1] : null;

		int fromRank = piece.getRank();
		int fromFile = piece.getFile();

		board[fromRank-1][fromFile-1] = new Blank(Piece.BLANK, fromRank, fromFile);
		board[toRank-1][toFile-1] = piece;
		piece.setRank(toRank);
		piece.setFile(toFile);		

		return capturedPiece;
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
