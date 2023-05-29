import java.net.*;
import java.io.*;

public class SocketOperator {
	private Socket socket;
	private InputStream is;
	private OutputStream os;
	private int mSize;
	public SocketOperator(Socket socket) throws Exception {
		this.socket = socket;
		is = socket.getInputStream();
		os = socket.getOutputStream();
		mSize = 512;
	}
	
	public SocketOperator(Socket socket, int mSize) throws Exception {
		this(socket);
		this.mSize = mSize;
	}
	
	public void sendMessage(String message) throws Exception {
		byte[] padded = new byte[mSize];
		byte[] m = message.getBytes();
		for (int i = 0; i < m.length; i++) {
			padded[i] = m[i];
		}
		os.write(padded);
	}
	
	public void sendByte(int b) throws Exception {
		os.write(b);
	}
	
	public int recvByte() throws Exception {
		return is.read();
	}
	
	public String recvMessage() throws Exception {
		byte[] message = is.readNBytes(mSize);
		return (new String(message)).trim();
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
