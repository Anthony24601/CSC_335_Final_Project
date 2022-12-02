import java.util.ArrayList;
import java.util.Arrays;

public class Player {
	
	public final String type;
	Board board;
	static ChessUI ui;
	Client client;
	String selected1;
	String selected2;
	
	ArrayList<String> possible_moves;
	
	public Player(String type) {
		this.type = type;
		board = new Board(false);
		client = new Client("127.0.0.1", 59896, this);
		possible_moves = new ArrayList<>();
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
		System.out.println("==============");
		System.out.println(rank + " " + file);
		for (String x: possible_moves) {
			System.out.println(x);
		}

		if (client.getTurn()) {
			if (selected1 == null || possible_moves.size() == 0) {
				if (board.get(rank, file).getColor() == client.getColor()) {
					selected1 = select;
					possible_moves = getMoves(client.getModel().getPossibleMoves(selected1));
					ui.updatePossibles(possible_moves);
				}
			} else {
				if (possible_moves.contains(select)) {
					selected2 = select;
					boolean capture = false;
					int rank_prev = selected1.charAt(1)-'0';
					int file_prev = selected1.charAt(0)-'a'+1;
					if (board.get(rank, file).getColor() != Piece.BLANK) {
						capture = true;
					}
					String temp = client.getModel().constructMove(board.get(rank_prev, file_prev), rank, file, capture);
					boolean temp2 = client.getModel().makeMove(temp);
					if (temp2) {
						client.sendMove(selected1, selected2);
					}
					updateBoard(client.getModel().getCurrentBoard());
					//board.move(selected1, selected2);
					
					//client.getModel().makeMove(selected2);
					//updateBoard(board);
					
				}
				selected1 = null;
				selected2 = null;
				possible_moves.clear();
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
	
	private ArrayList<String> getMoves(ArrayList<String> arrayList) {
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
