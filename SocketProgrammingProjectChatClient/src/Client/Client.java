package Client;

import java.io.*;
import java.net.*;

public class Client {
    DatagramSocket clientSocket;

    public Client (int portNumber) throws SocketException {
        clientSocket = new DatagramSocket(portNumber);
    }

    public void run () throws IOException {
        BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));

        while (true) {

            System.out.println("Please enter your username: ");
            String line = consoleReader.readLine();
            DatagramPacket usernamePacket = sendPacket(line);
            clientSocket.send(usernamePacket);

            DatagramPacket welcomePacket = receivePacket();
            clientSocket.receive(welcomePacket);
            System.out.println(new String(welcomePacket.getData()));

            DatagramPacket requestPacket = receivePacket();
            clientSocket.receive(requestPacket);
            System.out.println(new String(requestPacket.getData()));

            String username = consoleReader.readLine();
            DatagramPacket username2Packet = sendPacket(username);
            clientSocket.send(username2Packet);

            DatagramPacket connectionRequestPermission = receivePacket();
            clientSocket.receive(connectionRequestPermission);
            String serverInformation = new String(connectionRequestPermission.getData());
            String [] information = serverInformation.split(",");
            System.out.println(information[0] + information[1]);
            TCPConnection1(information[0]);

        }

    }

    private void TCPConnection1(String sourceIP) throws IOException {
        Socket clientSocket = new Socket(sourceIP, 16000);

        BufferedReader reader = new BufferedReader(
                new InputStreamReader(clientSocket.getInputStream()));
        PrintWriter writer = new PrintWriter(
                new OutputStreamWriter(clientSocket.getOutputStream()));

        BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Please write your name: ");
        String name = consoleReader.readLine();
        System.out.println("Please write your message: ");

        while (true) {
            String message = consoleReader.readLine();
            message = name + ":" + message;
            System.out.println(message);
            writer.println(message);
            writer.flush();

            message = reader.readLine();
            System.out.println(message);
        }

    }

    private DatagramPacket receivePacket() {
        byte[] buffer = new byte[1024];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

        return packet;
    }

    private DatagramPacket sendPacket(String line) throws UnknownHostException {
        DatagramPacket packet = new DatagramPacket(line.getBytes(), line.getBytes().length,
                InetAddress.getByName("localhost"), 20000);

        return packet;
    }

    public static void main(String[] args) {
        Client client = null;
        try {
            client = new Client(17000);
            client.run();
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
