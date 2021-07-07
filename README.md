# socket-channel-demo
 Client-Server Socket Channel Demo Application

Problem Statement
-----------------
- Create a client and a server
- Client uses a TCP  socket to connect to a server
- A server listens on the same host, port 5555
- On receiving a connection from a client, the server creates a channel 
- All further communication between that client and the server occurs over that channel
- The client sends a file name to the server
- The server transfers the file to the client
- When the communication is finished, the client closes the connection and the channel is destroyed.
- If a new client connects, a new channel is created.

	 
Solution Approach
-----------------
- A java based approach that uses core java libraries from java.net, java.nio packages to create Socket, SocketChannel, etc. 
- The same libraries are also used to send/receive data on the Client side and read/write operations on the Server side.
- java.lang.Thread is used for launching the Worker thread which handles the client request.
- Two simple runner classes are designed to test run the Server as well as Clients in a multi-threaded environment.

Components Overview
-------------------
1. Server
- Creates Server Socket Channel and binds the address (host: port) to the Socket.
- Waits indefinitely for the request from a client. This is achieved by configuring 'Blocking' as true.
- Once the Server accepts a request from the client, it opens a new channel and also creates a new thread (Worker) to work on the data requested through that channel.
2. Worker (Thread)
- One worker thread is created to serve requests from one client.
- It reads the file name from the channel as sent by the client.
- Gets the file with the name, same as the request from the server location.
- Writes the file to the channel.
3. Client
- Creates a Client Socket Channel by pointing to the same address (host: port) of the Server.
- Sends request (filename as string) to the server through the socket channel
- Receives the File from the Socket channel after the Server places the same there.
- Closes the channel once the file is received.

Testing
-------
1. Setup
- To simulate this scenario in the local Windows system, the file locations as well as file name patterns must be configured as follows:

	CLIENT_FILE_PATH_PATTERN = "C:\\work\\demo\\client\\%s";

	SERVER_FILE_PATH_PATTERN = "C:\\work\\demo\\server\\%s";

	FILE_NAME_PATTERN = "test%d.txt";

	CLIENT_NAME_PATTERN = "CLIENT-%d";


2. Run ServerRunner
- It creates a Server using the Host and Port as configured in Constants class and starts it.
- In this exercise: HOST = localhost, PORT = 5555

- ServerRunner Console:
------------------------------------------------------------------------------------------------------------

	Socket Channel Demo Server Started at localhost:5555...

------------------------------------------------------------------------------------------------------------
- More: This is done from the main() method in the exercise. However, it can also be started in a new Thread.


3. Run ClientRunner:
- It simulates the scenario where multiple clients are trying to establish communication to the server through the Socket channel at the same time.
- Hence it starts multiple threads configured as NUM_OF_CLIENTS in the Constants class. 
- Each client thread uses the same HOST and PORT (as configured) to create a Client where the Server is running. 
- Also the same Thread name is used as the Client name (numbered with the loop index).
- In this Exercise, NUM_OF_CLIENTS = 2;

- ClientRunner Console:
------------------------------------------------------------------------------------------------------------

	CLIENT-1 is ready to send request to localhost/127.0.0.1:5555 on Channel[1495510123]

	CLIENT-2 is ready to send request to localhost/127.0.0.1:5555 on Channel[2052942685]

	CLIENT-2 sent request to download file :: test2.txt on Channel[2052942685]

	CLIENT-1 sent request to download file :: test1.txt on Channel[1495510123]

------------------------------------------------------------------------------------------------------------

- After Client Runner is started, the Server starts getting messages from the client and also it writes the file to the channels.

- ServerRunner Console:
------------------------------------------------------------------------------------------------------------

	File name requested from client:: test1.txt

	File name requested from client:: test2.txt

	File Sent::C:\work\demo\server\test2.txt

	File Sent::C:\work\demo\server\test1.txt

------------------------------------------------------------------------------------------------------------

- Client Runner prints the outcome of receive method after the server sends the file to the channel.

- ClientRunner Console:
------------------------------------------------------------------------------------------------------------

	CLIENT-1 received the file:: C:\work\demo\client\test1.txt on Channel[1495510123]

	CLIENT-2 received the file:: C:\work\demo\client\test2.txt on Channel[2052942685]

	Channel[2052942685] is closed successfully.

	Channel[1495510123] is closed successfully.

------------------------------------------------------------------------------------------------------------

Observations:
-------------
- Check by increasing the NUM_OF_CLIENTS to see how same number of threads are getting created.
- Check for Channel id (channel object hash code) to see how the client (thread) uses one and same channel to send and receive data and finally closes it.

Summary and Next Steps:
-----------------------
- This exercise is a basic example of Client-Server Socket programming using Socket Channels and multiple threads. 
- The focus is more on:
	- how to establish the connection between the client and the server.
	- open channels and create threads to do read/write operations on the server-side.
	- open channels and do send/receive on the client-side.
- Further improvements:
	- Use of System.out.print() statements can be converted to use loggers like log4j or slf4j libraries.
	- Distribute the responsibilities like send/receive or read/write to different objects
	- Add JUnit test cases to test the unit level methods like send(), receive(), read() and write() etc.
	- Add integration test case to simulate Server and Client and how they integrate.
