package com.csefinalproject.github.multiplayer.networking.packet;

import com.csefinalproject.github.multiplayer.networking.IPeer;

public class ChatPacket extends Packet {
    static final long serialVersionUID = SerialIds.CHAT_PACKET;
    final String message;

    public ChatPacket(IPeer peerToSendFrom, String message) {
        super(peerToSendFrom);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
