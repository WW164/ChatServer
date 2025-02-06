package Server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;

public class Server {

    DatagramSocket serverSocket;
    ArrayList<User> allUsers = new ArrayList<User>();

    public Server() throws SocketException {
        serverSocket = new DatagramSocket(20000);
    }

    public void run () throws IOException {
        while (true) {

            System.out.println("Server starts listening");
            DatagramPacket firstPacket = receivePacket();
            serverSocket.receive(firstPacket);

            User newUser = new User();
            newUser = registration(firstPacket, newUser);
            System.out.println(newUser.username + " connected");
            String response = "Welcome " + newUser.username;
            DatagramPacket welcomePacket = sendPacket(newUser, response);
            serverSocket.send(welcomePacket);

            String request = "Who would you like to chat with? ";
            DatagramPacket requestPacket = sendPacket(newUser, request);
            serverSocket.send(requestPacket);

            DatagramPacket responsePacket = receivePacket();
            serverSocket.receive(responsePacket);

            String responseUsername = new String(responsePacket.getData());
            System.out.println(newUser.username + " requested to connect " + responseUsername);

            if (responseUsername.startsWith("parinazMB")) {
                String connection = "localhost, 16000, ";
                DatagramPacket connectionInformationPacket = sendPacket(newUser, connection);
                serverSocket.send(connectionInformationPacket);
                System.out.println("Information sent successfully.");

            }

            else if(!checkRegistration(responseUsername)) {
                String connection = "Connection request denied.";
                DatagramPacket connectionInformationPacket = sendPacket(newUser, connection);
                serverSocket.send(connectionInformationPacket);
                System.out.println(connection);
            }

        }
    }

    public DatagramPacket receivePacket() {

        byte[] buffer = new byte[1024];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

        return packet;
    }

    private DatagramPacket sendPacket(User user, String response) {
        DatagramPacket packet = new DatagramPacket(response.getBytes(),
                response.getBytes().length,
                user.sourceIP,
                user.sourcePort);
        return packet;
    }

    private User registration(DatagramPacket packet, User user) {
        user.setPacket(packet);
        user.setUsername();
        user.setSourceIP();
        user.setSourcePort();

        allUsers.add(user);

        return user;

    }

    boolean isRegistered;
    private boolean checkRegistration(String username2) {
        for(int i = 0; i < allUsers.size(); i++) {
            if (allUsers.get(i).username.equals(username2))
                isRegistered = true;
            else
                isRegistered = false;

        }

        return isRegistered;

    }

    public static void main(String[] args) {
        Server server = null;
        try {
            server = new Server();
            server.run();
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
