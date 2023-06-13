package com.csefinalproject.github.multiplayer.behaviour.server;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

import com.csefinalproject.github.multiplayer.behaviour.client.ClientPacketHandler;
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
	private final GamePacketHandler gamePacketHandler;
	private final Map<ClientData, Entity> clientDataEntityMap = new HashMap<ClientData,Entity>();

	public GameManager(short port) {
		instance = this;

		// Create the server
		System.out.println("[SERVER] Creating the server.");
		this.server = new Server();

		// Start the server
		this.server.start(port);
		System.out.println("[SERVER] Opening the server at " + this.server.getIp() + " on port " + port + ".");

		// Create the NetworkEventManager
		this.networkEventManager = new NetworkEventManager(this.server);

		// Create and start the packet handler
		this.gamePacketHandler = new GamePacketHandler(this, this.networkEventManager);
		this.gamePacketHandler.startHandling();
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

	public GamePacketHandler getGamePacketHandler() {
		return gamePacketHandler;
	}

	public Map<ClientData, Entity> getClientDataEntityMap() {
		return clientDataEntityMap;
	}
}
