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
	
	public Server(int port) {
		this.running = false;
		this.port = port;
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
				ClientManager manager = new ClientManager(socket);
				pool.execute(manager);
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private static class ClientManager implements Runnable {
		private boolean running;
		
		private Socket socket;
		private ObjectInputStream in;
		private ObjectOutputStream out;
		
		public ClientManager(Socket socket) {
			this.running = false;
			this.socket = socket;
		}
		
		@Override
		public void run() {
			System.out.println("[Server] New Connection: " + socket);
			running = true; // possibly move to another function
			
			// initial communication
			try {
				out = new ObjectOutputStream(socket.getOutputStream());
				out.flush();
				in = new ObjectInputStream(socket.getInputStream());
				
				String message = (String) in.readObject();
				System.out.println("[Server] Received client message: " + message);
				
				out.writeObject("Hi!");
				System.out.println("[Server] Sending response to client " + socket);
			} 
			catch (IOException | ClassNotFoundException e) {
				System.out.println("[Server] Connection failed!");
				e.printStackTrace();
			}
			
			while (running) {
				// continuous communication
			}
		}
	}
}
