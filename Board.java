public class Board {
	final static int RANKS = 8;
    final static int FILES = 8;

    public final static char MAX_RANK = '1' + RANKS - 1;
    public final static char MAX_FILE = 'a' + FILES - 1;

	public final static int MAX_ROW = RANKS - 1;
	public final static int MAX_COLUMN = FILES - 1;

	private Piece[][] board;
	private King[] kings;

	public Board() {
		board = new Piece[8][8];
		reset();
	} 

	/**
	 * Attemps a move on the chess board.
	 * @param fromRow
	 * @param fromCol
	 * @param toRow
	 * @param toCol
	 * @return true if the move was made, false if the move was not
	 * 		made (invalid move)
	 */
	public boolean attemptMove(int fromRow, int fromCol, int toRow, int toCol){
		// change arguement to a String of chess notation??
		Piece selectedPiece = board[fromRow][fromCol];
		if(selectedPiece.isValidMove(fromRow, fromCol, toRow, toCol)){
			// place selected piece in new postion and a blank in the old position
			board[fromRow][fromCol] = new Blank(Piece.BLANK);
			board[toRow][toCol] = selectedPiece;
		}
		else{
			return false;
		}
		return true;
	}

	/**
	 * Fills up board with new Pieces in the starting positions
	 */
	public void reset(){
		// fill with blanks
		for (int row = 0; row < 8; row++) {
			for (int col = 0; col < 8; col++) {
				board[row][col] = new Blank(Piece.BLANK);
			}
		}
		// rooks
		board[0][0] = new Rook(Piece.BLACK, this);
		board[0][7] = new Rook(Piece.BLACK, this);
		board[7][0] = new Rook(Piece.WHITE, this);
		board[7][7] = new Rook(Piece.WHITE, this);
		// knights
		board[0][1] = new Knight(Piece.BLACK, this);
		board[0][6] = new Knight(Piece.BLACK, this);
		board[7][1] = new Knight(Piece.WHITE, this);
		board[7][6] = new Knight(Piece.WHITE, this);
		// bishops
		board[0][2] = new Bishop(Piece.BLACK, this);
		board[0][5] = new Bishop(Piece.BLACK, this);
		board[7][2] = new Bishop(Piece.WHITE, this);
		board[7][5] = new Bishop(Piece.WHITE, this);
		//queens
		board[0][3] = new Queen(Piece.BLACK, this);
		board[7][3] = new Queen(Piece.WHITE, this);
		// kings
		board[0][4] = new King(Piece.BLACK, this);
		board[7][4] = new King(Piece.WHITE, this);
		// pawns
		for(int c=0; c<8; c++){
			board[1][c] = new Pawn(Piece.BLACK, this);
			board[6][c] = new Pawn(Piece.WHITE, this);
		}
	}

	/**
	 * Returns the Piece at the indicated position
	 * @param row int 0-7 O is top (rank 8) 7 is bottom (rank 1)
	 * @param col int 0-7 0 is left (file a) 7 is right (file h)
	 * @return
	 */
	public Piece get(int row, int col) {
		return board[row][col];
	}

	/**
	 * Returns true if there is a check mate occuring on the board
	 * @return
	 */
	public boolean hasCheckmate() {
		// TODO
        return false;
    }
}
