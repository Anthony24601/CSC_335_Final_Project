/*
 * File: Client.java
 * Author: Miles Gendreau
 * Course: CSC 335, Fall 2022
 * Description: This file contains the Client class, which is used for 
 * multiplayer networking.
 */

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.net.Socket;

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
	Player player;
	
<<<<<<< Updated upstream
=======
	int color;
	
>>>>>>> Stashed changes
	public Client(String host, int port, Player p) {
		this.host = host;
		this.port = port;
		player = p;
		id = 0;
	}
	
	/*
	public Client(String host, int port, int id, Player p) {
		this(host, port, p);
		this.id = id;
	}
	*/
	
<<<<<<< Updated upstream
=======
	public int getColor() {
		return color;
	}
	
>>>>>>> Stashed changes
	public void openConnection() {
		try {
			socket = new Socket(host, port);
			running = true;
			this.start();
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
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
	
	public void sendMove(String pos, String move) {
		curr_pos = pos;
		curr_move = move;
	}
	
	public boolean getTurn() {
		return turn_active;
	}
	
	@Override
	public void run() {
		try {
			out = new ObjectOutputStream(socket.getOutputStream());
	    	out.flush();
	    	in = new ObjectInputStream(socket.getInputStream());
	    	
	    	// initial communication
	    	print_debug("Sending request to socket server...");
	    	out.writeObject("Requesting turn");
	    	
	    	boolean response = in.readBoolean();
	    	turn_active = response;
	    	
	    	model = (GameModel) in.readObject();
	    	print_debug("Received model!");
		}
		catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		while (running) {
			// continual communication
			if (!turn_active) {
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
					//e.printStackTrace();
				}
			}
		}
	}
	
	private void print_debug(String msg) {
		System.out.println("  [Client " + id + "] " + msg);
		System.out.flush();
	}
}
