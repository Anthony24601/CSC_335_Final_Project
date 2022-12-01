import java.util.Random;

public class NetworkTesting {
	public static void main(String[] args) {
		Random rand = new Random();
		for (int i = 0; i < 10; i++) {
			int x = rand.nextInt(2);
			System.out.println(x);
		}
		/*
		Server server = new Server(59896);
		Client client_0 = new Client("127.0.0.1", 59896, 0);
		Client client_1 = new Client("127.0.0.1", 59896, 1);
	
		server.openServer();
		client_0.openConnection();
		client_1.openConnection();
		
		client_0.sendMove("a0", "KC3");
		client_1.sendMove("h3", "PH5");
		
		try {
			Thread.sleep(20000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		client_0.closeConnection();
		client_1.closeConnection();
		server.closeServer();
		System.exit(0);
		*/
	}
}
