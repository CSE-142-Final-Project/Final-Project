package com.csefinalproject.github.multiplayer.behaviour.client;

import com.csefinalproject.github.multiplayer.networking.NetworkEventManager;

public class ClientPacketHandler {
    ClientManager clientManager;
    NetworkEventManager networkEventManager;
    public ClientPacketHandler(ClientManager clientManager, NetworkEventManager networkEventManager) {
        this.clientManager = clientManager;
        this.networkEventManager = networkEventManager;
    }
    public void startHandling() {

    }
}
