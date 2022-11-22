
public class Blank extends Piece{

	public Blank(int color, int rank, int file) {
		super(color, rank, file, "blank");
	}

	public String[] getValidMoves(Board board) {
		return null;
	}
	@Override
	public char getKind() {
		return 0;
	}

	@Override
	public int getRank() {
		return 0;
	}

	@Override
	public int getFile() {
		return 0;
	}

	public boolean isBlank(){
		return true;
	}

	/*@Override
	public boolean isValidMove(int fromRow, int fromCol, int toRow, int toCol) {
		return true;
	}*/

	@Override
	public int getColor() {
		return color;
	}
}
