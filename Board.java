
public class Board {
	Piece[][] board;
	
	public Board() {
		board = new Piece[8][8];
		board[0][0] = new Piece("black", "rook");
		board[1][0] = new Piece("black", "knight");
		board[2][0] = new Piece("black", "bishop");
		board[3][0] = new Piece("black", "queen");
		board[4][0] = new Piece("black", "king");
		board[5][0] = new Piece("black", "bishop");
		board[6][0] = new Piece("black", "knight");
		board[7][0] = new Piece("black", "rook");
		
		board[0][1] = new Piece("black", "pawn");
		board[1][1] = new Piece("black", "pawn");
		board[2][1] = new Piece("black", "pawn");
		board[3][1] = new Piece("black", "pawn");
		board[4][1] = new Piece("black", "pawn");
		board[5][1] = new Piece("black", "pawn");
		board[6][1] = new Piece("black", "pawn");
		board[7][1] = new Piece("black", "pawn");
		
		board[0][7] = new Piece("white", "rook");
		board[1][7] = new Piece("white", "knight");
		board[2][7] = new Piece("white", "bishop");
		board[3][7] = new Piece("white", "queen");
		board[4][7] = new Piece("white", "king");
		board[5][7] = new Piece("white", "bishop");
		board[6][7] = new Piece("white", "knight");
		board[7][7] = new Piece("white", "rook");
		
		board[0][6] = new Piece("white", "pawn");
		board[1][6] = new Piece("white", "pawn");
		board[2][6] = new Piece("white", "pawn");
		board[3][6] = new Piece("white", "pawn");
		board[4][6] = new Piece("white", "pawn");
		board[5][6] = new Piece("white", "pawn");
		board[6][6] = new Piece("white", "pawn");
		board[7][6] = new Piece("white", "pawn");
		
		for (int row = 0; row < 8; row++) {
			for (int col = 0; col < 8; col++) {
				if (board[row][col] == null) {
					board[row][col] = new Blank("blank","blank");
				}
			}
		}
	}
	
	public Piece get(int row, int col) {
		return board[row][col];
	}
}
