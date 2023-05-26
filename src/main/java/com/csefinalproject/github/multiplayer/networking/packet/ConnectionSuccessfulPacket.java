package com.csefinalproject.github.multiplayer.networking.packet;

import com.csefinalproject.github.multiplayer.networking.IPeer;

public class ConnectionSuccessfulPacket extends Packet{
    static final long serialVersionUID = SerialIds.CONNECTION_SUCCESSFUL_PACKET;
    public ConnectionSuccessfulPacket(IPeer peer) {
        super(peer);
    }

}
