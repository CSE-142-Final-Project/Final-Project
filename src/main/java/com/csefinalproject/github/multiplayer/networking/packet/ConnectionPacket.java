package com.csefinalproject.github.multiplayer.networking.packet;

import com.csefinalproject.github.multiplayer.networking.IPeer;

public class ConnectionPacket extends Packet {
    static final long serialVersionUID = SerialIds.CONNECTION_PACKET;
    String username;
    public ConnectionPacket(IPeer peer, String username) {
        super(peer);
        this.username = username;
    }
    public String getUsername() {
        return username;
    }
}
