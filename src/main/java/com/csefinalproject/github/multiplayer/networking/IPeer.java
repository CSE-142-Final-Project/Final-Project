package com.csefinalproject.github.multiplayer.networking;

import com.csefinalproject.github.multiplayer.networking.packet.Packet;

/**
 * Just an interface for any peer on the network
 */
public interface IPeer {
    int DEFAULT_TPS = 20;
    /**
     * Time in seconds that a peer waits for a response from the other peer
     */
    int DEFAULT_CONNECTION_TIMEOUT = 10;
    int DEFAULT_PACKET_SIZE = 512;
    int DEFAULT_KEEP_ALIVE_INTERVAL = 2;// 5 seconds
    int DEFAULT_KEEP_ALIVE_GRACE = 2;// 5 seconds of leeway on-top of the interval there supposed to be sent

    Packet getNextPacket();
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
