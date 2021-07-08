package demo.socket.channel.client;

import static demo.socket.channel.config.Constants.CLIENT_FILE_PATH_PATTERN;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.EnumSet;

/**
 * Client class connects to the socket channel to send request (file name in
 * this case) and then receives the file from the same channel to which the
 * Server Worker has already written the file content.
 *
 */
public class Client {
	private Integer channelId;
	private SocketChannel channel;
	private String clientName;
	private InetSocketAddress address;

	public Client(String host, int port) {
		this.address = new InetSocketAddress(host, port);
		try {
			this.channel = SocketChannel.open(address);
			this.clientName = Thread.currentThread().getName();
			this.channelId = channel.hashCode();
			System.out.println(clientName + " is ready to send request to " + channel.getRemoteAddress()
					+ " on Channel[" + this.channelId + "]");
		} catch (IOException e) {
			System.out.println("Exception while creating a client socket channel");
		}

	}

	/**
	 * When called this method sends the file name to the server by writing it to
	 * the channel
	 * 
	 * @param fileName
	 */
	public void send(String fileName) {
		byte[] messageData = new String(fileName).getBytes();
		ByteBuffer buffer = ByteBuffer.wrap(messageData);
		try {
			if (this.channel != null) {
				this.channel.write(buffer);
				System.out.println(clientName + " sent request to download file :: " + fileName + " on Channel["
						+ this.channelId + "]");
				buffer.clear();
			}
		} catch (IOException e) {
			System.out.println(clientName + "is unable to send the file name:: " + fileName + " on Channel["
					+ this.channelId + "]");
		}
	}

	/**
	 * When called this method receives the file from the server by reading it from
	 * the channel
	 * 
	 * @param fileName
	 */
	public void receive(String fileName) {
		if (this.channel != null) {
			String filePath = String.format(CLIENT_FILE_PATH_PATTERN, fileName);
			Path path = Paths.get(filePath);
			try {
				ByteBuffer buffer = ByteBuffer.allocate(1024);
				int numRead = this.channel.read(buffer);
				if (numRead > 0) {
					FileChannel fileChannel = FileChannel.open(path, EnumSet.of(StandardOpenOption.CREATE,
							StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE));
					do {
						buffer.flip();
						fileChannel.write(buffer);
						buffer.clear();
					} while (this.channel.read(buffer) > 0);
					System.out.println(this.clientName + " received the file:: " + filePath + " on Channel["
							+ this.channelId + "]");
				} else {
					System.out.println(this.clientName + " is unable to receive the file:: " + filePath + " on Channel["
							+ this.channelId + "] from Server");
				}
			} catch (Exception e) {
				System.out.println(this.clientName + " is unable to receive the file:: " + filePath + " on Channel["
						+ this.channelId + "]");
			}
		}
	}

	/**
	 * Closes the Socket Channel
	 */
	public void close() {
		if (this.channel != null) {
			try {
				this.channel.close();
				System.out.println(this.clientName + "::Channel[" + this.channelId + "] is closed successfully.");
			} catch (IOException e) {
				System.out.println(this.clientName + "::Unable to close Channel[" + this.channelId + "]");
			}
		}
	}
}