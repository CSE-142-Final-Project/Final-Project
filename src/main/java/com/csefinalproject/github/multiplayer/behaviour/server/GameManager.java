package com.csefinalproject.github.multiplayer.behaviour.server;

import java.util.Map;
import com.csefinalproject.github.multiplayer.behaviour.shared.Entity;
import com.csefinalproject.github.multiplayer.networking.ClientData;

public class GameManager {
	private static GameManager instance;
	private Map<ClientData, Entity> clientDataEntityMap;

	public GameManager() {
		instance = this;
	}

	public static GameManager getInstance() {
		return instance;
	}

	public Map<ClientData, Entity> getClientDataEntityMap() {
		return clientDataEntityMap;
	}
}
