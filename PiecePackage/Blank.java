package PiecePackage;
import Game.Board;
import Game.GameModel;

public class Blank extends Piece{
	private boolean passantState;

	public Blank(int color, int rank, int file) {
		super(color, rank, file, "blank");
		passantState = false;
	}

	public String[] getValidMoves(Board board, GameModel gameModel) {
		return null;
	}

	public boolean canCheck(Board board) {
        return false;
    }

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

	public void setPassant(boolean isPassant){
		passantState = isPassant;
	}
}
