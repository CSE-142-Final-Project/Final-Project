package com.csefinalproject.github.multiplayer.networking.packet;

public class ConnectionPacket extends Packet{
    static final long serialVersionUID = SerialIds.CONNECTION_PACKET;
    String username;
    public ConnectionPacket(String ip, short port, String username) {
        super(ip, port);
        this.username = username;
    }
    public String getUsername() {
        return username;
    }
}
