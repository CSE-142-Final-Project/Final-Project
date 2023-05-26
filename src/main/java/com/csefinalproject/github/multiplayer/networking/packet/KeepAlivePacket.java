package com.csefinalproject.github.multiplayer.networking.packet;

import com.csefinalproject.github.multiplayer.networking.IPeer;

public class KeepAlivePacket extends Packet{
    static final long serialVersionUID = SerialIds.KEEP_ALIVE_PACKET;
    public KeepAlivePacket(IPeer peer) {
        super(peer);
    }

}
