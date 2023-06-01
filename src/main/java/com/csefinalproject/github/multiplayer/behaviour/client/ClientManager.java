package com.csefinalproject.github.multiplayer.behaviour.client;

import com.buildingjavaprograms.drawingpanel.DrawingPanel;
import com.buildingjavaprograms.drawingpanel.PanelInput;
import com.csefinalproject.github.multiplayer.behaviour.shared.Entity;
import com.csefinalproject.github.multiplayer.networking.client.Client;
import com.csefinalproject.github.multiplayer.networking.exceptions.ConnectionFailedException;
import com.csefinalproject.github.multiplayer.networking.packet.ChatPacket;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ClientManager {
	private static ClientManager instance;

	private final Client client;
	private final ClientRenderer clientRenderer;
	private final PanelInput panelInput;
	private final List<Entity> entityList = new ArrayList<>();

	public ClientManager(String ip, short port) {
		instance = this;

		// Create Client
		System.out.println("[CLIENT] Creating Client.");
		this.client = new Client();

		// Create ClientRenderer
		System.out.println("[CLIENT] Creating ClientRenderer and Input.");
		this.clientRenderer = new ClientRenderer();
		this.panelInput = new PanelInput(clientRenderer.getDrawingPanel());

		// Try to connect
		System.out.println("[CLIENT] Attempting connection to " + ip + " on port " + port + ".");
		try {
			this.client.connect(ip, port, "Epic Client");
		} catch (ConnectionFailedException | UnknownHostException e) {
			throw new RuntimeException(e);
		}

		System.out.println("[CLIENT] Connected to the server.");

		// Get input from user
		Scanner console = new Scanner(System.in);
		while(true) {
			if (DrawingPanel.getInstances() == 0) {
				System.exit(0);
			}

			System.out.print("Write a message to send to the server: ");
			String message = console.nextLine();

			this.client.sendPacket(new ChatPacket(this.client, message));
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