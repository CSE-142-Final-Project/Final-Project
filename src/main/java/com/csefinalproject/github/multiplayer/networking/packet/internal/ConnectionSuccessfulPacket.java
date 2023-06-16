package com.csefinalproject.github.multiplayer.networking.packet.internal;

import com.csefinalproject.github.multiplayer.networking.IPeer;
import com.csefinalproject.github.multiplayer.networking.packet.Packet;
import com.csefinalproject.github.multiplayer.networking.packet.SerialIds;

/**
 * This packet is sent from the server to the client to tell them that they have successfully connected
 */
public class ConnectionSuccessfulPacket extends Packet {
    /**
     * For serialization
     */
    static final long serialVersionUID = SerialIds.CONNECTION_SUCCESSFUL_PACKET;

    /**
     * This constructor is used to create a new connection successful packet
     * @param peer the peer to send from
     */
    public ConnectionSuccessfulPacket(IPeer peer) {
        super(peer);
    }

}
