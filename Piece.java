public abstract class Piece {
	// colors
	public final static int BLANK = 0;
	public final static int WHITE = 10;
    public final static int BLACK = 20;
	public final static char PAWN = 0;
	public final static char BISHOP = 'B';
	public final static char KNIGHT = 'N';
	public final static char ROOK = 'R';
	public final static char QUEEN = 'Q';
	public final static char KING = 'K';

	protected int color;
  protected String color_string;
	protected int rank;
	protected int file;
  protected String name;
	
	public Piece(int color, int rank, int file, String name) {
		this.color = color;
    if (color == BLANK){
      color_string = "blank";
    } else if (color == WHITE){
      color_string = "white";
    } else {
      color_string = "black";
    }
    this.name = name;
		this.rank = rank;
		this.file = file;
	}
	
	abstract public String[] getValidMoves(Board board);

	protected int getRank() {
		return rank;
	}

	protected int getFile() {
		return file;
	}

	protected void setRank(int rank) {
		this.rank = rank;
	}

	protected void setFile(int file) {
		this.file = file;
	}

	protected String getLoc() {
		return String.format("%c%d", file + 'a' - 1, rank);
	}

	protected void setLoc(String loc) {
		rank = loc.charAt(0) - 'a';
		file = Integer.parseInt(loc.substring(1, 2));
	}

	protected boolean equals(Piece other) {
		return color == other.color && rank == other.rank && file == other.file;
	}

	public String toString() {
		if (color == WHITE) {
			return "w" + getKind();
    } else {
			return "b" + getKind();
		}
	}


	public String getPicture(int row, int col) {
		if ((col%2 == 0 && row%2 == 0) || (col%2 == 1 && row%2 == 1)) {
			return "images/light/" + color_string + "/" + name + ".png";
    }
  }
		
  
	abstract public char getKind();



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
