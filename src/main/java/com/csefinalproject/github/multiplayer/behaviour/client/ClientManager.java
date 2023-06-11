package com.csefinalproject.github.multiplayer.behaviour.client;

import com.buildingjavaprograms.drawingpanel.DrawingPanel;
import com.buildingjavaprograms.drawingpanel.PanelInput;
import com.csefinalproject.github.multiplayer.Main;
import com.csefinalproject.github.multiplayer.behaviour.shared.Entity;
import com.csefinalproject.github.multiplayer.behaviour.shared.Player;
import com.csefinalproject.github.multiplayer.networking.NetworkEventManager;
import com.csefinalproject.github.multiplayer.networking.client.Client;
import com.csefinalproject.github.multiplayer.networking.exceptions.ConnectionFailedException;
import com.csefinalproject.github.multiplayer.networking.packet.ChatPacket;
import com.csefinalproject.github.multiplayer.networking.packet.InputDataPacket;
import com.csefinalproject.github.multiplayer.networking.packet.JoinRequestPacket;
import com.csefinalproject.github.multiplayer.util.Ticker;

import java.awt.*;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class ClientManager {
	private static ClientManager instance;

	private final Client client;
	private final ClientRenderer clientRenderer;
	private final PanelInput panelInput;
	private final List<Entity> entityList = new ArrayList<>();
	final Ticker clientThread;

	public ClientManager(String name, String ip, short port) {
		instance = this;

		// Create Client
		System.out.println("[CLIENT] Creating Client.");
		this.client = new Client();

		// Create ClientRenderer
		System.out.println("[CLIENT] Creating ClientRenderer and Input.");
		this.clientRenderer = new ClientRenderer();
		this.panelInput = new PanelInput(clientRenderer.getDrawingPanel());

		// Connect to server
		connect(name, ip, port);

		// Create client thread
		this.clientThread = new Ticker(Main.TPS);
		this.clientThread.subscribe(this::clientTick);
		this.clientThread.start();

		// Start handling packets
		ClientPacketHandler handler = new ClientPacketHandler(this,new NetworkEventManager(client));
		handler.startHandling();

		this.client.sendPacket(new ChatPacket(this.client, "Hello!!!!!!"));
		this.client.sendPacket(new JoinRequestPacket(this.client));
	}

	private void clientTick() {
		if (DrawingPanel.getInstances() == 0) {
			client.disconnect();
			clientThread.stop();
			return;
		}

		// Input
		boolean w = panelInput.keyDown('w');
		boolean a = panelInput.keyDown('a');
		boolean s = panelInput.keyDown('s');
		boolean d = panelInput.keyDown('d');
		client.sendPacket(new InputDataPacket(client, w, a, s, d, 0));

		// Draw all the entities
		this.clientRenderer.DrawEntities(entityList);
	}

	private void connect(String name, String ip, short port) {
		// Try to connect
		System.out.println("[CLIENT] Attempting connection to " + ip + " on port " + port + ".");
		try {
			this.client.connect(ip, port, name);
		} catch (ConnectionFailedException | UnknownHostException e) {
			throw new RuntimeException(e);
		}
		System.out.println("[CLIENT] Connected to the server.");
	}

	/**
	 * Adds an {@link Entity} to the Entity list.
	 * @param entity The entity to add.
	 */
	public synchronized void AddEntity(Entity entity) {
		entityList.add(entity);
	}

	/**
	 * Removes an {@link Entity} from the Entity list.
	 * @param entity The entity to remove.
	 */
	public synchronized void RemoveEntity(Entity entity) {
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