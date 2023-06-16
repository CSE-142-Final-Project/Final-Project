package com.csefinalproject.github.multiplayer.networking.packet.internal;

import com.csefinalproject.github.multiplayer.networking.IPeer;
import com.csefinalproject.github.multiplayer.networking.packet.Packet;
import com.csefinalproject.github.multiplayer.networking.packet.SerialIds;

/**
 * This packet is used to keep the connection between the server and client alive
 */
public class KeepAlivePacket extends Packet {
    /**
     * For serialization
     */
    static final long serialVersionUID = SerialIds.KEEP_ALIVE_PACKET;

    /**
     * This constructor is used to create a new keep alive packet
     * @param peer the peer to send from
     */
    public KeepAlivePacket(IPeer peer) {
        super(peer);
    }

}
