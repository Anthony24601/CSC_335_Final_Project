/*
 * File: Server.java
 * Author: Miles Gendreau
 * Course: CSC 335, Fall 2022
 * Description: This file contains the Server class, which is used for 
 * multiplayer networking.
 */

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server extends Thread {
	private boolean running;
	
	private ServerSocket listener;
	private int port;
	
	private ClientManager[] clients;
	private final int MAX_PLAYERS;
	private int turn;
	
	private final int[] COLORS;
	private int[] player_ids;
			
	private GameModel model;
	
	public Server(int port) {
		this.running = false;
		this.port = port;
		this.MAX_PLAYERS = 2;
		this.clients = new ClientManager[MAX_PLAYERS];
		this.turn = 0;
		
		Random rand = new Random();
		this.COLORS = new int[] {Piece.WHITE, Piece.BLACK};
		int id_1 = rand.nextInt(2);
		this.player_ids = new int[] {id_1, 1-id_1};
		
		this.model = GameModel.getInstance();
	}
	
	public void openServer() {
		print_debug("Server listening on port " + port);
		try {
			listener = new ServerSocket(port);
			running = true;
			this.start();
		}
		catch (IOException e) { 
			e.printStackTrace(); 
		}
	}
	
	public void closeServer() {
		try {
			listener.close();
			running = false;
			this.interrupt();
			print_debug("Server closed.");
		}
		catch (IOException e) { 
			e.printStackTrace(); 
		}
	}
	
	@Override
	public void run() {
		ExecutorService pool = Executors.newFixedThreadPool(MAX_PLAYERS);
		for (int i = 0; i < MAX_PLAYERS; i++) {
			try {
				Socket socket = listener.accept();
				ClientManager manager = new ClientManager(socket, player_ids[i]);
				clients[player_ids[i]] = manager;
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		for (ClientManager m : clients) {
			pool.execute(m);
		}
	}
	
	private void print_debug(String msg) {
		System.out.println("[Server] " + msg);
		System.out.flush();
	}
	
	private class ClientManager implements Runnable {
		private Socket socket;
		private int id;

		private ObjectInputStream in;
		private ObjectOutputStream out;
		
		public ClientManager(Socket socket, int id) {
			this.socket = socket;
			this.id = id;
		}
		
		@Override
		public void run() {
			print_debug("New connection: " + socket);
			
			try {
				out = new ObjectOutputStream(socket.getOutputStream());
				out.flush();
				in = new ObjectInputStream(socket.getInputStream());
				
				// initial communication
				String message = (String) in.readObject();
				
				print_debug("Sending turn to client " + id);
		    	out.writeBoolean((turn == id));
		    	out.writeInt(COLORS[id]);
		    	
		    	print_debug("Sending GameModel to client " + id + "...");
		    	out.writeObject(model);
				out.flush();
				out.reset();
			} 
			catch (IOException | ClassNotFoundException e) {
				print_debug("Initial communication failed!");
				e.printStackTrace();
			}
			
			String loc;
			String move;
			while (running) {
				// continuous communication
				try {
					// read current player's move
					loc = (String) in.readObject();
					if (loc.equals("bye!")) {
						print_debug("client " + id + " disconnecting");
						sendModel();
						break;
					}
					
					move = (String) in.readObject();
					print_debug("Player " + turn + "'s move: " + loc + ", " + move);
					
					// update piece in gameModel
					model.getCurrentBoard().move(loc, move);
					
					// set next player's turn and send them the model
					turn = (turn + 1) % clients.length;
					sendModel();
				}
				catch (IOException | ClassNotFoundException e) {
					print_debug("Communication failed.");
					e.printStackTrace();
				}
			}
			
			try {
				socket.close();
			} catch (IOException e) {
				print_debug("Failed to close socket " + socket);
				e.printStackTrace();
			}
		}
		
		private void sendModel() {
			try {
				print_debug("Sending model to next player " + turn);
				clients[turn].out.writeBoolean(true);
				clients[turn].out.writeObject(model);
				clients[turn].out.flush();
				clients[turn].out.reset();
			}
			catch (IOException e) {
				print_debug("Failed to send model (client may have disconnected).");
				e.printStackTrace();
			}
		}
	}
}
