
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
		if (game_file != null) { 
			// TODO load game
		}
		
		// TODO add ai stuff here
		LocalPlayer player = new LocalPlayer("normal");
		player.run();
	}
	
	public static void networkedGameCreate() {
		if (game_file != null) { 
			// TODO load game
		}
		
		Server server = new Server(59896);
		server.openServer();
		
		NetworkedPlayer player = new NetworkedPlayer("normal");
		player.open();
		player.run();
	}
	
	public static void networkedGameJoin() {
		NetworkedPlayer player = new NetworkedPlayer("normal");
		player.open();
		player.run();
	}
}
