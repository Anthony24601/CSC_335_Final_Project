
public abstract class Piece {

	String color;
	String name;

	public Piece(String color, String name) {
		this.color = color;
		this.name = name;
	}
  
	public String getPicture(int row, int col) {
		if ((col%2 == 0 && row%2 == 0) || (col%2 == 1 && row%2 == 1)) {
			return "images/light/" + color + "/" + name + ".png";
		} else {
			return "images/dark/" + color + "/" + name + ".png";
		}
	}
	
}