package com.csefinalproject.github.multiplayer.networking.packet.internal;

import com.csefinalproject.github.multiplayer.networking.IPeer;
import com.csefinalproject.github.multiplayer.networking.packet.Packet;
import com.csefinalproject.github.multiplayer.networking.server.ClientData;

public class DummyTimeoutPacket extends Packet {

    final ClientData data;
    public DummyTimeoutPacket(IPeer peerToSendFrom, ClientData data) {
        super(peerToSendFrom);
        this.data = data;
    }

    public ClientData getData() {
        return data;
    }
}
