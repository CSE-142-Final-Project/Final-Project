package com.csefinalproject.github.multiplayer.networking;

import com.csefinalproject.github.multiplayer.networking.exceptions.ConnectionFailedException;
import com.csefinalproject.github.multiplayer.networking.packet.Packet;

public class Client implements IPeer{

    public void connect(String ip, short port) throws ConnectionFailedException {

    }
    public ClientData[] clientData() {
        return null;
    }


    public void disconnect() {

    }

    public void sendPacket(Packet packet) {

    }

    public double lastKeepAlivePacketTime() {
        return 0;
    }

    public Packet getNextPacket() {
        return null;
    }
    public boolean hasNextPacket() {
        return false;
    }

}



