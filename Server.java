import java.util.*;
import java.net.*;
import java.io.*;

public class Server {
	public static final String[] GAMES = {"Rock Paper Scissors"};
	private static final int timeoutSeconds = 60;
	public static void main(String[] args) {
		try {
			ServerSocket server = new ServerSocket(3004, 512);
			while (true) {
				Socket client = server.accept();
				System.out.println("New client connected...");
				client.setSoTimeout(timeoutSeconds * 1000);
				ClientHandler ch = new ClientHandler(client);
				ch.start();
			}
		}
 		catch (Exception e){
			System.out.println(e);
			System.exit(1);
		}		  
	}
}

class ClientHandler extends Thread {
	private static MultiWaitingRoom mwr = new MultiWaitingRoom(Server.GAMES.length);
	private SocketOperator so;
	ClientHandler(Socket client) throws Exception {
		so = new SocketOperator(client);
	}
	
	public void run() {
		SocketOperator so2 = null;
		try {
			String gameMenu = GameStrategy.toMenu(Server.GAMES);
			so.sendMessage(gameMenu);
			int selection = so.validateRecvByte(0, mwr.size() - 1);
			so2 = mwr.getWaiting(selection, so);
			if (so2 == null) {
				//Current socket has been sent to waiting room.
				System.out.println(so.getInetAddress() + " has been sent to the waiting room.");
				so.sendByte(NetCodes.WAITING);
				return;
			}
			so.sendByte(NetCodes.READY);
			so2.sendByte(NetCodes.READY);
			//Begin play with the two matched sockets.
			Game game = null;
			switch (selection) {
				case 0:
					game = new RPS();
					break;
				default:
					return;
			}
			String menu = "";
			int gameState = Game.CONTINUE;
			SocketOperator activePlayer = so;
			SocketOperator other = so2;
			//Initiate and sustain gameplay.
			while (gameState < Game.PLAYER1) {
				gameState = Game.CONTINUE;
				
				while (gameState == Game.CONTINUE) {
					activePlayer.sendByte(NetCodes.CONTINUE);
					String prompt = game.prompt();
					activePlayer.sendMessage(prompt);
					selection = activePlayer.validateRecvByte(game.getMinInput(), game.getMaxInput());
					gameState = game.move(selection);
				}
				//Swap activePlayer
				SocketOperator temp = activePlayer;
				activePlayer = other;
				other = temp;
			}
			so.sendByte(NetCodes.DONE);
			so2.sendByte(NetCodes.DONE);
			System.out.println("FINISHED GAME");
			//Send out final game state to players.
			if (gameState == Game.TIE) {
				so.sendByte(Game.TIE);
				so2.sendByte(Game.TIE);
			}
			else if (gameState == Game.PLAYER1) {
				so.sendByte(Game.WIN);
				so2.sendByte(Game.LOSS);
			}
			else {
				so.sendByte(Game.LOSS);
				so2.sendByte(Game.WIN);
			}
			
		}
		catch (Exception e) {
			System.out.println(e);
		}
	}
}



class MultiWaitingRoom {
	private WaitingRoom[] rooms;
	MultiWaitingRoom(int numRooms) {
		rooms = new WaitingRoom[numRooms];
		for (int i = 0; i < numRooms; i++) {
			rooms[i] = new WaitingRoom();
		}
	}
	public SocketOperator getWaiting(int roomNum, SocketOperator other) {
		return rooms[roomNum].getWaiting(other);
	}
	public int size() {
		return rooms.length;
	}
}

class WaitingRoom {
	private SocketOperator waiting;
	
	WaitingRoom() {
		waiting = null;
	}
	
	WaitingRoom(SocketOperator w) {
		waiting = w;
	}
	
	public synchronized SocketOperator getWaiting(SocketOperator other) {
		if (waiting == null) {
			waiting = other;
			return null;
		}
		else {
			SocketOperator temp = waiting;
			waiting = null;
			return temp;
		}
	}
	
}
