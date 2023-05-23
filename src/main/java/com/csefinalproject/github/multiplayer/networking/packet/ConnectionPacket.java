package com.csefinalproject.github.multiplayer.networking.packet;

public class ConnectionPacket extends Packet{
    String username;
    public ConnectionPacket(String ip, short port, String username) {
        super(ip, port);
        this.username = username;
    }
    public String getUsername() {
        return username;
    }
}
