import java.util.ArrayList;

abstract class Player {

	protected final String type;
	protected ChessUI ui;
	protected Board board;
	protected String selected1;
	protected String selected2;
	
	protected ArrayList<String> possible_moves;

	public Player(String type) {
		this.type = type;
		ui = new ChessUI(this);
		board = new Board(false);
		possible_moves = new ArrayList<>();
	}
	
	abstract void move(String select);
	
	public Board getBoard() {
		return board;
	}

	public void run() {
		ui.run();
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

	protected ArrayList<String> getMoves(ArrayList<String> arrayList) {
		for (int i = 0; i < arrayList.size(); i++) {
			String temp = arrayList.get(i);
			if (temp.charAt(temp.length()-1) == '+') {
				temp = temp.substring(temp.length()-3, temp.length()-1);
			} else {
				temp = temp.substring(temp.length()-2);
			}
			arrayList.set(i, temp);
		}
		return arrayList;
	}
}
