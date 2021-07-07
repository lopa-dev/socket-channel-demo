package demo.socket.channel.server;

import static demo.socket.channel.config.Constants.HOST;
import static demo.socket.channel.config.Constants.PORT;

/**
 * This class uses the main method to start the Socket Channel Server on HOST
 * and PORT as configured in Constants class
 * 
 */
public class ServerRunner {
	/**
	 * Starts the server by constructing the Server Object.
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		new Server(HOST, PORT).start();
	}
}
