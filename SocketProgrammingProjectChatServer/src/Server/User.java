package Server;

import java.net.DatagramPacket;
import java.net.InetAddress;

public class User {
    DatagramPacket packet;
    String username;
    InetAddress sourceIP;
    int sourcePort;

    public void setPacket(DatagramPacket packet) {

        this.packet = packet;
    }

    public void setUsername() {

        this.username = new String(packet.getData());
    }

    public void setSourceIP() {

        this.sourceIP = packet.getAddress();
    }

    public void setSourcePort() {
        this.sourcePort = packet.getPort();
    }
}
