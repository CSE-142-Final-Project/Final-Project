package com.csefinalproject.github.multiplayer.behaviour.client;

import com.buildingjavaprograms.drawingpanel.PanelInput;
import com.csefinalproject.github.multiplayer.behaviour.shared.Entity;
import com.csefinalproject.github.multiplayer.networking.client.Client;
import com.csefinalproject.github.multiplayer.networking.exceptions.ConnectionFailedException;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class ClientManager {
	private static ClientManager instance;

	private Client client;
	private ClientRenderer clientRenderer;
	private PanelInput panelInput;
	private List<Entity> entityList;

	public ClientManager(String ip, short port) {
		instance = this;

		// Create Client
		System.out.println("[CLIENT] Creating Client.");
		this.client = new Client();

		// Create Entity List
		System.out.println("[CLIENT] Creating EntityList.");
		this.entityList = new ArrayList<>();

		// Create ClientRenderer
		System.out.println("[CLIENT] Creating ClientRenderer.");
		this.clientRenderer = new ClientRenderer();

		// Create Input
		System.out.println("[CLIENT] Creating PanelInput.");
		this.panelInput = new PanelInput(clientRenderer.getDrawingPanel());

		// Try to connect
		System.out.println("[CLIENT] Attempting connection to " + ip + " on port " + port);
		try {
			this.client.connect(ip, port, "Epicly Client");
		} catch (ConnectionFailedException | UnknownHostException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Adds an {@link Entity} to the Entity list.
	 * @param entity The entity to add.
	 */
	public void AddEntity(Entity entity) {
		entityList.add(entity);
	}

	/**
	 * Removes an {@link Entity} from the Entity list.
	 * @param entity The entity to remove.
	 */
	public void RemoveEntity(Entity entity) {
		entityList.remove(entity);
	}

	public static ClientManager getInstance() {
		return instance;
	}

	public Client getClient() {
		return client;
	}

	public ClientRenderer getClientRenderer() {
		return clientRenderer;
	}

	public List<Entity> getEntityList() {
		return entityList;
	}
}
