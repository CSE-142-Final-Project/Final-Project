package com.csefinalproject.github.multiplayer.behaviour.shared;

import com.csefinalproject.github.multiplayer.behaviour.client.ClientManager;

import java.awt.Point;

public class Entity {
	private short id;

	private String name;
	private String pathToTexture;
	private Point position;

	public Entity(String name, String pathToTexture, Point position) {
		this.name = name;
		this.pathToTexture = pathToTexture;
		this.position = position;

		// Add this to the entity list.
		ClientManager.getInstance().AddEntity(this);
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
