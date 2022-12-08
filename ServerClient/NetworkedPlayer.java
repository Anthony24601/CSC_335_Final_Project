/*
 * File: NetworkedPlayer.java
 * Author: Miles Gendreau
 * Course: CSC 335, Fall 2022
 * Description: NetworkedPlayer extends the Player class and represents a Player in a networked
 * 				Game
 * NetworkedPlayer objects are created with a the default constructor
 */

package ServerClient;
import Game.GameModel;
import PiecePackage.Piece;

public class NetworkedPlayer extends Player {
	private Client client;
	
	/**
	 * Default constructor for a networked player
	 */
	public NetworkedPlayer() {
		client = new Client("127.0.0.1", 59896, this);
	}
	
	/**
	 * Opens the connection of the server
	 * @return None
	 */
	public void open() {
		client.openConnection();
	}

	/**
	 * Closes the connection to the server
	 */
	public void close() {
		client.closeConnection();
	}
	
	/**
	 * Returns the GameModel of the game
	 * @return the GameModel of the game
	 */
	public GameModel getModel() {
		return client.getModel();
	}
	
	/**
	 * Overrides the move method in Player class
	 * @param select is a String representing a square a player selected
	 * @return None
	 */
	@Override
	public void move(String select) {
		if (select.equals("Forfeit")) {
			int turn = Piece.BLACK;
			if (client.getModel() != null && client.getModel().isWhitesTurn()) {
				turn = Piece.WHITE;
			}
			if (turn == client.getColor()) {
				GameModel model = client.getModel();
				model.flipTurn();
				model.setIsOver();
				model.setHasCheckmate();
				ui.update();
				client.sendMove("Forfeit", "");
			}
		} else {
			board = client.getModel().getCurrentBoard();

			int rank = select.charAt(1)-'0';
			int file = select.charAt(0)-'a'+1;
			/*
			For Debug Purposes
			System.out.println("==============");
			System.out.println(rank + " " + file);
			for (String x: possible_moves) {
				System.out.println(x);
			}
			*/
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
						if (result) {
							client.sendMove(selected1, selected2);
						}
						updateBoard(client.getModel().getCurrentBoard());
					}
					selected1 = null;
					selected2 = null;
					possible_moves.clear();
					ui.updatePossibles(possible_moves);
				}
			}
		}
	}

	/**
	 * Saves the game to a txt file
	 * @param fileName is the name of the saved file
	 * @return None
	 */
	public void saveGame(String fileName) {
		GameModel model = client.getModel();
		model.saveGame(fileName);
	}
	
	/**
	 * gets the Type of Player
	 * @return "Local"
	 */
	public String getType() {
		return "Network";
	}
	
	/**
	 * Gets the color of the Player
	 * @return the color of the Player
	 */
	public int getColor() {
		return client.getColor();
	}
}
