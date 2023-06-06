package com.csefinalproject.github.multiplayer.behaviour.shared;

import com.buildingjavaprograms.drawingpanel.DrawingPanel;
import com.csefinalproject.github.multiplayer.behaviour.client.ClientManager;
import com.csefinalproject.github.multiplayer.util.Ticker;

import java.awt.*;

public class Entity {
	private static short instances;
	private final short id;

	private final String name;
	private final String pathToTexture;
	private final Point position;

	public Entity(String name, String pathToTexture, Point position) {
		this.id = instances;
		instances += 1;

		this.name = name;
		this.pathToTexture = pathToTexture;
		this.position = position;
	}

	public void Draw(DrawingPanel panel, Graphics g) {
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
}
