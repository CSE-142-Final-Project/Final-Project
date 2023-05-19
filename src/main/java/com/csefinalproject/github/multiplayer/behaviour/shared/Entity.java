package com.csefinalproject.github.multiplayer.behaviour.shared;

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
