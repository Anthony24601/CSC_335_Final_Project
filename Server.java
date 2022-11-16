

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.Executors;

/**
 * A server for a multi-player tic tac toe game. Loosely based on an example in
 * Deitel and Deitel’s “Java How to Program” book. For this project I created a
 * new application-level protocol called TTTP (for Tic Tac Toe Protocol), which
 * is entirely plain text. The messages of TTTP are:
 *
 * Client -> Server MOVE <n> QUIT
 *
 * Server -> Client WELCOME <char> VALID_MOVE OTHER_PLAYER_MOVED <n>
 * OTHER_PLAYER_LEFT VICTORY DEFEAT TIE MESSAGE <text>
 */
public class Server {

    public static void main(String[] args) throws Exception 
    {
        try (var listener = new ServerSocket(58901)) 
        {
            System.out.println("Tic Tac Toe Server is Running...");
            var pool = Executors.newFixedThreadPool(200);
            while (true) 
            {
                Game game = new Game();
                pool.execute(new Play(listener.accept(), new Player('X'), game));
                pool.execute(new Play(listener.accept(), new Player('O'), game));
            }
        }
    }
}

class Game 
{
    // Board cells numbered 0-63, top to bottom, left to right; null if empty
    private Player[] board = new Player[64];
    private Player player1 = null;
    private Player player2 = null;
    private Player currentPlayer = null;
    
    public Player[] getBoard() {return board;}
    public Player getCurrentPlayer() {return currentPlayer;}
    public void setCurrentPlayer(Player player) {currentPlayer = player;}
    public Player getPlayer1(){return player1;}
    public void setPlayer1(Player player) {player1 = player;}
    public Player getPlayer2(){return player2;}
    public void setPlayer2(Player player) {player2= player;}
}

class Player
{
	private char mark;
	Scanner input;
	PrintWriter output;
	
	
	public Player(char mark) {this.mark = mark;}
	public char getMark() {return mark;}
	public void setInput(Scanner input) { this.input = input;}
	public void setOutput(PrintWriter output) {this.output = output;}
	public Scanner getInput() {return input;}
	public PrintWriter getOutput() {return output;}
}

    /**
     * A Player is identified by a character mark which is either 'X' or 'O'. For
     * communication with the client the player has a socket and associated Scanner
     * and PrintWriter.
     */
class Play implements Runnable 
{
	Player you;
	Player opponent=null;
	Game game;
	Socket socket;

	public Play(Socket socket, Player player, Game game) 
	{
		this.socket = socket;
		this.you = player;
		this.game = game;
		System.out.println(player);
		game.setCurrentPlayer(you);
		if (game.getPlayer1() == null)
			game.setPlayer1(you);
		else
			game.setPlayer2(you);
	}

	@Override
	public void run() 
	{
		try 
		{
			you.setInput(new Scanner(socket.getInputStream()));
			you.setOutput(new PrintWriter(socket.getOutputStream(), true));
			you.getOutput().println("WELCOME " + you.getMark());
			processCommands();
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		} 
	}

	private void processCommands() 
	{
		while (you.getInput().hasNextLine()) 
		{
			if (you == game.getPlayer1()) opponent = game.getPlayer2();
			else opponent = game.getPlayer1();
			
			var command = you.getInput().nextLine();
			if (command.startsWith("QUIT"))
				return;
			else if (command.startsWith("MOVE"))
				processMoveCommand(Integer.parseInt(command.substring(5)));
		}
	}

	private void processMoveCommand(int location) 
	{
		try 
		{
			move(location, you);
			you.getOutput().println("VALID_MOVE");
			opponent.getOutput().println("OPPONENT_MOVED " + location);
			if (hasWinner()) 
			{
				you.getOutput().println("VICTORY");
				opponent.getOutput().println("DEFEAT");
			} 
			else if (boardFilledUp()) 
			{
				you.getOutput().println("TIE");
				opponent.getOutput().println("TIE");
			}
		} 
		catch (IllegalStateException e) 
		{
			you.getOutput().println("MESSAGE " + e.getMessage());
		}
	}

	public synchronized void move(int location, Player player) 
	{
		if (player != game.getCurrentPlayer()) {
			throw new IllegalStateException("Not your turn");
		} else if (game.getBoard()[location] != null) {
			throw new IllegalStateException("Cell already occupied");
		}
		game.getBoard()[location] = game.getCurrentPlayer();
		game.setCurrentPlayer(opponent);
	}

	public boolean hasWinner() 
	{
		Player[] board = game.getBoard();
		return (board[0] != null && board[0] == board[1] && board[0] == board[2])
				|| (board[3] != null && board[3] == board[4] && board[3] == board[5])
				|| (board[6] != null && board[6] == board[7] && board[6] == board[8])
				|| (board[0] != null && board[0] == board[3] && board[0] == board[6])
				|| (board[1] != null && board[1] == board[4] && board[1] == board[7])
				|| (board[2] != null && board[2] == board[5] && board[2] == board[8])
				|| (board[0] != null && board[0] == board[4] && board[0] == board[8])
				|| (board[2] != null && board[2] == board[4] && board[2] == board[6]);
	}

	public boolean boardFilledUp() 
	{
		return Arrays.stream(game.getBoard()).allMatch(p -> p != null);
	}
}