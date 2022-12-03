
public class NetworkedPlayer extends Player {
	private Client client;
	
	public NetworkedPlayer(String type) {
		super(type);
		client = new Client("127.0.0.1", 59896, this);
	}
	
	public void open() {
		client.openConnection();
	}
	
	@Override
	public void move(String select) {
		//System.out.println(select);
		board = client.getModel().getCurrentBoard();

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
					boolean result = client.getModel().movePieceFromLocs(selected1, selected2);
					/*
					boolean capture = false;
					int rank_prev = selected1.charAt(1)-'0';
					int file_prev = selected1.charAt(0)-'a'+1;
					if (board.get(rank, file).getColor() != Piece.BLANK) {
						capture = true;
					}
					String temp = MoveParser.constructMove(board.get(rank_prev, file_prev), rank, file, capture);
					boolean temp2 = client.getModel().makeMove(temp);
					if (temp2) {
						client.sendMove(selected1, selected2);
					}
					*/
					if (result) {
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

	
	public void saveGame(String fileName) {
		GameModel model = client.getModel();
		model.saveGame(fileName);
	}
}
