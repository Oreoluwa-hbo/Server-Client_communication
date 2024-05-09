import java.io.*;
import java.net.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class  myfileserver{
    private static final int SERVER_PORT = 11111;
    private static final int MAX_THREADS = 10;
    private static final AtomicInteger totalRequests = new AtomicInteger(0); // Total number of requests
    private static final AtomicInteger successfulRequests = new AtomicInteger(0); // Number of successful requests
    private static final Object lock = new Object(); // Lock for synchronization

    public static void main(String[] args) {
        // Create a fixed pool of worker threads
        ExecutorService threadPool = Executors.newFixedThreadPool(MAX_THREADS);

        try (ServerSocket serverSocket = new ServerSocket(SERVER_PORT)) {
            System.out.println("Server started and listening on port " + SERVER_PORT);

            while (true) {
                // Listen for incoming client connections
                Socket clientSocket = serverSocket.accept();
                InetAddress clientAddress = clientSocket.getInetAddress();

                // Create a new worker thread to handle the client request
                Runnable worker = new ClientHandler(clientSocket);
                threadPool.execute(worker);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Create a ClientHandler class to handle each client request
    static class ClientHandler implements Runnable {
        private Socket clientSocket;

        public ClientHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        @Override
        public void run() {
            try (
                    // Create input and output streams for communication with the client
                    BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            ) {
                // Read the requested filename from the client
                String filename = in.readLine();

                synchronized (lock) {
                    System.out.println("REQ " + totalRequests.incrementAndGet() + ": File " + filename + " requested from " + clientSocket.getInetAddress().getHostAddress());
                }
                // Check if the file exists in the server's directory
                File file = new File(filename);
                if (file.exists() && !file.isDirectory()) {
                    out.println("File found");

                    // Send the file to the client
                    try (BufferedReader fileReader = new BufferedReader(new FileReader(file))) {
                        String line;
                        while ((line = fileReader.readLine()) != null) {
                            out.println(line);
                        }
                    }

                    // Display stats and other outputs
                    synchronized (lock) {
                        System.out.println("REQ " + totalRequests.get() + ": " + (file.exists() ? "Successful" : "Not Successful"));
                        System.out.println("REQ " + totalRequests.get() + ": Total successful requests so far = " + successfulRequests.get());
                        System.out.println("REQ " + totalRequests.get() + ": File transfer complete");
                    }
                } else {
                    synchronized (lock) {
                        System.out.println("REQ " + totalRequests.get() + ": " + (file.exists() ? "Successful" : "Not Successful"));
                        System.out.println("REQ " + totalRequests.get() + ": Total successful requests so far = " + successfulRequests.get());
                    }
                }

                // Send the request and success stats to the client
                String statistics;
                synchronized (lock) {
                    statistics = "Server handled " + totalRequests.get() + " requests, " + successfulRequests.get() + " requests were successful";
                }
                out.println(statistics);

                // Close the client socket
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
