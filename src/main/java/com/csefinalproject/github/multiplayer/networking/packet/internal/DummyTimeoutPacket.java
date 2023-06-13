package com.csefinalproject.github.multiplayer.networking.packet.internal;

import com.csefinalproject.github.multiplayer.networking.IPeer;
import com.csefinalproject.github.multiplayer.networking.packet.Packet;

public class DummyTimeoutPacket extends Packet {

    final int clientId;
    public DummyTimeoutPacket(IPeer peerToSendFrom, int clientId) {
        super(peerToSendFrom);
        this.clientId = clientId;
    }

    public int getClientId() {
        return clientId;
    }
}
