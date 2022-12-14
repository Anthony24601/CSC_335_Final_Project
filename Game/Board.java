package Game;
/**
File: ChessUI.java
Author: Chris Macholtz
Course: CSC 335
Purpose: Contains all information regarding the board's state. 
Can answer simple questions based on the piece layout, but does not make any game logic decisions 
		 
*/

import java.util.ArrayList;

import PiecePackage.Bishop;
import PiecePackage.Blank;
import PiecePackage.King;
import PiecePackage.Knight;
import PiecePackage.Pawn;
import PiecePackage.Piece;
import PiecePackage.Queen;
import PiecePackage.Rook;

import java.io.Serializable;

public class Board implements Serializable {
	private static final long serialVersionUID = 1L;

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

    private boolean whiteKingRookHasMoved = false;
    private boolean whiteQueenRookHasMoved = false;
    private boolean blackKingRookHasMoved = false;
    private boolean blackQueenRookHasMoved = false;
    private boolean whiteKingHasMoved = false;
    private boolean blackKingHasMoved = false;
	
	private Piece[][] board;

	private ArrayList<String> moveHistory;

	/**
	 * Constructor
	 * @param isBlank	Whether the board is pre-filled with pieces or not
	 */
	public Board(boolean isBlank) {
		board = new Piece[8][8];
		queens = new ArrayList<>();
		bishops = new ArrayList<>();
		knights = new ArrayList<>();
		rooks = new ArrayList<>();
		pawns = new ArrayList<>();
		moveHistory = new ArrayList<>();

		if (isBlank) {
			for (int r = 0; r < RANKS; r++) {
				for (int f = 0; f < FILES; f++) {
					board[r][f] = new Blank(Piece.BLANK, r, f);
				}
			}
		} else {
			reset();
		}
	}

	/**
	 * Returns a copy of a board, including its current state
	 * @return	A copy of the Board object
	 */
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

		newBoard.whiteKingHasMoved = whiteKingHasMoved;
		newBoard.whiteKingRookHasMoved = whiteKingRookHasMoved;
		newBoard.whiteQueenRookHasMoved = whiteQueenRookHasMoved;
		newBoard.blackKingHasMoved = blackKingHasMoved;
		newBoard.blackKingRookHasMoved = blackKingRookHasMoved;
		newBoard.blackQueenRookHasMoved = blackQueenRookHasMoved;

		return newBoard;
	}

	/**
	 * Getter for whether the white king has moved. 
	 * Used for determining if castling is possible
	 * @return	Whether the white king has moved
	 */
	public boolean getWhiteKingHasMoved() {
		return whiteKingHasMoved;
	}

	/**
	 * Getter for whether the white king's rook has moved
	 * Used for determining if castling is possible
	 * @return	Whether the white king's rook has moved
	 */
	public boolean getWhiteKingRookHasMoved() {
		return whiteKingRookHasMoved;
	}

	/**
	 * Getter for whether the white queens's rook has moved
	 * Used for determining if castling is possible
	 * @return	Whether the white queens's rook has moved
	 */
	public boolean getWhiteQueenRookHasMoved() {
		return whiteQueenRookHasMoved;
	}

	/**
	 * Getter for whether the black king has moved. 
	 * Used for determining if castling is possible
	 * @return	Whether the black king has moved
	 */
	public boolean getBlackKingHasMoved() {
		return blackKingHasMoved;
	}

	/**
	 * Getter for whether the black king's rook has moved
	 * Used for determining if castling is possible
	 * @return	Whether the black king's rook has moved
	 */
	public boolean getBlackKingRookHasMoved() {
		return blackKingRookHasMoved;
	}

	/**
	 * Getter for whether the black queens's rook has moved
	 * Used for determining if castling is possible
	 * @return	Whether the black queens's rook has moved
	 */
	public boolean getBlackQueenRookHasMoved() {
		return blackQueenRookHasMoved;
	}

	/**
	 * Polls all of a given color's piece's moves. Necessary since algebraic notation 
	 * does not specific WHICH of each piece's kind is being used
	 * @param kind			The kind / type of piece
	 * @param isWhite		Whether the piece is white
	 * @param gameModel		Handle for the GameModel (needed to ask questions on piece level)
	 * @return				An ArrayList<String> of all moves, formatted as <location>:<move>
	 */	
	public ArrayList<String> getMoves(char kind, boolean isWhite, GameModel gameModel) {
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
				String[] validMoves = p.getValidMoves(this, gameModel);
				for (String vm : validMoves) {
					moveMap.add(p.getLoc() + ":" + vm);
				}
			}
		}

		adjustSameSpace(moveMap);
		return moveMap;
	}

	/**
	 * Modifies the algebraic move notation in case the rank or file need to be included to disambiguate which piece is moving
	 * @param moveMap	An updated version of the ArrayList of moves
	 */
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
						moveMap.set(i, loc1 + ":" + String.format("%c%c%s", move1.charAt(0), loc1.charAt(1), move1.substring(1)));
						moveMap.set(j, loc2 + ":" + String.format("%c%c%s", move2.charAt(0), loc2.charAt(1), move2.substring(1)));
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
		moveHistory.clear();

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

	/**
	 * Getter for a piece on the board
	 * @param rank	The piece's rank
	 * @param file	The piece's file
	 * @return		The piece
	 */
	public Piece get(int rank, int file) {
		return this.board[rank-1][file-1];
	}

	/**
	 * Getter for a piece on the board, accepting a location string such as "e5"
	 * @param loc	A location string
	 * @return		The piece
	 */
	public Piece get(String loc) {
		int rank = loc.charAt(1)-'0';
		int file = loc.charAt(0)-'a'+1;
		return get(rank, file);
	}

	/**
	 * Polls all of the pieces on the board to see if the opposing king is in check
	 * @param isWhite	Whether checking for white's side
	 * @return			Whether the opposing king is in check
	 */
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

	/**
	 * Checks if the side has successfully obtained a checkmate
	 * @param isWhite		Whether the side is white
	 * @param gameModel		GameModel handle, used for making piece movement decisions
	 * @return				Whether checkmate has been done
	 */
	public boolean hasCheckmate(boolean isWhite, GameModel gameModel) {
		int ownColor = isWhite ? Piece.WHITE : Piece.BLACK;
		String[] moves;
		for (Piece p : pawns) {
			if (p.getColor() == ownColor) {
				moves = p.getValidMoves(this, gameModel);
				if (moves.length > 0) {
					return false;
				}
			}
		}
		for (Piece r : rooks) {
			if (r.getColor() == ownColor) {
				moves = r.getValidMoves(this, gameModel);
				if (moves.length > 0) {
					return false;
				}
			}
		}
		for (Piece n : knights) {
			if (n.getColor() == ownColor) {
				moves = n.getValidMoves(this, gameModel);
				if (moves.length > 0) {
					return false;
				}
			}
		}
		for (Piece b : bishops) {
			if (b.getColor() == ownColor) {
				moves = b.getValidMoves(this, gameModel);
				if (moves.length > 0) {
					return false;
				}
			}
		}
		for (Piece q : queens) {
			if (q.getColor() == ownColor) {
				moves = q.getValidMoves(this, gameModel);
				if (moves.length > 0) {
					return false;
				}
			}
		}
		if (ownColor == Piece.WHITE) {
			moves = whiteKing.getValidMoves(this, gameModel);
			if (moves.length > 0) {
				return false;
			}
		} else {
			moves = blackKing.getValidMoves(this, gameModel);
			if (moves.length > 0) {
				return false;
			}
		}

        return true;
    }

	/**
	 * Returns whether a given rank and file are inside the board
	 * @param rank	Rank
	 * @param file	File
	 * @return		Whether it is inside the board
	 */
	public boolean isInBounds(int rank, int file) {
		return rank >= 1 && rank <= 8 && file >= 1 && file <= 8;
	}

	/**
	 * Returns whether a space is empty
	 * @param rank	Rank
	 * @param file	File
	 * @return		Whether the space is empty
	 */
	public boolean isEmpty(int rank, int file) {
		return isInBounds(rank, file) && board[rank-1][file-1].getClass() == Blank.class;
	}

	/**
	 * Saves a given move before going through the move logic
	 * @param loc	Location of the moving piece
	 * @param move	Move for the piece
	 * @return		The captured piece, if it is a capture. Otherwise, null
	 */
	public Piece moveAndSave(String loc, String move) {
		moveHistory.add(move);
		return move(loc, move);
	}

	/**
	 * Converts a given algebraic move into something actionable for board
	 * @param loc	Location of the moving piece
	 * @param move	Move for the piece
	 * @return		The captured piece, if it is a capture. Otherwise, null
	 */
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

	/**
	 * Moves the piece
	 * @param piece			The moving piece
	 * @param toRank		Destination rank
	 * @param toFile		Destination file
	 * @param isCapture		Whether a piece is being captured
	 * @return				The captured piece. Otherwise, null
	 */
	private Piece move(Piece piece, int toRank, int toFile, boolean isCapture) {
		Piece capturedPiece = isCapture ? board[toRank-1][toFile-1] : null;
		resetPassantSquare();

		int fromRank = piece.getRank();
		int fromFile = piece.getFile();

		board[fromRank-1][fromFile-1] = new Blank(Piece.BLANK, fromRank, fromFile);

		if(piece.getKind()==Piece.PAWN){
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
			if((isCapture && capturedPiece.isBlank()) || (toFile!=fromFile && board[toRank-1][toFile-1].isBlank())){
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
		
		if (capturedPiece != null && !capturedPiece.isBlank()) {
			removePiece(capturedPiece);
		}
		addHasMoved(piece, capturedPiece);
		
		board[toRank-1][toFile-1] = piece;
		piece.setRank(toRank);
		piece.setFile(toFile);

		if (piece.getKind() == Piece.PAWN) {
			if (piece.getColor() == Piece.WHITE && piece.getRank() == 8) {
				pawnPromotionMove(piece);
			}
			else if (piece.getColor() == Piece.BLACK && piece.getRank() == 1) {
				pawnPromotionMove(piece);
			}
		}

		return capturedPiece;
	}

	/**
	 * Used for tracking if castling is still possible
	 * @param movingPiece		The moving piece
	 * @param capturedPiece		Used in case a rook has been captured
	 */
	private void addHasMoved(Piece movingPiece, Piece capturedPiece) {
		if (movingPiece.getKind() == 'R') {
			if (movingPiece.getColor() == Piece.WHITE) {
				if (movingPiece.getRank() == 1 && movingPiece.getFile() == 8) {
					whiteKingRookHasMoved = true;
				}
				else if (movingPiece.getRank() == 1 && movingPiece.getFile() == 1) {
					whiteQueenRookHasMoved = true;
				}
			} else {
				if (movingPiece.getRank() == 8 && movingPiece.getFile() == 8) {
					blackKingRookHasMoved = true;
				}
				else if (movingPiece.getRank() == 8 && movingPiece.getFile() == 1) {
					blackQueenRookHasMoved = true;
				}
			}
		}

		else if (movingPiece.getKind() == 'K') {
			if (movingPiece.getColor() == Piece.WHITE) {
				whiteKingHasMoved = true;
			} else {
				blackKingHasMoved = true;
			}
		}

		else if (capturedPiece != null && capturedPiece.getKind() == 'R') {
			if (capturedPiece.getColor() == Piece.WHITE) {
				if (movingPiece.getRank() == 1 && movingPiece.getFile() == 8) {
					whiteKingRookHasMoved = true;
				}
				else if (movingPiece.getRank() == 1 && movingPiece.getFile() == 1) {
					whiteQueenRookHasMoved = true;
				}
			} else {
				if (movingPiece.getRank() == 8 && movingPiece.getFile() == 8) {
					blackKingRookHasMoved = true;
				}
				else if (movingPiece.getRank() == 8 && movingPiece.getFile() == 1) {
					blackQueenRookHasMoved = true;
				}
			}
		} 	
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

	/**
	 * Invokes a castle kingside
	 * @param loc	Location of the king
	 * @return		null
	 */
	private Piece kingsideCastleMove(String loc){
		Piece rook, king;
		// White King
		if (loc.equals("e1")) {
			rook = get(1, 8);
			king = get(1, 5);
			move(rook, 1, 6, false);
			move(king, 1, 7, false);
			whiteKingHasMoved = true;
			whiteKingRookHasMoved = true;
		} else if (loc.equals("e8")) {
			rook = get(8, 8);
			king = get(8, 5);
			move(rook, 8, 6, false);
			move(king, 8, 7, false);
			blackKingHasMoved = true;
			blackKingRookHasMoved = true;
		} else {
			System.out.println("KingsideCastleMove: lol wat?");
			System.exit(300);
		}
		return null;
	}

	/**
	 * Invokes a castle queenside
	 * @param loc	Location of the king
	 * @return		null
	 */
	private Piece queensideCastleMove(String loc){
		Piece rook, king;
			// White King
			if (loc.equals("e1")) {
				rook = get(1, 1);
				king = get(1, 5);
				move(rook, 1, 4, false);
				move(king, 1, 3, false);
				whiteKingHasMoved = true;
				whiteQueenRookHasMoved = true;
			} else if (loc.equals("e8")) {
				rook = get(8, 1);
				king = get(8, 5);
				move(rook, 8, 4, false);
				move(king, 8, 3, false);
				blackKingHasMoved = true;
				blackQueenRookHasMoved = true;
			} else {
				System.out.println("Queenside Castle Move: lol wat?");
				System.exit(300);
			}
			return null;
	}

	/**
	 * Places a newly constructed Queen of the same color at the indicated
	 * location on this Board, and removes the old piece
	 */
	private void pawnPromotionMove(Piece piece) {
		pawns.remove(piece);
		Queen newQueen = new Queen(piece.getColor(), piece.getRank(), piece.getFile());
		removePiece(piece);
		placePiece(newQueen);
	}

	/**
	 * Places a given piece on the board and sets the piece's own rank and file info
	 * @param piece		Piece to be placed
	 */
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

	/**
	 * Removes a piece from the board and from its ArrayList
	 * @param piece		Piece to be removed
	 */
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
				System.out.println("Remove Piece: lol wat?");
				System.exit(200);
		}
	}

	/**
	 * Returns the number of pieces a given side has. Used for determining a draw
	 * @return	An array of the number of pieces a side has
	 */
	public int[] getNumColors() {
		int[] count = new int[] {0,0};
		for (int rank = 1; rank <= 8; rank++) {
			for (int file = 1; file <= 8; file++) {
				if (get(rank,file).getColor() == Piece.WHITE) {
					count[0] += 1;
				} else if (get(rank,file).getColor() == Piece.BLACK){
					count[1] += 1;
				}
			}
		}
		return count;
	}
	
	/**
	 * Getter for the king
	 * @param color		The color of the king
	 * @return			The king
	 */
	public Piece getKing(int color) {
		if (color == Piece.WHITE) {
			return whiteKing;
		}
		return blackKing;
	}
	
	/**
	 * Checks if only one bishop remains. Used for determining a draw
	 * @return	Whether one bishop remains
	 */
	public boolean checkOneBishop() {
		if (bishops.size() == 1) {
			return true;
		}
		return false;
	}
	
	/**
	 * Checks if only one knight remains. Used for determining a draw
	 * @return	Whether one knight remains
	 */
	public boolean checkOneKnight() {
		if (knights.size() == 1) {
			return true;
		}
		return false;
	}
	
	/**
	 * Checks for count of bishops. Used for determining a draw
	 * @return	
	 */
	public boolean checkBishops() {
		if (bishops.size() != 2) {
			return false;
		}
		//Assumed to be different color bishops
		Piece b1 = bishops.get(0);
		Piece b2 = bishops.get(1);
		String color1 = b1.getPicture(b1.getRank(), b1.getFile()).split("/")[1];
		String color2 = b2.getPicture(b2.getRank(), b2.getFile()).split("/")[1];
		if (color1.equals(color2)) {
			return true;
		}
		return false;
	}
	
	/**
	 * Used for using printing the board onto the terminal
	 * @return 		String version of the board
	 */
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

	/**
	 * Getter for the move history
	 * @return	The move history
	 */
    public ArrayList<String> getMoveHistory() {
        return moveHistory;
    }
}
