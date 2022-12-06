/*
 * File: Client.java
 * Author: Miles Gendreau
 * Course: CSC 335, Fall 2022
 * Description: Creates a Client for the multiplayer networking.
 * Client objects are created using a host ip as a string, port number as an int,
 * 		and a player object
 */

package ServerClient;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import Game.GameModel;
import PiecePackage.Piece;

public class Client extends Thread {
	private boolean running = false;

	private Socket socket;
	private String host;
	private int port;

	private ObjectInputStream in;
	private ObjectOutputStream out;
	private String curr_pos;
	private String curr_move;

	private int id;
	private boolean turn_active;
	private GameModel model;
	private Player player;
	private int color;

	/**
	 * Client Constructor
	 * @param host is a string the represents the ip of the server
	 * @param port is an int representing the port number
	 * @param p is a player object tied to the client
	 * @return None
	 */
	public Client(String host, int port, Player p) {
		this.host = host;
		this.port = port;
		player = p;
		id = 0;
		color = Piece.WHITE;
	}

	/**
	 * return the color of the player
	 * @return color is an int. Specifications can be found in the Piece class
	 */
	public int getColor() {
		return color;
	}

	/**
	 * sets the color of the Client/Player
	 * @param color is an int. Specifications can be found in the Piece class
	 * @return None
	 */
	public void setColor(int color) {
		this.color = color;
	}

	/**
	 * Returns the GameModel
	 * @return model represents the current GameModel of the game
	 */
	public GameModel getModel() {
		return model;
	}

	/**
	 * Creates a connection and tries to connect to the host or server
	 * @return None
	 */
	public void openConnection() {
		try {
			socket = new Socket(host, port);
			print_debug("made the socket");
			running = true;
			this.start();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * closes the Connection to the server
	 * @return None
	 */
	public void closeConnection() {
		if (socket == null) {
	    	print_debug("No connection active!");
			return;
		}
		try {
			// wave goodbye to server
			out.writeObject("bye!");
			out.flush();

			socket.close();
			running = false;
			this.interrupt();
			print_debug("Connection closed!");
		}
		catch (IOException e) { e.printStackTrace(); }
	}

	/**
	 * Sets the current position and move to send to the server
	 * @param pos is a String representing the position of the piece being moved
	 * @param move is a String representing the move being made
	 * @return None
	 */
	public void sendMove(String pos, String move) {
		curr_pos = pos;
		curr_move = move;
	}

	/**
	 * gets whose turn it currently is
	 * @return turn_active returns true if its Client's turn, false otherwise
	 */
	public boolean getTurn() {
		return turn_active;
	}

	/**
	 * Overrides the run method of the Thread class. Facilitate communication between
	 * 		Client and Server
	 * @return None
	 */
	@Override
	public void run() {
		try {
			print_debug("trying to make streams");
			out = new ObjectOutputStream(socket.getOutputStream());
	    	out.flush();
	    	in = new ObjectInputStream(socket.getInputStream());

	    	// initial communication
	    	print_debug("Sending request to socket server...");
	    	out.writeObject("Requesting turn");

	    	turn_active = in.readBoolean();
	    	color = in.readInt();

	    	model = (GameModel) in.readObject();
			player.setBoard(model.getCurrentBoard());
			player.updateBoard(model.getCurrentBoard());
	    	print_debug("Received model!");
		}
		catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}

		// continual communication
		while (running) {
			if (!turn_active && !model.getIsOver()) {
				try {
					print_debug("Waiting for my turn...");
					turn_active = in.readBoolean();
					model = (GameModel) in.readObject();
					print_debug("It's my turn now!");
					player.updateBoard(model.getCurrentBoard());
				}
				catch (IOException | ClassNotFoundException e) {
					print_debug("Turn assignment failed!");
					e.printStackTrace();
				}
			}
			else if (curr_move != null) {
				try {
					print_debug("Sending my move");
					out.writeObject(curr_pos);
					out.writeObject(curr_move);
					curr_pos = curr_move = null;
					turn_active = false;
				}
				catch (IOException e) {
					print_debug("Failed to send move!");
					e.printStackTrace();
				}
			}
			else {
				try {
					Thread.sleep(10);
				}
				catch (InterruptedException e) {
					print_debug("Thread was interrupted!");
				}
			}
		}
	}

	//----------Private Method-------------
	
	/**
	 * Prints out a debug message
	 * @param msg is a String representing the debug message
	 * @return None
	 */
	private void print_debug(String msg) {
		System.out.println("  [Client " + id + "] " + msg);
		System.out.flush();
	}
}
