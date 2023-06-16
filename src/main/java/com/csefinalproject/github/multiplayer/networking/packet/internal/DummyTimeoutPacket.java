package com.csefinalproject.github.multiplayer.networking.packet.internal;

import com.csefinalproject.github.multiplayer.networking.IPeer;
import com.csefinalproject.github.multiplayer.networking.packet.Packet;
import com.csefinalproject.github.multiplayer.networking.server.ClientData;

/**
 * This packet should never be sent over the network. It is added as a packet the server recieved when a client times out
 */
public class DummyTimeoutPacket extends Packet {
    final ClientData data;

    /**
     * This constructor is used to create a new dummy timeout packet
     * @param peerToSendFrom the peer to send from
     * @param data the client data
     */
    public DummyTimeoutPacket(IPeer peerToSendFrom, ClientData data) {
        super(peerToSendFrom);
        this.data = data;
    }

    /**
     * This method is used to get the client data
     * @return the client data
     */
    public ClientData getData() {
        return data;
    }
}
