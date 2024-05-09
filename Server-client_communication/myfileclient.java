import java.io.*;
import java.net.*;

public class myfileclient{
    public static void main(String[] args) {
        if (args.length != 3) {
            System.out.println("Enter: java myfileclient <server_IP> <server_port> <filename>");
            return;
        }

        String serverIP = args[0];
        int serverPort = Integer.parseInt(args[1]);
        String filename = args[2];

        try (Socket socket = new Socket(serverIP, serverPort)) {
            System.out.println("Connected to server at " + serverIP + ":" + serverPort);

            // Send a request to the server with the specified filename
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println(filename);

            // Read the response from the server
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String serverResponse = in.readLine();


            if (serverResponse.equals("File found")) {
                System.out.println("File " + filename + " found at server");
                // Receive and display the stats
                String statistics = in.readLine();
                System.out.println(statistics);

                // Receive and save the file
                try (FileWriter fileWriter = new FileWriter(filename)) {
                    String line;
                    while ((line = in.readLine()) != null && !line.startsWith("Server handled")) {
                        fileWriter.write(line + "\n");
                    }
                }

                System.out.println("Downloading file " + filename);
                System.out.println("Download complete");

            } else {
                System.out.println("File " + filename + " not found at server");
                String statistics = in.readLine();
                System.out.println(statistics);

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
