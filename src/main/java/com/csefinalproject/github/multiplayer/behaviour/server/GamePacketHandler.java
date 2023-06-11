package com.csefinalproject.github.multiplayer.behaviour.server;

import com.csefinalproject.github.multiplayer.behaviour.shared.Entity;
import com.csefinalproject.github.multiplayer.behaviour.shared.Player;
import com.csefinalproject.github.multiplayer.networking.NetworkEventManager;
import com.csefinalproject.github.multiplayer.networking.packet.*;
import com.csefinalproject.github.multiplayer.networking.server.ClientData;
import com.csefinalproject.github.multiplayer.networking.server.Server;

import java.awt.*;
import java.util.Map;

public class GamePacketHandler {
    private final GameManager gameManager;
    private final Server server;
    private final NetworkEventManager networkEventManager;

    public GamePacketHandler(GameManager gameManager, NetworkEventManager networkEventManager) {
        this.gameManager = gameManager;
        this.server = gameManager.getServer();
        this.networkEventManager = networkEventManager;
    }

    public void startHandling() {
        this.networkEventManager.subscribeEvent(ChatPacket.class, (ChatPacket packet) -> handleNewChatMessage(packet));
        this.networkEventManager.subscribeEvent(InputDataPacket.class, (InputDataPacket packet) -> handleInput(packet));
        this.networkEventManager.subscribeEvent(JoinRequestPacket.class, (JoinRequestPacket packet) -> handleNewJoinRequest(packet));
        this.networkEventManager.subscribeEvent(PlayerLeftPacket.class, (PlayerLeftPacket packet) -> handlePlayerLeaving(packet));
    }

    /**
     * Broadcast messages sent from the clients
     */
    private void handleNewChatMessage(ChatPacket packet) {
        Server server = this.gameManager.getServer();
        String message = server.getUserFromPacket(packet).getUsername() + ": " + packet.getMessage();

        server.broadcast(new ChatPacket(server, message));
        System.out.println(message);
    }

    private void handleInput(InputDataPacket packet) {
        ClientData sender = this.server.getUserFromPacket(packet);

        for(Map.Entry<ClientData, Entity> entry : this.gameManager.getClientDataEntityMap().entrySet()) {
            if(entry.getKey().equals(sender)) {
                Point position = entry.getValue().getPosition();
                if(packet.isForward()) {
                    position.y -= 4;
                }
                if (packet.isBackward()) {
                    position.y += 4;
                }
                if (packet.isLeft()) {
                    position.x -= 4;
                }
                if (packet.isRight()) {
                    position.x += 4;
                }

                entry.getValue().setPosition(position);
                this.server.broadcast(new PositionPacket(this.server, entry.getValue().getPosition(), sender.getClientID()));
            }
        }
    }

    /**
     * Handle a new join request by sending all the other clients to the new client and create the player entity
     */
    private void handleNewJoinRequest(JoinRequestPacket packet) {
        ClientData sender = this.server.getUserFromPacket(packet);
        Map<ClientData, Entity> clientDataEntityMap = this.gameManager.getClientDataEntityMap();

        // Send all the previous players
        for(ClientData existingClient : clientDataEntityMap.keySet()) {
            // Create the packet
            PlayerJoinedPacket previousPlayerJoin = new PlayerJoinedPacket(
                    this.server,
                    existingClient.getUsername(),
                    existingClient.getClientID()
            );

            // Send it to the new client
            this.server.send(previousPlayerJoin, sender.getClientID());
        }

        // Create player
        Player newPlayer = new Player(sender.getUsername(), new Point(60,60), sender.getClientID());

        // Add to entity map
        clientDataEntityMap.put(sender, newPlayer);

        // Broadcast a join
        this.server.broadcast(new PlayerJoinedPacket(this.server, sender.getUsername(), newPlayer.getId()));
    }

    /**
     * Broadcast a player left packet after a player says they're going to leave...
     * Is there a way for the server to do this without the client needing to tell it? Yes. Do I care? no.
     */
    private void handlePlayerLeaving(PlayerLeftPacket packet) {
        ClientData sender = this.server.getUserFromPacket(packet);
        System.out.println("[SERVER] Client " + sender.getClientID() + " at " + packet.getIp() + " is leaving.");

        // Remove them from the list
        this.gameManager.getClientDataEntityMap().remove(sender);

        // Broadcast it to all the other clients
        this.server.broadcast(new PlayerLeftPacket(this.server, sender.getClientID()));
    }
}
