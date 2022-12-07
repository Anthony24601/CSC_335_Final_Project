/**
 * File: Blank.java
 * Author: Grace Driskill
 * Course: CSC 335
 * Purpose: Represents a blank/empty spot on the chess board
 * Constructors:
 * Blank(int color, int rank, int file)
 */
package PiecePackage;
import Game.Board;
import Game.GameModel;

public class Blank extends Piece{
	private boolean passantState;

	/**
	 * Creates a new Blank
	 * @param color which color to make this Blank (Piece.WHITE or
	 * 	Piece.BLACK)
	 * @param rank rank of this Blank
	 * @param file file of this Blank
	 */
	public Blank(int color, int rank, int file) {
		super(color, rank, file, "blank");
		passantState = false;
	}

	@Override
	public String[] getValidMoves(Board board, GameModel gameModel) {
		return null;
	}

	@Override
	public boolean canCheck(Board board) {
        return false;
    }

	@Override
	public boolean isBlank(){
		return true;
	}

	@Override
	public int getColor() {
		return color;
	}

	@Override
	public char getKind() {
		return 0;
	}

	@Override
	public Blank copy() {
		return new Blank(color, rank, file);
	}

	@Override
	public boolean isPassant(){
		return passantState;
	}

	/**
	 * Sets the passantState to true or false
	 */
	public void setPassant(boolean isPassant){
		passantState = isPassant;
	}
}
