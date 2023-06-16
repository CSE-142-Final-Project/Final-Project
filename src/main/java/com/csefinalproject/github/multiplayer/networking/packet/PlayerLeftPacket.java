package com.csefinalproject.github.multiplayer.networking.packet;

import com.csefinalproject.github.multiplayer.networking.IPeer;

/**
 * This packet is used to tell the clients that a player has left
 */
public class PlayerLeftPacket extends Packet {
    /**
     * For serialization
     */
    public static final long serialVersionUID = SerialIds.PLAYER_LEFT_PACKET;
    /**
     * The client id of the player who left
     */
    private final short clientId;
    /**
     * This constructor is used to create a new player left packet
     * @param peerToSendFrom the peer to send from
     * @param clientId the client id of the player who left
     */
    public PlayerLeftPacket(IPeer peerToSendFrom, short clientId) {
        super(peerToSendFrom);

        this.clientId = clientId;
    }

    /**
     * This method is used to get the client id
     * @return the client id
     */
    public short getClientId() {
        return clientId;
    }
}
