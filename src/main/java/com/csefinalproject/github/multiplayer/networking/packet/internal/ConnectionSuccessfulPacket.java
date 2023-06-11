package com.csefinalproject.github.multiplayer.networking.packet.internal;

import com.csefinalproject.github.multiplayer.networking.IPeer;
import com.csefinalproject.github.multiplayer.networking.packet.Packet;
import com.csefinalproject.github.multiplayer.networking.packet.SerialIds;

public class ConnectionSuccessfulPacket extends Packet {
    static final long serialVersionUID = SerialIds.CONNECTION_SUCCESSFUL_PACKET;
    public ConnectionSuccessfulPacket(IPeer peer) {
        super(peer);
    }

}
