package com.csefinalproject.github.multiplayer.behaviour.server;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.csefinalproject.github.multiplayer.behaviour.client.ClientPacketHandler;
import com.csefinalproject.github.multiplayer.behaviour.shared.Entity;

import com.csefinalproject.github.multiplayer.behaviour.shared.Player;
import com.csefinalproject.github.multiplayer.networking.NetworkEventManager;

import com.csefinalproject.github.multiplayer.networking.packet.*;
import com.csefinalproject.github.multiplayer.networking.server.ClientData;
import com.csefinalproject.github.multiplayer.networking.server.Server;

/**
 * This is the game manager. It is used to run a server and handle all the game logic. It is a singleton
 */
public class GameManager {
	private static GameManager instance;

	private final Server server;
	private final NetworkEventManager networkEventManager;
	private final GamePacketHandler gamePacketHandler;
	private final Map<ClientData, Entity> clientDataEntityMap = new ConcurrentHashMap<>();

	/**
	 * This constructor is used to create a new game manager
	 * @param port the port to open the server on
	 */
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

	/**
	 * Get the instance of the game manager
	 * @return the instance of the game manager
	 */
	public static GameManager getInstance() {
		return instance;
	}

	/**
	 * This method is used to get the server
	 * @return the server
	 */
	public Server getServer() {
		return server;
	}

	/**
	 * This method is used to get the network event manager
	 * @return the network event manager
	 */
	public NetworkEventManager getNetworkEventManager() {
		return networkEventManager;
	}

	/**
	 * This method is used to get the game packet handler
	 * @return the game packet handler
	 */
	public GamePacketHandler getGamePacketHandler() {
		return gamePacketHandler;
	}

	/**
	 * This method is used to get the client data entity map
	 * @return the client data entity map
	 */
	public Map<ClientData, Entity> getClientDataEntityMap() {
		return clientDataEntityMap;
	}
}
