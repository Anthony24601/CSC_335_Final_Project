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
	
	private int id;
	private boolean turn_active;
	
	public Client(String host, int port) {
		this.host = host;
		this.port = port;
	}
	
	public Client(String host, int port, int id) {
		this(host, port);
		this.id = id;
	}
	
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
			socket.close();
			running = false;
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		this.interrupt();
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
	    	
	    	boolean response = (boolean) in.readBoolean();
	    	turn_active = response;
	    	print_debug("Response: " + response);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
		while (running) {
			// continual communication
		}
	}
	
	private void print_debug(String msg) {
		System.out.println("  [Client " + id + "] " + msg);
		System.out.flush();
	}
}
