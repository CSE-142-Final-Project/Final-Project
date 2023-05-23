package com.csefinalproject.github.multiplayer.networking;

import com.csefinalproject.github.multiplayer.networking.packet.Packet;

public class Server implements IPeer {
    public void start(short port) {

    }
    public void stop() {

    }
    public void send(Packet packet, short client) {

    }
    public void broadcast(Packet packet) {

    }
    public ClientData[] getClientData() {
        return null;
    }
    public Packet getNextPacket() {
        return null;
    }
    public boolean hasNextPacket() {
        return false;
    }
}
