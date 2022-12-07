package ServerClient;
import java.util.Scanner;

import Game.AI;
import Game.Board;
import Game.GameModel;
import PiecePackage.Piece;

public class LocalPlayer extends Player {
	private static final int COLORS[] = new int[] {Piece.WHITE, Piece.BLACK};
	private int turn = 0;
	
	private GameModel model;

	private AI ai;
	final boolean HAS_AI;
	
	public LocalPlayer(String ai_type) {
		super();
		this.model = GameModel.getInstance();
		this.model.setCurrentBoard(new Board(false));
		this.board = GameModel.getInstance().getCurrentBoard();
		if (AI.isValidType(ai_type)) {
			HAS_AI = true;
			ai = new AI(false, ai_type);
		}
		else { HAS_AI = false; }
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
						Thread.yield();
						ui.update();
						turn = (turn + 1) % 2;
					}
					if (HAS_AI && !model.getHasCheckmate()) {
						model.makeMove(ai.decideOnMove());
						board = model.getCurrentBoard();
						ui.update();
						turn = (turn + 1) % 2;
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
	
	// new loadGame - called from main
	public boolean loadGame(String game_file) {
		boolean success = model.loadGame(game_file);
		
		if(!model.isWhitesTurn()){
			turn = 1;
		}
		return success;
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
