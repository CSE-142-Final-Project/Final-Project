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

public class ClientManager {
	private static ClientManager instance;

	private final Client client;
	private final ClientRenderer clientRenderer;
	private final PanelInput panelInput;
	private final List<Entity> entityList = new CopyOnWriteArrayList<>();
	private final Ticker clientThread;

	private boolean isChatting = false;

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

	private void clientTick() {
		if (DrawingPanel.getInstances() == 0) {
			// This is bad... but for now it works :D
			this.client.sendPacket(new PlayerLeftPacket(this.client, (short)0));

			this.client.disconnect();
			this.clientThread.stop();
			return;
		}

		// Basic chatting
		if(panelInput.keyDown('t') && !this.isChatting) {
			this.isChatting = true;
			System.out.println("I am ready to chat");
			while(this.isChatting) {
				System.out.println(panelInput.readKey());
				if(this.panelInput.keyDown('\n')) {
					this.panelInput.flushKeyboardEvents();
					System.out.println("I am done chatting.");
					this.isChatting = false;
				}
			}
		}

		// Input
		boolean w = panelInput.keyDown('w') || panelInput.keyDown('W');
		boolean s = panelInput.keyDown('s') || panelInput.keyDown('S');
		boolean a = panelInput.keyDown('a') || panelInput.keyDown('A');
		boolean d = panelInput.keyDown('d') || panelInput.keyDown('D');

		if(w || s || a || d) {
			client.sendPacket(new InputDataPacket(client, w, s, a, d, 0));
		}

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

	public boolean isChatting() {
		return isChatting;
	}
}