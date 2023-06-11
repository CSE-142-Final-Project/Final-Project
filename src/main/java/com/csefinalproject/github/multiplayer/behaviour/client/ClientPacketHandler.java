package com.csefinalproject.github.multiplayer.behaviour.client;

import com.csefinalproject.github.multiplayer.behaviour.shared.Entity;
import com.csefinalproject.github.multiplayer.behaviour.shared.Player;
import com.csefinalproject.github.multiplayer.networking.NetworkEventManager;
import com.csefinalproject.github.multiplayer.networking.packet.PlayerJoinedPacket;
import com.csefinalproject.github.multiplayer.networking.packet.PlayerLeftPacket;
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
        this.networkEventManager.subscribeEvent(PlayerLeftPacket.class, (PlayerLeftPacket packet) -> handlePlayerLeave(packet));
        this.networkEventManager.subscribeEvent(PositionPacket.class, (PositionPacket packet) -> handleMovement(packet));
    }

    private void handleNewPlayer(PlayerJoinedPacket packet) {
        System.out.println("[CLIENT] Creating a new player with the username: " + packet.getUsername());
        new Player(packet.getUsername(), new Point(60,60), packet.getClientId());
    }

    private void handlePlayerLeave(PlayerLeftPacket packet) {
        System.out.println("Kicking out client " + packet.getClientId());
        // Remove all entities with the ID
        for(Entity entity : this.clientManager.getEntityList()) {
            if(entity.getId() == packet.getClientId()) {
                System.out.println("They match!!");
                this.clientManager.RemoveEntity(entity);
            }
        }
    }

    private void handleMovement(PositionPacket packet) {
        // Set the position
        for(Entity entity : this.clientManager.getEntityList()) {
            if(entity.getId() == packet.getClientId()) {
                entity.setPosition(packet.getPosition());
            }
        }
    }
}
