package com.csefinalproject.github.multiplayer.behaviour.client;

import com.csefinalproject.github.multiplayer.behaviour.shared.Player;
import com.csefinalproject.github.multiplayer.networking.NetworkEventManager;
import com.csefinalproject.github.multiplayer.networking.packet.PlayerJoinedPacket;
import com.csefinalproject.github.multiplayer.networking.packet.PositionPacket;

import java.awt.*;

public class ClientPacketHandler {
    ClientManager clientManager;
    NetworkEventManager networkEventManager;

    public ClientPacketHandler(ClientManager clientManager, NetworkEventManager networkEventManager) {
        this.clientManager = clientManager;
        this.networkEventManager = networkEventManager;
    }

    public void startHandling() {
        this.networkEventManager.subscribeEvent(PlayerJoinedPacket.class, (PlayerJoinedPacket packet) -> handleNewPlayer(packet));
    }

    private void handleNewPlayer(PlayerJoinedPacket packet) {
        System.out.println("Creating a new player with the username: " + packet.getUsername());
        Player newPlayer = new Player(packet.getUsername(), new Point(0,0));

        this.clientManager.AddEntity(newPlayer);
    }
}
