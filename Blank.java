
public class Blank extends Piece{

	public Blank(int color, int rank, int file) {
		super(color, rank, file, "blank");
	}

	public String[] getValidMoves(Board board) {
		return null;
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
}
