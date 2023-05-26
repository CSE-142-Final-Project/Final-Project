package com.csefinalproject.github.multiplayer.behaviour.server;

import java.util.Map;

import com.csefinalproject.github.multiplayer.behaviour.shared.Entity;
import com.csefinalproject.github.multiplayer.networking.server.ClientData;

public class GameManager {
	private static GameManager instance;

	private Map<ClientData, Entity> clientDataEntityMap;

	public GameManager() {
		instance = this;

		System.out.println("[SERVER] It's me the server!");
	}

	public static GameManager getInstance() {
		return instance;
	}

	public Map<ClientData, Entity> getClientDataEntityMap() {
		return clientDataEntityMap;
	}
}
