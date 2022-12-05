import java.util.Scanner;

public class LocalPlayer extends Player {
	private static final int COLORS[] = new int[] {Piece.WHITE, Piece.BLACK};
	private int turn = 0;
	
	private GameModel model;

	AI ai;
	final static boolean HAS_AI = false;
	
	public LocalPlayer(String type) {
		super(type);
		model = GameModel.getInstance();
		this.model.setCurrentBoard(new Board(false));
		board = GameModel.getInstance().getCurrentBoard();
		loadGame();
		if (HAS_AI) {
			ai = new AI(false);
		}
	}
	
	public GameModel getModel() {
		return model;
	}
	
	@Override
	public void move(String select) {
		if (select.equals("Forfeit")) {
			model.flipTurn();
			model.setIsOver();
			model.setHasCheckmate();
		} else {
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
						ui.moveSound();
						turn = (turn + 1) % 2;
					}
					if (HAS_AI) {
						model.makeMove(ai.decideOnMove());
						ui.moveSound();
						turn = (turn + 1) % 2;
						board = model.getCurrentBoard();
					}
				}
				selected1 = null;
				selected2 = null;
				possible_moves.clear();
				ui.updatePossibles(possible_moves);
			}
		}
	}

	/**
	 * Prompts user whether they want to load an existing game. 
	 * If yes, asks for a file to load from and then loads the
	 * info into the GameModel
	 */
	private void loadGame() {
		Scanner s = new Scanner(System.in);
		System.out.println("Load game from a file? y/n");
		String loadGame = s.nextLine();
		if(loadGame.equals("y")){;
			boolean success;
			String fileName;
			do{
				System.out.println("Enter file name or s to stop: ");
				fileName = s.nextLine();
				success = model.loadGame(fileName);
			}
			while(!(success || fileName.equals("s")));
		}
		s.close();

		if(!model.isWhitesTurn()){
			turn = 1;
		}
	}
	
	@Override
	public void saveGame(String fileName) {
		model.saveGame(fileName);
	}
	
	public String getType() {
		return "Local";
	}
	
	public int getColor() {
		return Piece.BLANK;
	}
}
