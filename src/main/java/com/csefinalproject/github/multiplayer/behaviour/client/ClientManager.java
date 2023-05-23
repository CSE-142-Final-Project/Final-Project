package com.csefinalproject.github.multiplayer.behaviour.client;

import com.buildingjavaprograms.drawingpanel.PanelInput;
import com.csefinalproject.github.multiplayer.behaviour.shared.Entity;

import java.util.List;

public class ClientManager {
	private static ClientManager instance;
	private PanelInput panelInput;
	private ClientRenderer clientRenderer;

	private List<Entity> entityList;

	public ClientManager() {
		instance = this;

		// Create ClientRenderer
		System.out.println("[CLIENT] Creating ClientRenderer.");
		clientRenderer = new ClientRenderer();

		// Create Input
		System.out.println("[CLIENT] Creating PanelInput.");
		panelInput = new PanelInput(clientRenderer.getDrawingPanel());
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

	public ClientRenderer getClientRenderer() {
		return clientRenderer;
	}

	public List<Entity> getEntityList() {
		return entityList;
	}
}
