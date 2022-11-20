
public abstract class Piece {
	// colors
	public final static int BLANK = 0;
	public final static int WHITE = 10;
    public final static int BLACK = 20;

	protected int color;
	
	public Piece(int color) {
		this.color = color;
	}
	
	/**
	 * Check if a move is valid. This check if it follows this Piece's
	 * move rules and if it conflicts with the other Pieces on the
	 * Board
	 * @param fromRow the starting row 
	 * @param fromCol the starting column
	 * @param toRow the row the Piece wants to move to
	 * @param toCol the column the Piece wants to move to
	 * @return boolean, true if the move is valid
	 */
	abstract public boolean isValidMove(int fromRow, int fromCol, int toRow, int toCol);

	/**
	 * Returns the path to the image for this Piece. The row and col is
	 * needed to check if the image is for a dark square or a light
	 * square.
	 * @param row row this Piece is at
	 * @param col column this Piece is at
	 * @return
	 */
	abstract public String getPicture(int row, int col);

	/**
	 * Returns this Piece's color
	 * @return Piece.BLACK or Piece.WHITE
	 */
	abstract public int getColor();

	/**
	 * @return true if this is a blank piece, false if its a different type
	 */
	abstract public boolean isBlank();

	/**
	 * Checks if a coordinate is within the board's boundries
	 * @param row
	 * @param col
	 * @return
	 */
	protected static boolean isInBounds(int row, int col) {
        return (row >= 0 && row <= Board.MAX_ROW && col >= 0 && col <= Board.MAX_COLUMN);
    }
}
