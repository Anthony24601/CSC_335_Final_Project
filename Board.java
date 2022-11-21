




public class Board {
	Piece[][] board;

	public Board() {
		board = new Piece[8][8];
		board[0][0] = new Rook("black","rook");
		board[1][0] = new Rook("black","rook");
		board[0][1] = new Rook("white","rook");
		board[1][1] = new Rook("white","rook");
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
