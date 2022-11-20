
public class Blank extends Piece{

	public Blank(String color, String name) {
		super(color, name);
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
}