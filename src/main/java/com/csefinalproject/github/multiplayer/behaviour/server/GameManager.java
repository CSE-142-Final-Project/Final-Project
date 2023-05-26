package com.csefinalproject.github.multiplayer.behaviour.server;

import java.util.Map;

import com.csefinalproject.github.multiplayer.behaviour.shared.Entity;
import com.csefinalproject.github.multiplayer.networking.ClientData;
import com.csefinalproject.github.multiplayer.networking.NetworkEventManager;
import com.csefinalproject.github.multiplayer.networking.Server;

public class GameManager {
	private static GameManager instance;

	private Server server;
	private NetworkEventManager networkEventManager;
	private Map<ClientData, Entity> clientDataEntityMap;

	public GameManager(String ip, short port) {
		instance = this;

		System.out.println("[SERVER] Creating the Server");
		this.server = new Server();

		System.out.println("[SERVER] Creating the NetworkEventManager");
		this.networkEventManager = new NetworkEventManager(this.server);


	}

	public static GameManager getInstance() {
		return instance;
	}

	public Map<ClientData, Entity> getClientDataEntityMap() {
		return clientDataEntityMap;
	}
}
