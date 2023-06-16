package com.csefinalproject.github.multiplayer.networking.packet;

import com.csefinalproject.github.multiplayer.networking.IPeer;

import java.io.Serializable;

/**
 * This is the base class for all packets
 */
public class Packet implements Serializable {
    /**
     * For serialization
     */
    final String ipOfSender;
    /**
     * The port of the sender
     */
    final int portOfSender;
    /**
     * The time that this packet was sent
     */
    final long sendTime;

    /**
     * This constructor is used to create a new packet
     * @param peerToSendFrom the peer to send from
     */
    public Packet(IPeer peerToSendFrom) {
        ipOfSender = peerToSendFrom.getIp();
        portOfSender = peerToSendFrom.getPort();
        sendTime = System.nanoTime();
    }

    /**
     * This method is used to get the ip of the sender
     * @return the ip of the sender
     */
    public String getIp() {
        return ipOfSender;
    }

    /**
     * This method is used to get the port of the sender
     * @return the port of the sender
     */
    public int getPort() {
        return portOfSender;
    }

}
