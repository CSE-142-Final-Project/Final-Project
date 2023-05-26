package com.csefinalproject.github.multiplayer.networking.packet;

import com.csefinalproject.github.multiplayer.networking.IPeer;

import java.io.Serializable;

public class Packet implements Serializable {
    final String ipOfSender;
    final int portOfSender;
    final long sendTime;
    public Packet(IPeer peerToSendFrom) {
        ipOfSender = peerToSendFrom.getIp();
        portOfSender = peerToSendFrom.getPort();
        sendTime = System.nanoTime();
    }
    public long getSendTime() {
        return 0L;
    }
    public String getIp() {
        return ipOfSender;
    }
    public int getPort() {
        return portOfSender;
    }

}
