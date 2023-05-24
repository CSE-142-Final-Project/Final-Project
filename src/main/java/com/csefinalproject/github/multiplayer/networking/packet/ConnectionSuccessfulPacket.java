package com.csefinalproject.github.multiplayer.networking.packet;

public class ConnectionSuccessfulPacket extends Packet{
    static final long serialVersionUID = SerialIds.CONNECTION_SUCCESSFUL_PACKET;
    public ConnectionSuccessfulPacket(String ip, short port) {
        super(ip, port);
    }

}
