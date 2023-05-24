package com.csefinalproject.github.multiplayer.networking.packet;

import java.io.Serializable;

public class Packet implements Serializable {

    public Packet(String ip, short port) {

    }
    public long getSendTime() {
        return 0L;
    }
    public String getIp() {
        return null;
    }
    public short getPort() {
        return 0;
    }

}
