package com.csefinalproject.github.multiplayer.networking.packet;

import com.csefinalproject.github.multiplayer.networking.IPeer;

public class JoinRequestPacket extends Packet {
    public static final long serialVersionUID = SerialIds.JOIN_REQUEST_PACKET;

    public JoinRequestPacket(IPeer peerToSendFrom) {
        super(peerToSendFrom);
    }
}
