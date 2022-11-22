package src;
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

	private ArrayList<Piece> kings;
	private ArrayList<Piece> queens;
	private ArrayList<Piece> bishops;
	private ArrayList<Piece> knights;
	private ArrayList<Piece> rooks;
	private ArrayList<Piece> pawns;

	private Piece[][] board;

	public Board() {
		board = new Piece[8][8];
		kings = new ArrayList<>();
		queens = new ArrayList<>();
		bishops = new ArrayList<>();
		knights = new ArrayList<>();
		rooks = new ArrayList<>();
		pawns = new ArrayList<>();
		reset();
		//temp_board();
	} 

	/**
	 * Attemps a move on the chess board.
	 * @param fromRank
	 * @param fromFile
	 * @param toRank
	 * @param toFile
	 * @return true if the move was made, false if the move was not
	 * 		made (invalid move)
	public boolean attemptMove(int fromRank, int fromFile, int toRank, int toFile){
		// change arguement to a String of chess notation??
		Piece selectedPiece = board[fromRank][fromFile];
		if(selectedPiece.isValidMove(fromRank, fromFile, toRank, toFile)){
			// place selected piece in new postion and a blank in the old position
			board[fromRank][fromFile] = new Blank(Piece.BLANK);
			board[toRank][toFile] = selectedPiece;
		}
		else{
			return false;
		}
		return true;
	}
	 */

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
			case 'K': pieces = kings; break;
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
		rooks.add(leftBlackR);
		rooks.add(rightBlackR);
		rooks.add(leftWhiteR);
		rooks.add(rightWhiteR);
		board[7][0] = leftBlackR;
		board[7][7] = rightBlackR;
		board[0][0] = leftWhiteR;
		board[0][7] = rightWhiteR;

		// knights
		Knight leftBlackN = new Knight(Piece.BLACK, 8, 2);
		Knight rightBlackN = new Knight(Piece.BLACK, 8, 7);
		Knight leftWhiteN = new Knight(Piece.WHITE, 1, 2);
		Knight rightWhiteN = new Knight(Piece.WHITE, 1, 7);
		knights.add(leftBlackN);
		knights.add(rightBlackN);
		knights.add(leftWhiteN);
		knights.add(rightWhiteN);
		board[7][1] = leftBlackN;
		board[7][6] = rightBlackN;
		board[0][1] = leftWhiteN;
		board[0][6] = rightWhiteN;
		
		// bishops
		Bishop leftBlackB = new Bishop(Piece.BLACK, 8, 3);
		Bishop rightBlackB = new Bishop(Piece.BLACK, 8, 6);
		Bishop leftWhiteB = new Bishop(Piece.WHITE, 1, 3);
		Bishop rightWhiteB = new Bishop(Piece.WHITE, 1, 6);
		bishops.add(leftBlackB);
		bishops.add(rightBlackB);
		bishops.add(leftWhiteB);
		bishops.add(rightWhiteB);
		board[7][2] = leftBlackB;
		board[7][5] = rightBlackB;
		board[0][2] = leftWhiteB;
		board[0][5] = rightWhiteB;
		
		//queens
		Queen blackQueen = new Queen(Piece.BLACK, 8, 4);
		Queen whiteQueen = new Queen(Piece.WHITE, 1, 4);
		queens.add(blackQueen);
		queens.add(whiteQueen);
		board[7][3] = blackQueen;
		board[0][3] = whiteQueen;
		
		// kings
		King blackKing = new King(Piece.BLACK, 8, 5);
		King whiteKing = new King(Piece.WHITE, 1, 5);
		kings.add(blackKing);
		kings.add(whiteKing);
		board[7][4] = blackKing;
		board[0][4] = whiteKing;
		
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
		Rook r = new Rook(Piece.WHITE, 2, 3);
		Queen q = new Queen(Piece.BLACK, 7, 7);
		rooks.add(r);
		queens.add(q);
		board[1][2] = r;
		board[6][6] = q;
	}

	public Piece get(int rank, int file) {
		return board[rank-1][file-1];
	}

	/**
	 * Returns true if there is a check mate occuring on the board
	 * @return
	 */
	public boolean hasCheckmate() {
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
		int r1 = loc.charAt(1)-'0';
		int f1 = loc.charAt(0)-'a'+1;
		Piece piece = get(r1, f1);

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
