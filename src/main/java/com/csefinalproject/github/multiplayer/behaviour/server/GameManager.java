package com.csefinalproject.github.multiplayer.behaviour.server;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

import com.csefinalproject.github.multiplayer.behaviour.shared.Entity;

import com.csefinalproject.github.multiplayer.behaviour.shared.Player;
import com.csefinalproject.github.multiplayer.networking.NetworkEventManager;

import com.csefinalproject.github.multiplayer.networking.packet.*;
import com.csefinalproject.github.multiplayer.networking.server.ClientData;
import com.csefinalproject.github.multiplayer.networking.server.Server;


public class GameManager {
	private static GameManager instance;

	private final Server server;
	private final NetworkEventManager networkEventManager;
	private final Map<ClientData, Entity> clientDataEntityMap = new HashMap<>();

	public GameManager(short port) {
		instance = this;

		// Create the server
		System.out.println("[SERVER] Creating the server.");
		this.server = new Server();

		// Start the server
		System.out.println("[SERVER] Opening the server on port " + port + ".");
		this.server.start(port);

		// Create the NetworkEventManager
		this.networkEventManager = new NetworkEventManager(this.server);

//		// Broadcast messages sent from the clients
//		this.networkEventManager.subscribeEvent(ChatPacket.class, (ChatPacket packet) -> {
//			String message = this.server.getUserFromPacket(packet).getUsername() + ": " + packet.getMessage();
//
//			this.server.broadcast(new ChatPacket(this.server, message));
//			System.out.println(message);
//		});

		this.networkEventManager.subscribeEvent(JoinRequestPacket.class, (JoinRequestPacket packet) -> {
			// Send all the previous players
			for(Map.Entry<ClientData, Entity> entry : this.clientDataEntityMap.entrySet()) {
				System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
				// Create the packet
				PlayerJoinedPacket previousPlayerJoin = new PlayerJoinedPacket(
						this.server,
						entry.getValue().getName(),
						entry.getKey().getClientID()
				);

				// Send it to the new client
				System.out.println("[SERVER] Sending Client ID " + previousPlayerJoin.getClientId() + " to the new player with Client ID" + this.server.getUserFromPacket(packet).getClientID());
				this.server.send(previousPlayerJoin, this.server.getUserFromPacket(packet).getClientID());
			}

			// Create player
			Player newPlayer = new Player(this.server.getUserFromPacket(packet).getUsername(), new Point(60,50));

			// Add to entity map
			this.clientDataEntityMap.put(this.server.getUserFromPacket(packet), newPlayer);

			// Broadcast a join
			this.server.broadcast(new PlayerJoinedPacket(this.server, newPlayer.getName(), newPlayer.getId()));
		});

		try {
			Thread.sleep(10000L);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	public static GameManager getInstance() {
		return instance;
	}

	public Server getServer() {
		return server;
	}

	public NetworkEventManager getNetworkEventManager() {
		return networkEventManager;
	}

	public Map<ClientData, Entity> getClientDataEntityMap() {
		return clientDataEntityMap;
	}
}
