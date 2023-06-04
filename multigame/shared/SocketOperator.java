package multigame.shared;

import java.net.*;
import java.io.*;
import multigame.shared.*;
import multigame.games.*;

public class SocketOperator {
	private Socket socket;
	private InputStream is;
	private OutputStream os;
	private int mSize;
	private byte[] buffer;
	public SocketOperator(Socket socket) throws Exception {
		this.socket = socket;
		is = socket.getInputStream();
		os = socket.getOutputStream();
		mSize = 512;
		buffer = new byte[mSize];
	}
	
	public SocketOperator(Socket socket, int mSize) throws Exception {
		this(socket);
		this.mSize = mSize;
	}
	
	public void sendMessage(String message) throws Exception {
		byte[] m = message.getBytes();
		for (int i = 0; i < m.length; i++) {
			buffer[i] = m[i];
		}
		os.write(buffer);
	}
	
	public void sendByte(int b) throws Exception {
		os.write(b);
	}
	
	public int recvByte() throws Exception {
		return is.read();
	}
	
	public String recvMessage() throws Exception {
		is.readNBytes(buffer, 0, mSize);
		return (new String(buffer)).trim();
	}
	
	public int validateRecvByte(int min, int max) throws Exception {
		int received = recvByte();
		while (received < min || received > max) {
			sendByte(NetCodes.INVALID);
			received = recvByte();
		}
		sendByte(NetCodes.OKAY);
		return received;
	}
	
	public int validateRecvByte(Game game) throws Exception {
    int received = recvByte();
		while (!game.isValidInput(received)) {
			sendByte(NetCodes.INVALID);
			received = recvByte();
		}
		sendByte(NetCodes.OKAY);
		return received;
	}

	public void printMessage() throws Exception {
		System.out.println(recvMessage());
	}
	
	public void close() throws Exception {
		socket.close();
	}
	
	public InetAddress getInetAddress() {
		return socket.getInetAddress();
	}
}
