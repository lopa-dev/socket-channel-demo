package demo.socket.channel.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * The Server class with the start method to handle requests in multiple threads
 */
public class Server {
	private InetSocketAddress socketAddress;

	/**
	 * Server object constructor to set Socket Address to configured host and port.
	 * 
	 * @param host
	 * @param port
	 * @throws IOException
	 */
	public Server(String host, int port) throws IOException {
		socketAddress = new InetSocketAddress(host, port);
	}

	/**
	 * Create server channel and waits for request. Once request comes from client
	 * it starts a new worker thread to handle the request
	 * 
	 * @throws IOException
	 */
	public void start() throws IOException {
		ServerSocketChannel serverChannel = ServerSocketChannel.open();
		// Blocking is set to true so that the process run is blocked until client
		// requests for information
		serverChannel.configureBlocking(true);
		serverChannel.socket().bind(socketAddress);

		System.out.println("Socket Channel Demo Server Started at " + socketAddress.getHostName() + ":"
				+ socketAddress.getPort() + "...");
		SocketChannel channel = null;
		// Wait for client request through infinite loop
		while (true) {
			channel = serverChannel.accept();
			new Worker(channel).start();
		}
	}
}