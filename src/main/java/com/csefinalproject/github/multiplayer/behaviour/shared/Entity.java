package com.csefinalproject.github.multiplayer.behaviour.shared;

import com.buildingjavaprograms.drawingpanel.DrawingPanel;
import com.csefinalproject.github.multiplayer.behaviour.client.ClientManager;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

/**
 * This class is used to represent an entity
 */
public class Entity {
	private static final int SIZE = 64;

	private static short instances;
	private final short id;

	private final String name;
	private final String pathToTexture;
	private Image texture;
	private Point position;

	/**
	 * This constructor is used to create a new entity
	 * @param name the name of the entity
	 * @param pathToTexture the path to the texture
	 * @param position the position of the entity
	 * @param clientId the client id of the entity
	 */
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

	/**
	 * Draws the texture of this entity
	 * @param panel the panel to draw to
	 * @param g the graphics object to draw to
	 */
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

	/**
	 * This method is used to get the id
	 * @return the id
	 */
	public short getId() {
		return id;
	}

	/**
	 * This method is used to get the name
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * This method is used to get the path to the texture
	 * @return the path to the texture
	 */
	public String getPathToTexture() {
		return pathToTexture;
	}

	/**
	 * This method is used to set the position
	 * @param newPosition the new position
	 */
	public void setPosition(Point newPosition) {
		this.position = newPosition;
	}

	/**
	 * This method is used to get the position
	 * @return the position
	 */
	public Point getPosition() {
		return position;
	}

	/**
	 * This method is used to get the texture
	 * @return the texture
	 */
	public Image getTexture() {
		return texture;
	}
}
