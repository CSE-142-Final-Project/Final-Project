package com.csefinalproject.github.multiplayer.networking;

import com.csefinalproject.github.multiplayer.networking.exceptions.ConnectionFailedException;
import com.csefinalproject.github.multiplayer.networking.exceptions.PacketDecodeError;
import com.csefinalproject.github.multiplayer.networking.packet.Packet;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.concurrent.atomic.AtomicBoolean;

public interface IPeer {
    int DEFAULT_TPS = 20;
    /**
     * Time in seconds that a peer waits for a response from the other peer
     */
    int DEFAULT_CONNECTION_TIMEOUT = 10;
    int DEFAULT_PACKET_SIZE = 256;
    int DEFAULT_KEEP_ALIVE_INTERVAL = 5;// 5 seconds
    int DEFAULT_KEEP_ALIVE_GRACE = 5;// 5 seconds of leeway on-top of the interval there supposed to be sent
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
}
