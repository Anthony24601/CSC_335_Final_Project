import java.io.File;

public class Player {
	
	Board board;
	static ChessUI ui;
	Client client;
	String selected1;
	String selected2;
	
	public Player() {
		board = new Board(false);
		client = new Client("127.0.0.1", 59896, this);
	}
	
	public void makeUI() {
		ui = new ChessUI(this);
	}
	
	public Board getBoard() {
		return board;
	}
	
	public void run() {
		ui.run();
	}
	
	public void open() {
		client.openConnection();
	}
	
	public void move(String select) {
		//System.out.println(select);
		if (client.getTurn()) {
			System.out.println("yay");
			if (selected1 == null) {
				selected1 = select;
			} else {
				selected2 = select;
				
				
				//TODO
				
				//Check if the move is valid!!!!
				board.move(selected1, selected2);
				client.sendMove(selected1, selected2);
				selected1 = null;
				selected2 = null;
			}
		} else {
			System.out.println("no");
		}
	}
	
	public void updateBoard(Board board) {
		this.board = board;
		ui.update();
	}
}
