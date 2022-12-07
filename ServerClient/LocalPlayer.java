/**
 * File: LocalPlayer.java
 * Author: Miles Gendreau
 * Course: CSC 335, Fall 2022
 * Description: LocalPlayer extends the Player class and represents a Player in a Local Game
 * Local Player objects are created with a string representing the ai type
 */

package ServerClient;
import Game.AI;
import Game.Board;
import Game.GameModel;
import PiecePackage.Piece;

public class LocalPlayer extends Player {
	private static final int COLORS[] = new int[] {Piece.WHITE, Piece.BLACK};
	private int turn = 0;
	
	private GameModel model;

	AI ai;
	final boolean HAS_AI;
	
	/**
	 * LocalPlayer Constructor
	 * @param ai_type is a string representing the ai type
	 */
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
	
	/**
	 * Gets the model of the current game
	 * @return null
	 */
	public GameModel getModel() {
		return model;
	}
	
	/**
	 * Overrides the move method in Player class
	 * @param select is a String representing a square a player selected
	 * @return None
	 */
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
					if (HAS_AI && !model.getHasCheckmate()) {
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
	 * Overrides the updateBoard method in the Player class.
	 * Updates the board in UI
	 * @param board is a Board object
	 * @return null
	 */
	@Override
	public void updateBoard(Board board) {
		super.updateBoard(board);
	}

	/**
	 * Loads a pre-created game in
	 * @param game_file is a String representing the game file's name
	 * @return true if loadGame worked, false if otherwise
	 */
	public boolean loadGame(String game_file) {
		boolean success = model.loadGame(game_file);
		
		if(!model.isWhitesTurn()){
			turn = 1;
		}
		return success;
	}
	
	/**
	 * Saves the game to a txt file
	 * @param fileName is the name of the saved file
	 * @return None
	 */
	@Override
	public void saveGame(String fileName) {
		model.saveGame(fileName);
	}
	
	/**
	 * gets the Type of Player
	 * @return "Local"
	 */
	public String getType() {
		return "Local";
	}
	
	/**
	 * Gets the color of the Player
	 * @return Blank. Since its a local player, there is no specified color attached
	 */
	public int getColor() {
		return Piece.BLANK;
	}
}
