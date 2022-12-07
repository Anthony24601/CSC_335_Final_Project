/**
File: Main.java
Author: Miles Gendreau
Course: CSC 335
Purpose: This is the Main file of the game. In order to run the game, run this file.
*/

package Start;
import ServerClient.LocalPlayer;
import ServerClient.NetworkedPlayer;
import ServerClient.Server;
import UI.MainMenu;

public class Main {

	private static String game_file;
	private static String ai;
	private static int mode;
	
	public static void main(String[] args) {
		start();
	}
	
	/**
     * Starts the game
     * @param N/A
     * @return None
     */
	public static void start() {
		setConfig(null, null, 0);
		GameModel.resetInstance();

		MainMenu menu = new MainMenu();
		menu.run();
		
		switch (mode) {
		case 0:
			return;
		case 1:
			Main.localGame();
			break;
		case 2:
			Main.networkedGameCreate();
			break;
		case 3:
			Main.networkedGameJoin();
			break;
		}
	}
 	
	/**
     * Sets the configurations set by the player
     * @param N/A
     * @return None
     */
	public static void setConfig(String game_file, String ai, int mode) {
		Main.game_file = game_file;
		Main.ai = ai;
		Main.mode = mode;
	} 
	
	/**
     * Creates a local game
     * @param N/A
     * @return None
     */
	public static void localGame() {
		LocalPlayer player = new LocalPlayer(ai);
		if (game_file != null) { 
			boolean success = player.loadGame(game_file);
			if (!success) { System.out.println("Failed to load file"); }
		}
		
		player.run();
		if (player.finished) { start(); }
	}
	
	/**
     * Creates a networked game
     * @param N/A
     * @return None
     */
	public static void networkedGameCreate() {
		Server server = new Server(59896);
		if (game_file != null) { 
			boolean success = server.loadGame(game_file);
			if (!success) { System.out.println("Failed to load file"); }
		}
		
		server.openServer();
		
		NetworkedPlayer player = new NetworkedPlayer();
		player.open();
		player.run();
		if (player.finished) { 
			player.close();
			server.closeServer();
			start(); 
		}
	}
	
	/**
     * Joins a networked game
     * @param N/A
     * @return None
     */
	public static void networkedGameJoin() {
		NetworkedPlayer player = new NetworkedPlayer();
		player.open();
		player.run();
		if (player.finished) { 
			player.close();
			start(); 
		}
	}
}
