
public class Blank extends Piece{

	public Blank(int color) {
		super(color);
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

	public boolean isBlank(){
		return true;
	}

	@Override
	public boolean isValidMove(int fromRow, int fromCol, int toRow, int toCol) {
		return true;
	}

	@Override
	public int getColor() {
		return color;
	}
}
