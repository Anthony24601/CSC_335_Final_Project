import java.io.File;

public class Player {
	
	Board board;
	static ChessUI ui;
	Client client;
	String selected1;
	String selected2;
	
	String[] possible_moves;
	
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
			if (selected1 == null) {// || possible_moves.length == 0) {
				System.out.println("hi");
				//System.out.println(board.get(rank, file));
				selected1 = select;
				//ui.updatePossibles(possible_moves);
				/*
				if (board.get(rank, file).getColor() == client.getColor()) {
					selected1 = select;
					//board.get(rank, file).getValidMoves(board);
					//possible_moves = getMoves(board.get(rank, file).getValidMoves(board));
					ui.updatePossibles(possible_moves);
				}
				*/
			} else {
				selected2 = select;
				board.move(selected1, selected2);
				client.sendMove(selected1, selected2);
				selected1 = null;
				selected2 = null;
				possible_moves = new String[0];
				//ui.updatePossibles(possible_moves);
				System.out.println("hey");
				/*
				if (Arrays.asList(possible_moves).contains(select)) {
					selected2 = select;
					board.move(selected1, selected2);
					client.sendMove(selected1, selected2);
					selected1 = null;
					selected2 = null;
					possible_moves = new String[0];
					ui.updatePossibles(possible_moves);
				} else {
				}
				*/
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
