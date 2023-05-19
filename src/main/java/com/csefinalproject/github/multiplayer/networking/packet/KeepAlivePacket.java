package com.csefinalproject.github.multiplayer.networking.packet;

public class KeepAlivePacket extends Packet{

    public KeepAlivePacket(String ip, short port) {
        super(ip, port);
    }
}
