package com.csefinalproject.github.multiplayer.behaviour.server;

import com.csefinalproject.github.multiplayer.behaviour.shared.Entity;
import com.csefinalproject.github.multiplayer.behaviour.shared.Player;
import com.csefinalproject.github.multiplayer.networking.NetworkEventManager;
import com.csefinalproject.github.multiplayer.networking.packet.*;
import com.csefinalproject.github.multiplayer.networking.packet.internal.DummyTimeoutPacket;
import com.csefinalproject.github.multiplayer.networking.server.ClientData;
import com.csefinalproject.github.multiplayer.networking.server.Server;

import java.awt.*;
import java.util.Map;

/**
 * This class is used to handle packets sent from the clients
 */
public class GamePacketHandler {

    private final GameManager gameManager;
    private final Server server;
    private final NetworkEventManager networkEventManager;

    /**
     * This constructor is used to create a new game packet handler
     * @param gameManager the game manager
     * @param networkEventManager the network event manager
     */
    public GamePacketHandler(GameManager gameManager, NetworkEventManager networkEventManager) {
        this.gameManager = gameManager;
        this.server = gameManager.getServer();
        this.networkEventManager = networkEventManager;
    }

    /**
     * This method is used to start handling events
     */
    public void startHandling() {
        this.networkEventManager.subscribeEvent(ChatPacket.class, (ChatPacket packet) -> handleNewChatMessage(packet));
        this.networkEventManager.subscribeEvent(InputDataPacket.class, (InputDataPacket packet) -> handleInput(packet));
        this.networkEventManager.subscribeEvent(JoinRequestPacket.class, (JoinRequestPacket packet) -> handleNewJoinRequest(packet));
        this.networkEventManager.subscribeEvent(PlayerLeftPacket.class, (PlayerLeftPacket packet) -> handlePlayerLeaving(packet));
        this.networkEventManager.subscribeEvent(DummyTimeoutPacket.class, (DummyTimeoutPacket packet) -> handleForcibleDC(packet));
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

    /**
     * This method is used to handle player input
     * @param packet the input data packet
     */
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
        System.out.println("[SERVER] " + sender.getIP() + ":" + sender.getPort() + " is asking to connect");
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

        // Add to the entity map
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

    /**
     * Handle a forcible disconnect
     * @param packet the fake packet containing the client data
     */
    private void handleForcibleDC(DummyTimeoutPacket packet) {
        ClientData disconnected = packet.getData();
        System.out.println("[SERVER] Client " + disconnected.getClientID() + " has timed out");

        this.gameManager.getClientDataEntityMap().remove(disconnected);

        this.server.broadcast(new PlayerLeftPacket(this.server, disconnected.getClientID()));
    }
}
