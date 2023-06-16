package com.csefinalproject.github.multiplayer.networking.packet;

import com.csefinalproject.github.multiplayer.networking.IPeer;

/**
 * This packet is used to tell the clients that a player has joined
 */
public class PlayerJoinedPacket extends Packet {
    /**
     * For serialization
     */
    public static final long serialVersionUID = SerialIds.PLAYER_JOINED_PACKET;
    /**
     * The username of the player who joined
     */
    final String username;
    /**
     * The client id of the player who joined
     */
    final short clientId;

    /**
     * This constructor is used to create a new player joined packet
     * @param peerToSendFrom the peer to send from
     * @param username the username of the player who joined
     * @param clientId the client id of the player who joined
     */
    public PlayerJoinedPacket(IPeer peerToSendFrom, String username, short clientId) {
        super(peerToSendFrom);

        this.username = username;
        this.clientId = clientId;
    }

    /**
     * This method is used to get the username
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * This method is used to get the client id
     * @return the client id
     */
    public short getClientId() {
        return clientId;
    }
}
