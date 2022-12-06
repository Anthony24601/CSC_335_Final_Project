package ServerClient;
import UI.MainMenu;

public class Main {

	private static String game_file;
	private static String ai; // maybe change to work w/ player class
	private static int mode;
	
	public static void main(String[] args) {
		start();
	}
	
	public static void start() {
		setConfig(null, null, 0);
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
 	
	public static void setConfig(String game_file, String ai, int mode) {
		Main.game_file = game_file;
		Main.ai = ai;
		Main.mode = mode;
	} 
	
	public static void localGame() {
		LocalPlayer player = new LocalPlayer(ai);
		if (game_file != null) { 
			boolean success = player.loadGame(game_file);
			if (!success) { System.out.println("Failed to load file"); }
		}
		
		player.run();
		if (player.finished) { start(); }
	}
	
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
		if (player.finished) { start(); }
	}
	
	public static void networkedGameJoin() {
		NetworkedPlayer player = new NetworkedPlayer();
		player.open();
		player.run();
		if (player.finished) { start(); }
	}
}
