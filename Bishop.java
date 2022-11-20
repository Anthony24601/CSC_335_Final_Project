public class Bishop extends Piece {
    private static final String FILE_NAME = "bishop.png";
    
    private Board board;

    public Bishop(int color, Board b) {
        super(color);
        board = b;
    }

    @Override
    public boolean isValidMove(int fromRow, int fromCol, int toRow, int toCol) {
        // TODO Auto-generated method stub
        return false;
    }

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
