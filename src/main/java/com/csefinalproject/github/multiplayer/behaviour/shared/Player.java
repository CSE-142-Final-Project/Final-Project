package com.csefinalproject.github.multiplayer.behaviour.shared;

import com.buildingjavaprograms.drawingpanel.DrawingPanel;
import com.csefinalproject.github.multiplayer.behaviour.shared.Entity;

import javax.imageio.ImageIO;
import java.io.File;
import java.awt.*;
import java.io.IOException;

/**
 * This class is used to represent a player
 */
public class Player extends Entity {
	/**
	 * This is the default player texture
	 */
	private static final String DEFAULT_PLAYER_TEXTURE = "/assets/player.png";

	/**
	 * This constructor is used to create a new player
	 * @param name the name of the player
	 * @param position the position of the player
	 * @param clientId the client id of the player
	 */
	public Player(String name, Point position, short clientId) {
		super(name, DEFAULT_PLAYER_TEXTURE, position, clientId);
	}

	/**
	 * This is a method that draws the player
	 * @param panel the panel to draw on
	 * @param g the graphics object
	 */
	@Override
	public void Draw(DrawingPanel panel, Graphics g) {
		super.Draw(panel, g);

		// Draw username
		g.setColor(Color.BLACK);
		g.drawString(this.getName(), this.getPosition().x - 45, this.getPosition().y - 35);
	}
}
