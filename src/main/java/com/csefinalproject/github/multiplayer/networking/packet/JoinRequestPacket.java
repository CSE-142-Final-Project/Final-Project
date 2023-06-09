package com.csefinalproject.github.multiplayer.networking.packet;

import com.csefinalproject.github.multiplayer.networking.IPeer;

import java.io.Serial;

/**
 * This packet is used to tell the clients that a player has joined
 */
public class JoinRequestPacket extends Packet {
    /**
     * Serial ID
     */
    @Serial
    private static final long serialVersionUID = SerialIds.JOIN_REQUEST_PACKET;

    /**
     * This constructor is used to create a new player joined packet
     * @param peerToSendFrom the peer to send from
     */
    public JoinRequestPacket(IPeer peerToSendFrom) {
        super(peerToSendFrom);
    }
}
