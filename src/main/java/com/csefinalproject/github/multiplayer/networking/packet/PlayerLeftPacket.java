package com.csefinalproject.github.multiplayer.networking.packet;

import com.csefinalproject.github.multiplayer.networking.IPeer;

public class PlayerLeftPacket extends Packet {
    public static final long serialVersionUID = SerialIds.PLAYER_LEFT_PACKET;
    final short clientId;

    public PlayerLeftPacket(IPeer peerToSendFrom, short clientId) {
        super(peerToSendFrom);

        this.clientId = clientId;
    }

    public short getClientId() {
        return clientId;
    }
}
