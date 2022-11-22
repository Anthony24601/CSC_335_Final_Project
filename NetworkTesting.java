
public class NetworkTesting {
	public static void main(String[] args) {
		Server server = new Server(59896);
		Client client = new Client("127.0.0.1", 59896);
	
		server.openServer();
		client.openConnection();
		
		try {
			Thread.sleep(20000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		client.closeConnection();
		server.closeServer();
		System.exit(0);
	}
}
