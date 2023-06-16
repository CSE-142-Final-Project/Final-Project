package com.csefinalproject.github.multiplayer.networking.packet;

import com.csefinalproject.github.multiplayer.networking.IPeer;

/**
 * This packet is used to send chat messages
 */
public class ChatPacket extends Packet {
    /**
     * For serialization
     */
    static final long serialVersionUID = SerialIds.CHAT_PACKET;
    final String message;

    /**
     * This constructor is used to create a new chat packet
     * @param peerToSendFrom the peer to send from
     * @param message the message to send
     */
    public ChatPacket(IPeer peerToSendFrom, String message) {
        super(peerToSendFrom);
        this.message = message;
    }

    /**
     * This method is used to get the message
     * @return the message
     */
    public String getMessage() {
        return message;
    }
}
