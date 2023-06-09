package com.csefinalproject.github.multiplayer.behaviour.shared;

import com.buildingjavaprograms.drawingpanel.DrawingPanel;
import com.csefinalproject.github.multiplayer.behaviour.client.ClientManager;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class Entity {
	private static short instances;
	private final short id;

	private final String name;
	private final String pathToTexture;
	private final Point position;
	private final Image texture;

	public Entity(String name, String pathToTexture, Point position) {
		this.id = instances;
		instances += 1;

		this.name = name;
		this.pathToTexture = pathToTexture;
		this.position = position;

		// Get the texture
		try {
			texture = ImageIO.read(Objects.requireNonNull(getClass().getResource(pathToTexture)));
		} catch (IOException e) {
			throw new RuntimeException("Unable to get the texture at \"" + pathToTexture + "\"");
		} catch (NullPointerException e) {
			throw new RuntimeException("The \"" + pathToTexture + "\" resource does not exist.");
		}

		// Try and add it to the client manager
		try {
			ClientManager.getInstance().AddEntity(this);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void Draw(DrawingPanel panel, Graphics g) {
		// Now they can write code that draws every frame!
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

	public Point getPosition() {
		return position;
	}

	public Image getTexture() {
		return texture;
	}
}
