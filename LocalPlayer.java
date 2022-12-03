
public class LocalPlayer extends Player {
	private static final int COLORS[] = new int[] {Piece.WHITE, Piece.BLACK};
	private int turn = 0;
	
	private GameModel model;
	
	public LocalPlayer(String type) {
		super(type);
		model = GameModel.getInstance();
		this.model.setCurrentBoard(new Board(false));
	}
	
	@Override
	public void move(String select) {
		int rank = select.charAt(1)-'0';
		int file = select.charAt(0)-'a'+1;
		System.out.println("==============");
		System.out.println(rank + " " + file);
		for (String x: possible_moves) {
			System.out.println(x);
		}

		if (selected1 == null || possible_moves.size() == 0) {
			if (board.get(rank, file).getColor() == COLORS[turn]) {
				selected1 = select;
				possible_moves = getMoves(model.getPossibleMoves(selected1));
				ui.updatePossibles(possible_moves);
			}
		} else {
			if (possible_moves.contains(select)) {
				selected2 = select;
				boolean result = model.movePieceFromLocs(selected1, selected2);
				board = model.getCurrentBoard();
				if (result) {
					turn = (turn + 1) % 2;
				}
				/*
				boolean capture = false;
				int rank_prev = selected1.charAt(1)-'0';
				int file_prev = selected1.charAt(0)-'a'+1;
				if (board.get(rank, file).getColor() != Piece.BLANK) {
					capture = true;
				}
				String temp = MoveParser.constructMove(board.get(rank_prev, file_prev), rank, file, capture);
				boolean temp2 = model.makeMove(temp);
				if (temp2) {
					// probs need to do some stuff here
					board.move(selected1, selected2);
					turn = (turn + 1) % 2;
				}
				*/
			}
			selected1 = null;
			selected2 = null;
			possible_moves.clear();
			ui.updatePossibles(possible_moves);
		}

	}

	@Override
	void saveGame(String fileName) {
		model.saveGame(fileName);
	}
}
