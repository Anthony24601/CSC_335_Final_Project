package ServerClient;
import java.util.ArrayList;

import Game.Board;
import Game.GameModel;
import UI.ChessUI;

public abstract class Player {

	protected ChessUI ui;
	protected Board board;
	protected String selected1;
	protected String selected2;
		
	protected ArrayList<String> possible_moves;

	public Player() {
		ui = new ChessUI(this);
		board = new Board(false);
		possible_moves = new ArrayList<>();
	}
	
	public abstract void move(String select);
	
	public Board getBoard() {
		return board;
	}

	public void setBoard(Board board) {
		this.board = board;
	}
	
	public abstract GameModel getModel();

	public void run() {
		ui.run();
	}

	public void moveAI() {
		// TODO - implement
	}

	public void updateBoard(Board board) {
		this.board = board;
		ui.update();
	}

	public ArrayList<String> getMoves(ArrayList<String> arrayList) {
		for (int i = 0; i < arrayList.size(); i++) {
			String entry = arrayList.get(i);
			String toLoc = "";
			if (entry.split(":")[1].equals("0-0")) {
				if (entry.split(":")[0].equals("e1")) {
					toLoc = "g1";
				} else if (entry.split(":")[0].equals("e8")) {
					toLoc = "g8";
				} else {
					System.out.println("Player.getMoves: huh?");
					System.exit(600);
				}
			}
			else if (entry.split(":")[1].equals("0-0-0")) {
				if (entry.split(":")[0].equals("e1")) {
					toLoc = "c1";
				} else if (entry.split(":")[0].equals("e8")) {
					toLoc = "c8";
				}
			}
			else if (entry.charAt(entry.length()-1) == '+' || entry.charAt(entry.length()-1) == '#') {
				toLoc = entry.substring(entry.length()-3, entry.length()-1);
			} else {
				toLoc = entry.substring(entry.length()-2);
			}
			arrayList.set(i, toLoc);
		}
		return arrayList;
	}

	public abstract void saveGame(String fileName);
	
	public abstract String getType();
	
	public abstract int getColor();
}
