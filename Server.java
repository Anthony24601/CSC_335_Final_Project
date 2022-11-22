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
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server extends Thread {
	private boolean running;
	
	private ServerSocket listener;
	private int port;
	
	private ArrayList<ObjectOutputStream> global_out;
	int player_count;
	int turn;
	
	public Server(int port) {
		this.running = false;
		this.port = port;
		global_out = new ArrayList<>();
		player_count = 0;
		turn = 0;
	}
	
	public void openServer() {
		System.out.println("[Server] Server listening on port " + port);
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
			System.out.println("[Server] Server Closed.");
		}
		catch (IOException e) { 
			e.printStackTrace(); 
		}
		this.interrupt();
	}
	
	@Override
	public void run() {
		ExecutorService pool = Executors.newFixedThreadPool(5); // change for # players?
		while (running) {
			try {
				Socket socket = listener.accept();
				ClientManager manager = new ClientManager(socket, player_count++);
				pool.execute(manager);
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void print_debug(String msg) {
		System.out.println("[Server] " + msg);
		System.out.flush();
	}
	
	private class ClientManager implements Runnable {
		private boolean running;
		
		private Socket socket;
		private int id;

		private ObjectInputStream in;
		private ObjectOutputStream out;
		
		public ClientManager(Socket socket, int id) {
			this.running = false;
			this.socket = socket;
			this.id = id;
		}
		
		@Override
		public void run() {
			print_debug("New connection: " + socket);
			running = true; // possibly move to another function
			
			// initial communication
			try {
				out = new ObjectOutputStream(socket.getOutputStream());
				out.flush();
				global_out.add(out);
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
			
			while (running) {
				// continuous communication
			}
		}
		
	}
}
