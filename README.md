# File Server and Client Implementation
## File Descriptions:

1. myfileserver.java:
   - This Java file contains the implementation of the file server.
   - It listens on a specified port (default is 11111) for incoming client connections.
   - The server handles multiple client requests concurrently using a fixed pool of worker threads.
   - It responds to client requests to either retrieve files or check if a file exists on the server.
   - File transfer is performed and statistics about requests are displayed.

2. myfileclient.java:
   - This Java file contains the implementation of the file client.
   - It connects to the server's IP address and port to initiate communication.
   - The client sends requests to the server to retrieve files or check file availability.
   - It displays server responses, such as whether a requested file exists, and downloads files when available.

3. PROTOCOL.TXT: This file is used to describe the messages used for communication between the client and server including their formats and meanings.

## Usage Instructions:
### Compiling:
To compile the server and client programs, you can use the `javac` command:

javac myfileserver.java
javac myfileclient.java

Running the Server:
To start the server, use the following command:
java myfileserver

Running the Client:
To run the client and interact with the server, use the following command:
java myfileclient <server_IP> <server_port> <filename>
Replace <server_IP>, <server_port>, and <filename> with the appropriate values
