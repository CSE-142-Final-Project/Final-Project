package com.csefinalproject.github.multiplayer.networking.packet.internal;

import com.csefinalproject.github.multiplayer.networking.IPeer;
import com.csefinalproject.github.multiplayer.networking.packet.Packet;
import com.csefinalproject.github.multiplayer.networking.packet.SerialIds;

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
