package com.csefinalproject.github.multiplayer.networking;

import com.csefinalproject.github.multiplayer.networking.packet.Packet;

/**
 * Just an interface for any peer on the network
 */
public interface IPeer {
    /**
     * The number of times per second we should check for keepalive stuff
     */
    int DEFAULT_TPS = 20;
    /**
     * Time in seconds that a peer waits for a response from the other peer
     */
    int DEFAULT_CONNECTION_TIMEOUT = 10;
    /**
     * Default max packet size, in bytes
     */
    int DEFAULT_PACKET_SIZE = 512;
    /**
     * Default amount off time to wait in between sending keepalive packets
     */
    int DEFAULT_KEEP_ALIVE_INTERVAL = 2;
    /**
     * The amount of time that peers have to respond to keep alive packets
     */
    int DEFAULT_KEEP_ALIVE_GRACE = 2;

    /**
     * Get the oldest unprocessed packet that this peer has received (FIFO)
     * This method should return null if there are no packets
     * @return the next packet
     */
    Packet getNextPacket();

    /**
     * If the peer currently has a packet that needs to be processed
     * @return
     */
    boolean hasNextPacket();

    /**
     * @return the current port that THIS peer has opn
     */
    int getPort();

    /**
     * @return our ip
     */
    String getIp();
    /**
     * @return weather or not we are actively listening for packets
     */
    boolean isActive();
}
