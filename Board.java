




public class Board {
	Piece[][] board;

	public Board() {
		board = new Piece[8][8];
		//board[0][0] = new Piece("black","rook");
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
