package com.csefinalproject.github.multiplayer.behaviour.client;

public class ClientManager {
	private static ClientManager instance;

	private ClientRenderer clientRenderer;

	public ClientManager() {
		instance = this;

		// Create clientRenderer
		System.out.println("[CLIENT] Creating ClientRenderer.");
		clientRenderer = new ClientRenderer();
	}

	public static ClientManager getInstance() {
		return instance;
	}

	public ClientRenderer getClientRenderer() {
		return clientRenderer;
	}
}
