package com.csefinalproject.github.multiplayer.behaviour.shared;

import com.buildingjavaprograms.drawingpanel.DrawingPanel;
import com.csefinalproject.github.multiplayer.behaviour.client.ClientManager;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class Entity {
	private static final int SIZE = 64;

	private static short instances;
	private final short id;

	private final String name;
	private final String pathToTexture;
	private Image texture;
	private Point position;

	public Entity(String name, String pathToTexture, Point position, short clientId) {
		this.id = clientId;
		instances += 1;

		this.name = name;
		this.pathToTexture = pathToTexture;
		this.position = position;

		// If we're the server ignore getting the texture
		if(ClientManager.getInstance() == null) {
			return;
		}

		// Get the texture
		try {
			texture = ImageIO.read(Objects.requireNonNull(getClass().getResource(pathToTexture)));
		} catch (IOException e) {
			throw new RuntimeException("Unable to get the texture at \"" + pathToTexture + "\"");
		} catch (NullPointerException e) {
			throw new RuntimeException(e);
		}

		// Add it to the client manager
		ClientManager.getInstance().AddEntity(this);
	}

	public void Draw(DrawingPanel panel, Graphics g) {
		// Draw texture
		g.drawImage(getTexture(), this.getPosition().x - (SIZE / 2), this.getPosition().y - (SIZE / 2), SIZE, SIZE, null, null);
	}

	/**
	 * Get rid of this entity.
	 */
	public void Destroy() {
		// Remove the entity from the entity list
		ClientManager.getInstance().RemoveEntity(this);
	}

	public short getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getPathToTexture() {
		return pathToTexture;
	}

	public void setPosition(Point newPosition) {
		this.position = newPosition;
	}
	public Point getPosition() {
		return position;
	}

	public Image getTexture() {
		return texture;
	}
}
