package com.csefinalproject.github.multiplayer.networking.packet;

public class ConnectionSuccessfulPacket extends Packet{
    public ConnectionSuccessfulPacket(String ip, short port) {
        super(ip, port);
    }

}
