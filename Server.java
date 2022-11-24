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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server extends Thread {
	private boolean running;
	
	private ServerSocket listener;
	private int port;
	
	private ClientManager[] clients;
	private final int MAX_PLAYERS;
	private int turn;
	
	public Server(int port) {
		this.running = false;
		this.port = port;
		this.MAX_PLAYERS = 2; // default, maybe change
		this.clients = new ClientManager[MAX_PLAYERS];
		this.turn = 0;
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
			print_debug("Server closed.");
		}
		catch (IOException e) { 
			e.printStackTrace(); 
		}
		this.interrupt();
	}
	
	@Override
	public void run() {
		ExecutorService pool = Executors.newFixedThreadPool(MAX_PLAYERS);
		for (int i = 0; i < MAX_PLAYERS; i++) {
			try {
				Socket socket = listener.accept();
				ClientManager manager = new ClientManager(socket, i);
				clients[i] = manager;
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
			
			// initial communication
			try {
				out = new ObjectOutputStream(socket.getOutputStream());
				out.flush();
				in = new ObjectInputStream(socket.getInputStream());
				
				String message = (String) in.readObject();
				print_debug("Received client request: " + message);

				print_debug("Sending response to client " + id);
		    	out.writeBoolean((turn == id));
				out.flush();
			} 
			catch (IOException | ClassNotFoundException e) {
				print_debug("Connection failed!");
				e.printStackTrace();
			}
			
			String move;
			while (running) {
				// continuous communication
				try {
					move = (String) in.readObject();
					print_debug("Player " + turn + "'s move: " + move);
					// to implement, update piece in gameModel
					turn = (turn + 1) % clients.length;
					print_debug("Sending turn to next player " + turn);
					clients[turn].out.writeBoolean(true);
					clients[turn].out.flush();
					// to implement, send gamemodel to other player
				}
				catch (IOException | ClassNotFoundException e) {
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
		
	}
}
