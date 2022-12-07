/*
 * File: Player.java
 * Author: Miles Gendreau
 * Course: CSC 335, Fall 2022
 * Description: Abstract class representing the player. Has two subclasses NetworkedPlayer
 * 				and Local Player.
 * Player objects are created with a the default constructor
 */
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
	
	public boolean finished;

	/**
	 * Default Constructor
	 */
	public Player() {
		ui = new ChessUI(this);
		board = new Board(false);
		possible_moves = new ArrayList<>();
		finished = false;
	}
	
	/**
	 * Overrides the move method in Player class
	 * @param select is a String representing a square a player selected
	 */
	public abstract void move(String select);
	
	/**
	 * return the board of the player
	 * @return board is an Board object representing the current board
	 */
	public Board getBoard() {
		return board;
	}

	/**
	 * sets the current board to a new board
	 * @param board is a Board object
	 * @return None
	 */
	public void setBoard(Board board) {
		this.board = board;
	}
	
	/**
	 * Returns the GameModel of the game
	 * @return the GameModel of the game
	 */
	public abstract GameModel getModel();

	/**
	 * Runs the UI
	 * @return None
	 */
	public void run() {
		ui.run();
	}

	/**
	 * Overrides the updateBoard method in the Player class.
	 * Updates the board in UI
	 * @param board is a Board object
	 * @return null
	 */
	public void updateBoard(Board board) {
		this.board = board;
		ui.update();
	}

	/**
	 * translate between a move list to a squares list
	 * @param arrayList is an Array List of string of moves
	 * @return an arraylist of squares
	 */
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

	/**
	 * Saves the game to a txt file
	 * @param fileName is the name of the saved file
	 * @return None
	 */
	public abstract void saveGame(String fileName);
	
	/**
	 * gets the Type of Player
	 * @return "Local"
	 */
	public abstract String getType();
	
	/**
	 * Gets the color of the Player
	 * @return the color of the Player
	 */
	public abstract int getColor();
}
