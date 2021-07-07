package demo.socket.channel.server;

import static demo.socket.channel.config.Constants.SERVER_FILE_PATH_PATTERN;

import java.net.Socket;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Worker Thread class This reads data (file name as string) from the channel
 * and writes the processed data (actual file) to the same channel
 *
 */
public class Worker extends Thread {
	private final SocketChannel channel;
	private String data;

	public Worker(SocketChannel channel) {
		this.channel = channel;
	}

	@Override
	public void run() {
		read();
		write();
		try {
			this.channel.close();
		} catch (Exception e) {
			System.out.println("Exception while closing the channel");
		}
	}

	/**
	 * Reads data from channel
	 * 
	 * @return fileName
	 */
	private void read() {
		try {
			ByteBuffer buffer = ByteBuffer.allocate(1024);
			int numRead = -1;
			numRead = this.channel.read(buffer);

			if (numRead == -1) {
				Socket socket = this.channel.socket();
				SocketAddress remoteAddr = socket.getRemoteSocketAddress();
				System.out.println("Connection closed by client: " + remoteAddr);
				this.channel.close();
			}

			byte[] requestData = new byte[numRead];
			System.arraycopy(buffer.array(), 0, requestData, 0, numRead);
			this.data = new String(requestData);
			System.out.println("File name requested from client:: " + data);
		} catch (Exception e) {
			System.out.println("Exception while reading the file name from client");
		}
	}

	/**
	 * Gets the file from Server path and then writes it to the channel
	 * 
	 * @param fileName
	 */
	private void write() {
		String filePath = String.format(SERVER_FILE_PATH_PATTERN, this.data);
		Path path = Paths.get(filePath);
		try (FileChannel fileChannel = FileChannel.open(path)) {
			ByteBuffer buffer = ByteBuffer.allocate(1024);
			while (fileChannel.read(buffer) > 0) {
				buffer.flip();
				this.channel.write(buffer);
				buffer.clear();
			}
			System.out.println("File Sent::" + filePath);
		} catch (Exception e) {
			System.out.println("Server is unable to locate the requested file:: " + filePath);
		}
	}
}