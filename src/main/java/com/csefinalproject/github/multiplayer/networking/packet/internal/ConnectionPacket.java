package com.csefinalproject.github.multiplayer.networking.packet.internal;

import com.csefinalproject.github.multiplayer.networking.IPeer;
import com.csefinalproject.github.multiplayer.networking.packet.Packet;
import com.csefinalproject.github.multiplayer.networking.packet.SerialIds;

import java.io.Serial;

/**
 * This packet is sent from a client to a server to request a connection
 */
public class ConnectionPacket extends Packet {
    /**
     * For serialization
     */
    @Serial
    private static final long serialVersionUID = SerialIds.CONNECTION_PACKET;
    private final String username;
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
