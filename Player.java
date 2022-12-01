import java.util.Arrays;

public class Player {
	
	public final String type;
	Board board;
	static ChessUI ui;
	Client client;
	String selected1;
	String selected2;
	
	String[] possible_moves;
	
	public Player(String type) {
		this.type = type;
		board = new Board(false);
		client = new Client("127.0.0.1", 59896, this);
		possible_moves = new String[0];
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
		int rank = select.charAt(1)-'0';
		int file = select.charAt(0)-'a'+1;
		if (client.getTurn()) {
			if (selected1 == null || possible_moves.length == 0) {
				if (board.get(rank, file).getColor() == client.getColor()) {
					selected1 = select;
					possible_moves = getMoves(board.get(rank, file).getValidMoves(board));
					ui.updatePossibles(possible_moves);
				}
			} else {
				if (Arrays.asList(possible_moves).contains(select)) {
					selected2 = select;
					board.move(selected1, selected2);
					client.sendMove(selected1, selected2);
				}
				selected1 = null;
				selected2 = null;
				possible_moves = new String[0];
				ui.updatePossibles(possible_moves);
			}
		} else {
			System.out.println("no");
		}
	}
	
	public void moveAI() {
		// TODO - implement
	}
	
	public void updateBoard(Board board) {
		this.board = board;
		ui.update();
		if (type == "ai") {
			moveAI();
		}
	}
	
	private String[] getMoves(String[] possible) {
		for (int i = 0; i < possible.length; i++) {
			String temp = possible[i];
			if (temp.charAt(temp.length()-1) == '+') {
				temp = temp.substring(temp.length()-3, temp.length()-1);
			} else {
				temp = temp.substring(temp.length()-2);
			}
			possible[i] = temp;
		}
		return possible;
	}
}
