package com.csefinalproject.github.multiplayer.behaviour.server;

import java.util.HashMap;
import java.util.Map;

import com.csefinalproject.github.multiplayer.behaviour.shared.Entity;

import com.csefinalproject.github.multiplayer.networking.NetworkEventManager;

import com.csefinalproject.github.multiplayer.networking.packet.ChatPacket;
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
		System.out.println("[SERVER] Creating the Server.");
		this.server = new Server();

		// Start the server
		System.out.println("[SERVER] Opening the server on port " + port + ".");
		this.server.start(port);

		// Create the NetworkEventManager
		System.out.println("[SERVER] Creating the NetworkEventManager.");
		this.networkEventManager = new NetworkEventManager(this.server);

		// Broadcast messages sent from the clients
		this.networkEventManager.subscribeEvent(ChatPacket.class, (ChatPacket packet) -> {
			String message = this.server.getUserFromPacket(packet).getUsername() + ": " + packet.getMessage();

			this.server.broadcast(new ChatPacket(this.server, message));
			System.out.println(message);
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
