
public class Main {
	private static boolean SERVER = false;
	public static void main(String[] args) {
		if(SERVER){
			Server server = new Server(59896);
			server.openServer();
		}
		else{
			LocalPlayer player = new LocalPlayer("normal");
			player.run();
		}	
		
	}
}
