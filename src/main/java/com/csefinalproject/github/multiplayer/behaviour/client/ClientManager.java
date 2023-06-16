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
import com.csefinalproject.github.multiplayer.networking.packet.PlayerLeftPacket;
import com.csefinalproject.github.multiplayer.util.Ticker;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * This class is used to manage the client
 */
public class ClientManager {
	private static ClientManager instance;

	private final Client client;
	private final ClientRenderer clientRenderer;
	private final PanelInput panelInput;
	private final List<Entity> entityList = new CopyOnWriteArrayList<>();
	final Ticker clientThread;

	/**
	 * Get the instance of the client manager
	 * @param name the name of the client
	 * @param ip the ip of the server
	 * @param port the port of the server
	 */
	public ClientManager(String name, String ip, short port) {
		instance = this;

		// Create Client
		System.out.println("[CLIENT] Creating Client.");
		this.client = new Client();

		// Connect to server
		connect(name, ip, port);

		// Create ClientRenderer
		System.out.println("[CLIENT] Creating ClientRenderer and Input.");
		this.clientRenderer = new ClientRenderer();
		this.panelInput = new PanelInput(clientRenderer.getDrawingPanel());

		// Create client thread
		this.clientThread = new Ticker(Main.TPS);
		this.clientThread.subscribe(this::clientTick);
		this.clientThread.start();

		// Send a join request
		this.client.sendPacket(new JoinRequestPacket(this.client));

		// Start handling packets
		ClientPacketHandler handler = new ClientPacketHandler(this,new NetworkEventManager(client));
		handler.startHandling();
	}

	/**
	 * This method is called every tick by the client thread.
	 */
	private void clientTick() {
		if (DrawingPanel.getInstances() == 0) {
			// This is bad... but for now it works :D
			this.client.sendPacket(new PlayerLeftPacket(this.client, (short)0));

			this.client.disconnect();
			this.clientThread.stop();
			return;
		}

		// Input
		boolean w = panelInput.keyDown('w');
		boolean s = panelInput.keyDown('s');
		boolean a = panelInput.keyDown('a');
		boolean d = panelInput.keyDown('d');

		if(w || s || a || d) {
			client.sendPacket(new InputDataPacket(client, w, s, a, d, 0));
		}

		// Draw all the entities
		this.clientRenderer.DrawEntities(entityList);
	}

	/**
	 * Connects to the server.
	 * @param name The name of the client.
	 * @param ip The IP of the server.
	 * @param port The port of the server.
	 */
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

	/**
	 * Gets the instance of the client manager
	 * @return the instance of the client manager
	 */
	public static ClientManager getInstance() {
		return instance;
	}

	/**
	 * Gets the client
	 * @return the client (see {@link Client})
	 */
	public Client getClient() {
		return client;
	}

	/**
	 * Gets the client renderer
	 * @return the client renderer (see {@link ClientRenderer})
	 */
	public ClientRenderer getClientRenderer() {
		return clientRenderer;
	}

	/**
	 * Gets the list of entities
	 * @return the list of entities
	 */
	public List<Entity> getEntityList() {
		return entityList;
	}
}