package demo.socket.channel.client;

import static demo.socket.channel.config.Constants.CLIENT_NAME_PATTERN;
import static demo.socket.channel.config.Constants.FILE_NAME_PATTERN;
import static demo.socket.channel.config.Constants.HOST;
import static demo.socket.channel.config.Constants.NUM_OF_CLIENTS;
import static demo.socket.channel.config.Constants.PORT;

/**
 * Client Runner Class simulates the scenario where multiple clients are trying
 * to establish communication to server through Socket channel at the same time.
 * Hence it starts NUM_OF_CLIENTS threads as configured in Constants class. Each
 * client thread takes the same HOST and PORT (as configured) to create a Client
 * where the Server is running. Also the same Thread name is used as the Client
 * name (numbered with the loop index).
 *
 */
public class ClientRunner {

	public static void main(String[] args) {
		for (int i = 1; i <= NUM_OF_CLIENTS; i++) {
			new Thread(clientRunner(i), String.format(CLIENT_NAME_PATTERN, i)).start();
		}
	}

	private static Runnable clientRunner(int num) {
		String fileName = String.format(FILE_NAME_PATTERN, num);
		Runnable clientRunner = new Runnable() {
			@Override
			public void run() {
				// Creates client
				Client client = new Client(HOST, PORT);
				// Sends file name as request to socket channel
				client.send(fileName);

				// Pause for 5 seconds to realize the operations.
				// This can be removed any time.
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				// receives file from socket channel and downloads to client location
				client.receive(fileName);
				// Close the channel
				client.close();
			}
		};
		return clientRunner;
	}
}