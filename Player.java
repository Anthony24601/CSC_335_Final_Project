
public class Player {
	
	static ChessUI ui;
	Client client;

	Board board;
	String pos_selected;
	String move_selected;
	
	public Player() {
		if (ui == null) { ui = new ChessUI(this); }
		client = new Client("127.0.0.1", 59896, this);
		board = new Board();
	}
	
	public Board getBoard() {
		return board;
	}
	
	public void run() {
		client.openConnection();
		ui.run();
	}
	
	public void select(String select) {
		if (pos_selected == null) {
			pos_selected = select;
		} else {
			move_selected = select;
		}
	}
	
	public void move() {
		//System.out.println(select);
		if (client.getTurn() && pos_selected != null && move_selected != null) {
			// TODO validate move
			board.move(pos_selected, move_selected);
			client.sendMove(pos_selected, move_selected);
			pos_selected = null;
			move_selected = null;
		} else {
			System.out.println("no");
		}
	}
	
	public void updateBoard(Board board) {
		this.board = board;
		ui.update();
	}
}
