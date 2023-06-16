package com.csefinalproject.github.multiplayer.networking.packet.internal;

import com.csefinalproject.github.multiplayer.networking.IPeer;
import com.csefinalproject.github.multiplayer.networking.packet.Packet;
import com.csefinalproject.github.multiplayer.networking.packet.SerialIds;

/**
 * This packet is sent from a client to a server to request a connection
 */
public class ConnectionPacket extends Packet {
    /**
     * For serialization
     */
    static final long serialVersionUID = SerialIds.CONNECTION_PACKET;
    String username;
    /**
     * This constructor is used to create a new connection packet
     * @param peer the peer to send from
     * @param username the username of the client
     */
    public ConnectionPacket(IPeer peer, String username) {
        super(peer);
        this.username = username;
    }
    /**
     * This method is used to get the username
     * @return the username
     */
    public String getUsername() {
        return username;
    }
}
