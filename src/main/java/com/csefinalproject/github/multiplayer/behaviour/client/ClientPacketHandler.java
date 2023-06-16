package com.csefinalproject.github.multiplayer.behaviour.client;

import com.csefinalproject.github.multiplayer.behaviour.shared.Entity;
import com.csefinalproject.github.multiplayer.behaviour.shared.Player;
import com.csefinalproject.github.multiplayer.networking.NetworkEventManager;
import com.csefinalproject.github.multiplayer.networking.packet.PlayerJoinedPacket;
import com.csefinalproject.github.multiplayer.networking.packet.PlayerLeftPacket;
import com.csefinalproject.github.multiplayer.networking.packet.PositionPacket;

import java.awt.*;

/**
 * This class is used to handle packets on the client side
 */
public class ClientPacketHandler {

    ClientManager clientManager;
    NetworkEventManager networkEventManager;

    /**
     * This constructor is used to create a new client packet handler
     * @param clientManager the client manager
     * @param networkEventManager the network event manager
     */
    public ClientPacketHandler(ClientManager clientManager, NetworkEventManager networkEventManager) {
        this.clientManager = clientManager;
        this.networkEventManager = networkEventManager;
    }

    /**
     * This method is used to start handling packets
     */
    public void startHandling() {
        this.networkEventManager.subscribeEvent(PlayerJoinedPacket.class, (PlayerJoinedPacket packet) -> handleNewPlayer(packet));
        this.networkEventManager.subscribeEvent(PlayerLeftPacket.class, (PlayerLeftPacket packet) -> handlePlayerLeave(packet));
        this.networkEventManager.subscribeEvent(PositionPacket.class, (PositionPacket packet) -> handleMovement(packet));
    }

    /**
     * This method is used to handle a new player joining
     * @param packet the player joined packet
     */
    private void handleNewPlayer(PlayerJoinedPacket packet) {
        System.out.println("[CLIENT] Creating a new player with the username: " + packet.getUsername());
        new Player(packet.getUsername(), new Point(60,60), packet.getClientId());
    }

    /**
     * This method is used to handle a player leaving
     * @param packet the player left packet
     */
    private void handlePlayerLeave(PlayerLeftPacket packet) {
        // Remove all entities with the ID
        for(Entity entity : this.clientManager.getEntityList()) {
            if(entity.getId() == packet.getClientId()) {
                System.out.println("[CLIENT] Kicking out Client " + packet.getClientId());
                this.clientManager.RemoveEntity(entity);
            }
        }
    }

    /**
     * This method is used to handle a player moving
     * @param packet the position packet
     */
    private void handleMovement(PositionPacket packet) {
        // Set the position
        for(Entity entity : this.clientManager.getEntityList()) {
            if(entity.getId() == packet.getClientId()) {
                entity.setPosition(packet.getPosition());
            }
        }
    }
}
