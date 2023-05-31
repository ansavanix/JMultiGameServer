package multigame.client;

import java.util.Scanner;
import java.net.Socket;
import multigame.shared.*;
import multigame.games.Game;


public class Client {
	private static SocketOperator so;
	private static Scanner scanner;
	private static final String WIN = "You win!";
	private static final String LOSE = "You lose!";
	private static final String TIE = "You tied!";
	
	public static void main(String[] args) {
		if (args.length < 2) {
			System.out.println("Takes 2 arguments: server address, port number.");
			System.exit(1);
		}
		try {
			so = new SocketOperator(new Socket(args[0], Integer.valueOf(args[1])));
			scanner = new Scanner(System.in);
			System.out.println(so.recvMessage()); //Get game options.
			sendValidatedByte();
			System.out.println("WAITING...");
			while (so.recvByte() != NetCodes.READY) {
				//Go back to conditional statement.
			}
			System.out.println("SERVER READY");
			int serverByte = so.recvByte();
			System.out.println(serverByte);
			while (serverByte == NetCodes.CONTINUE) {
				System.out.println(so.recvMessage());
				sendValidatedByte();
				serverByte = so.recvByte();
			}
			serverByte = so.recvByte();
			System.out.println("GAME OVER!");
			String finalState = null;
			switch (serverByte) {
				case Game.WIN:
					finalState = WIN;
					break;
				case Game.LOSS:
					finalState = LOSE;
					break;
				default:
					finalState = TIE;
					break;
			}
			System.out.println(finalState);
		}
		catch (Exception e) {
			System.out.println(e);
			System.exit(1);
		}
	}
	
	private static void sendValidatedByte() throws Exception {
		int input = scanner.nextInt();
		so.sendByte(input);
		while (so.recvByte() == NetCodes.INVALID) {
			System.out.println("Rejected by server! Try again!");
			input = scanner.nextInt();
			so.sendByte(input);
		}
	}
}
