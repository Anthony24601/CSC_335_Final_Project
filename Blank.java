
public class Blank extends Piece{

	public Blank(int color, int rank, int file) {
		super(color, rank, file);
	}

	public String getPicture(int row, int col) {
		if (row%2 == 0) {
			if (col%2 == 0) {
				return "images/light/blank.png";
			} else {
				return "images/dark/blank.png";
			}
		} else {
			if (col%2 == 1) {
				return "images/light/blank.png";
			} else {
				return "images/dark/blank.png";
			}
		}
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

	@Override
	public int getRank() {
		return 0;
	}

	@Override
	public int getFile() {
		return 0;
	}
}
