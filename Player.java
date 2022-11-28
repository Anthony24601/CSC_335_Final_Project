
public class Player {
	
	Board board;
	static ChessUI ui;
	
	public Player() {
		board = new Board(false);
		ui = new ChessUI(this);
	}
	
	public Board getBoard() {
		return board;
	}
	
	public static void main(String[] args) {
    	Player player = new Player();
    	ui.run();
    }
}
