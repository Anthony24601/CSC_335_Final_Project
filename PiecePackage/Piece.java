/**
 * File: Piece.java
 * Author: Grace Driskill
 * Course: CSC 335
 * Purpose: Abstract class that represents a single piece 
 * on a chess board.
 * Constructor
 * Piece(int color, int rank, int file, String name)
 */
package PiecePackage;
import java.io.Serializable;

import Game.Board;
import Game.GameModel;

public abstract class Piece implements Serializable {
	private static final long serialVersionUID = 1L;
	
	// colors
	public final static int BLANK = 0;
	public final static int WHITE = 10;
    public final static int BLACK = 20;
	// piece types
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
	
	/**
	 * Creates a new Piece
	 * @param color which color to make this Piece (Piece.WHITE or
	 * 	Piece.BLACK)
	 * @param rank rank of this Piece
	 * @param file file of this Piece
	 * @param name name of this Piece
	 */
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
	
	/**
	 * Returns all the possible valid moves this Piece can current 
	 * make
	 * @param board Board this Piece is on
	 * @param gameModel GameModel this Piece is apart of
	 * @return String[]
	 */
	abstract public String[] getValidMoves(Board board, GameModel gameModel);

	/**
	 * Returns if this Piece can make a check from its current 
	 * position
	 * @param board Board this Piece is on
	 * @return true if this Piece can check, false otherwise
	 */
	abstract public boolean canCheck(Board board);

	/**
	 * Creates a copy of itself
	 * @return new Piece with same info as this Piece
	 */
	abstract public Piece copy();

	/**
	 * Returns the rank of this Piece
	 */
	public int getRank() {
		return rank;
	}

	/**
	 * Returns the file of this Piece
	 */
	public int getFile() {
		return file;
	}

	/**
	 * Set the rank of this Piece to the specified
	 * value
	 * @param rank int
	 */
	public void setRank(int rank) {
		this.rank = rank;
	}

	/**
	 * Set the file of this Piece to the specified
	 * value
	 * @param file int
	 */
	public void setFile(int file) {
		this.file = file;
	}

	/**
	 * Returns a String representation of this Piece's 
	 * position
	 * @return String in format <file><rank>
	 */
	public String getLoc() {
		return String.format("%c%d", file + 'a' - 1, rank);
	}

	/**
	 * Sets this Piece's location (changes rank and file)
	 * @param loc String in format <file><rank>
	 */
	public void setLoc(String loc) {
		rank = loc.charAt(0) - 'a';
		file = Integer.parseInt(loc.substring(1, 2));
	}

	/**
	 * Checks this Piece is equal to other by comparing
	 * color, rank and file
	 * @param other Piece 
	 * @return true if this Piece is equal
	 */
	public boolean equals(Piece other) {
		return color == other.color && rank == other.rank && file == other.file;
	}

	/**
	 * Returns a String represention of this Piece. 
	 * Includes the Piece's color and kind
	 * Ex. white queen would return "wq"
	 */
	public String toString() {
		if (color == WHITE) {
			return "w" + getKind();
	    } else if (color == BLACK){
			return "b" + getKind();
		}
	    else {
	    	return "blank";
	    }
	}

	/**
	 * Returns the path to the image for this Piece. The row and col is
	 * needed to check if the image is for a dark square or a light
	 * square.
	 * @param row row this Piece is at
	 * @param col column this Piece is at
	 * @return
	 */
	public String getPicture(int row, int col) {
		if ((col%2 == 0 && row%2 == 0) || (col%2 == 1 && row%2 == 1)) {
			return "images/light/" + color_string + "/" + name + ".png";
		} else {
			return "images/dark/" + color_string + "/" + name + ".png";
		}
	}

	/**
	 * returns true if the square this Piece is on is "en passantable",
	 * meaning if a pawn moves to this square, it would result in a 
	 * En Passant capture. Should only be true for Blanks.
	 */
	public boolean isPassant(){
		return false;
	}

	/**
	 * Only implemented for Blank, marks this Piece as one where an
	 * En Passant capture could happen
	 * @param b 
	 */
	public void setPassant(boolean isPassant) {}
  
	/**
	 * @return char, the kind of this Piece
	 */
	abstract public char getKind();

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
