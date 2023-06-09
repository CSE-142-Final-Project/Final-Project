package com.csefinalproject.github.multiplayer.behaviour.client;

import com.csefinalproject.github.multiplayer.networking.NetworkEventManager;
import com.csefinalproject.github.multiplayer.networking.packet.PositionPacket;

public class ClientPacketHandler {
    ClientManager clientManager;
    NetworkEventManager networkEventManager;

    public ClientPacketHandler(ClientManager clientManager, NetworkEventManager networkEventManager) {
        this.clientManager = clientManager;
        this.networkEventManager = networkEventManager;
    }

    public void startHandling() {
        this.networkEventManager.subscribeEvent(PositionPacket.class, (PositionPacket packet) -> {
            System.out.println("I got the gaming " + packet.getPosition());
        });
    }
}
