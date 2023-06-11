package com.csefinalproject.github.multiplayer.networking.packet;

import com.csefinalproject.github.multiplayer.networking.IPeer;

public class PlayerJoinedPacket extends Packet {
    public static final long serialVersionUID = SerialIds.PLAYER_JOINED_PACKET;
    final String username;
    final short clientId;

    public PlayerJoinedPacket(IPeer peerToSendFrom, String username, short clientId) {
        super(peerToSendFrom);

        this.username = username;
        this.clientId = clientId;
    }

    public String getUsername() {
        return username;
    }

    public short getClientId() {
        return clientId;
    }
}
