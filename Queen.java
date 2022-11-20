public class Queen extends Piece {
    private static final String FILE_NAME = "queen.png";
    private Board board;

     /**
     * Constructs a new queen of the specified color on the
     * passed Board
     * @param color either Piece.BLACK or Piece.WHITE
     * @param b a Board object
     */
    public Queen(int color, Board b) {
        super(color);
        board = b;
    }

    @Override
    public boolean isValidMove(int fromRow, int fromCol, int toRow, int toCol) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public String getPicture(int row, int col) {
		String path = "images/";
		if (row%2 == 0) {
			if (col%2 == 0) {
                path += "light/";
			} else {
                path += "dark/";
			}
		} else {
			if (col%2 == 1) {
                path += "light/";
			} else {
                path += "dark/";
			}
		}
        if(color==Piece.WHITE){
            path += "white/";
        }
        else{
            path += "black/";
        }
        return path+FILE_NAME;
	}
   
    @Override
    public int getColor() {
        return color;
    }

    @Override
    public boolean isBlank() {
        return false;
    }
}
