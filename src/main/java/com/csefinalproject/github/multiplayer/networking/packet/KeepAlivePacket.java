package com.csefinalproject.github.multiplayer.networking.packet;

public class KeepAlivePacket extends Packet{
    static final long serialVersionUID = SerialIds.KEEP_ALIVE_PACKET;
    public KeepAlivePacket(String ip, short port) {
        super(ip, port);
    }

}
