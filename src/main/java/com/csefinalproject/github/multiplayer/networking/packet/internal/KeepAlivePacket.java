package com.csefinalproject.github.multiplayer.networking.packet.internal;

import com.csefinalproject.github.multiplayer.networking.IPeer;
import com.csefinalproject.github.multiplayer.networking.packet.Packet;
import com.csefinalproject.github.multiplayer.networking.packet.SerialIds;

public class KeepAlivePacket extends Packet {
    static final long serialVersionUID = SerialIds.KEEP_ALIVE_PACKET;
    public KeepAlivePacket(IPeer peer) {
        super(peer);
    }

}
