package Client;

import java.io.*;
import java.net.*;

public class Client {
    ServerSocket serverSocket;

    public Client (int portNumber) throws IOException {
        serverSocket = new ServerSocket(portNumber);
    }

    public void run () throws IOException {
        BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));

        while (true) {

            System.out.println("Waiting...");
            Socket connectionSocket = serverSocket.accept();
            System.out.println("Chat started.");

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connectionSocket.getInputStream()));
            PrintWriter writer = new PrintWriter(
                    new OutputStreamWriter(connectionSocket.getOutputStream()));

            while (true) {

                String message = reader.readLine();
                System.out.println(message);
                System.out.println("parinazMB: ");
                message = consoleReader.readLine();
                message = "parinazMB: " + message;
                writer.println(message);
                writer.flush();

            }

        }

    }

    public static void main(String[] args) {
        Client client = null;
        try {
            client = new Client(16000);
            client.run();
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
