
public class Main {

	private static String game_file;
	private static String ai; // maybe change to work w/ player class
	private static int mode;
	
	public static void main(String[] args) {
		MainMenu menu = new MainMenu();
		menu.run();
		
		switch (mode) {
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
	}
	
	public static void networkedGameJoin() {
		NetworkedPlayer player = new NetworkedPlayer();
		player.open();
		player.run();
	}
}
